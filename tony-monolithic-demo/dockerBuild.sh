#!/usr/bin/env bash

TEMP=$(getopt -o r:p:n:P:N:t: --long docker-registry:,port:,project-name:,profile:,docker-org-name:,docker-org-name: \
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

if [ -z "$docker_registry" ]
then
 echo "-r|--docker-registry must be set"
 exit 1
fi

if [ -z "$port" ]
then
 echo "-p|--port must be set"
 exit 1
fi

if [ -z "$project_name" ]
then
 echo "-n|--project-name must be set"
 exit 1
fi

dir=/data/java-instances/"${profile}"-"${port}"

mkdir -p "${dir}"
echo "mkdir project_path ${dir}"

container_id=$(docker ps | grep "${project_name}" | awk '{print $1}')

if [ -n "$container_id" ]
then
  echo "docker stop container_id:${container_id}"
  docker stop "${container_id}";
fi

container_id=$(docker ps -a | grep "${project_name}" | awk '{print $1}')

if [ -n "$container_id" ]
then
  echo "docker rm container_id:${container_id}"
  docker rm "${container_id}";
fi

image_name="${docker_registry}/${docker_org_name}/${project_name}"
image_ids=$(docker images | grep -w "${image_name}" | grep -w "${image_tag}" | awk '{print $3}')

for image_id in $image_ids
do
   if [ -n "$image_id" ]
   then
	 echo "docker rmi ${image_id}"
	 docker rmi "${image_id}"
   fi
done

docker run -d --name="${project_name}" -p "${port}":"${port}" -e "JAVA_OPTS=-Dspring.profiles.active=${profile:=qa}" -v "${dir}"/logs:/logs "${image_name}"

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
