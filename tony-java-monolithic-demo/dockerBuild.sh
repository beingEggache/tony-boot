#!/usr/bin/env bash

TEMP=$(getopt -o \
    r:p:n:P:N:t: --long \
    docker-registry:,port:,project-name:,profile:,docker-org-name:,docker-org-name: \
     -n 'dockerBuild.bash' -- "$@")

eval set -- "$TEMP"

while true ; do
        case "$1" in
                -r|--docker-registry) docker_registry=$2 ; shift 2 ;;
                -p|--port) port=$2 ; shift 2 ;;
                -n|--project-name) project_name=$2 ; shift 2 ;;
                -P|--profile) profile=$2 ; shift 2 ;;
				-N|--docker-org-name) docker_org_name=$2 ; shift 2 ;;
				-t|--image-tag) image_tag=$2 ; shift 2 ;;
                --) shift ; break ;;
                *) break ;;
        esac
done

docker_org_name=${docker_org_name:=publisher}
profile=${profile:=qa}
image_tag=${image_tag:=latest}

check_arg(){
   if [ -z "$1" ]
   then
    echo "$2 must be set"
    exit 1
   fi
}

check_arg "$docker_registry" "-r|--docker-registry"
check_arg "$port" "-p|--port"
check_arg "$project_name" "-n|--project-name"

dir=/data/java-instances/"${project_name}"

mkdir -p "${dir}"
echo "mkdir ${dir}"

container_id=$(docker ps -a | grep -w "${project_name}" | awk '{print $1}')

if [ "$container_id" ]
then
  echo "docker rm container_id:${container_id}"
  docker rm -f "${container_id}";
fi

image_name="${docker_registry}/${docker_org_name}/${project_name}"
# pull latest images
docker pull "${image_name}"
# prune docker
docker system prune -f
# run the latest image
docker run -d --name="${project_name}" \
-p "${port}":"${port}" \
-v "${dir}"/logs:/logs \
-e "JAVA_OPTS=-Dspring.profiles.active=${profile:=qa}" \
"${image_name}"

run_status=$?
if [ $run_status != 0 ]
then
  echo "docker run failed. status:${run_status}"
  exit 1
fi

for i in {10..1}
do
   echo "$i"
   sleep 1
done

container_id=$(docker ps| grep "${project_name}" | awk '{print $1}')

if [ -z "$container_id" ]
then
 echo "$project_name start failed.please check log"
 docker logs --tail 100 "${project_name}"
 exit 1
fi

echo "done"
