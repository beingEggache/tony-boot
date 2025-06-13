#!/bin/sh

TEMP=$(getopt -o \
    r:d:p:n:P:N:t:e:o: --long \
    docker-registry:,project-dir:,port:,project-name:,profile:,docker-org-name:,image-tag:,env-file:,overwrite-config \
     -n 'dockerBuild.bash' -- "$@")

eval set -- "$TEMP"

while true ; do
        case "$1" in
                -r|--docker-registry) docker_registry=$2 ; shift 2 ;;
                -d|--project-dir) project_dir=$2 ; shift 2 ;;
                -p|--port) port=$2 ; shift 2 ;;
                -n|--project-name) project_name=$2 ; shift 2 ;;
                -P|--profile) profile=$2 ; shift 2 ;;
                -N|--docker-org-name) docker_org_name=$2 ; shift 2 ;;
                -t|--image-tag) image_tag=$2 ; shift 2 ;;
                -e|--env-file) env_file=$2 ; shift 2 ;;
                -o|--overwrite-config) overwrite_config=$2 ; shift 2 ;;
                --) shift ; break ;;
                *) break ;;
        esac
done

docker_org_name=${docker_org_name:=publisher}
profile=${profile:=qa}
image_tag=${image_tag:=latest}
overwrite_config=${overwrite_config:=false}

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

mkdir -p "${dir}" || { echo "Failed to create directory: ${dir}"; exit 1; }
echo "Created directory: ${dir}"

container_id=$(docker ps -af "name=${project_name}" -q)

if [ -n "$container_id" ]
then
  echo "Removing existing container: ${container_id}"
  docker rm -f "${container_id}" || { echo "Failed to remove container"; exit 1; }
fi

image_name="${docker_registry}/${docker_org_name}/${project_name}:${image_tag}"

# prune docker
echo "Cleaning up unused Docker resources"
docker system prune -f || echo "Warning: Failed to prune Docker system"

# pull latest images
echo "Pulling image: ${image_name}"
docker pull "${image_name}" || { echo "Failed to pull image"; exit 1; }

# run the latest image
docker run -d --name="${project_name}" \
-p "${port}":"${port}" \
-v "${dir}"/logs:/logs \
-e "PROFILE=${profile}" \
"${image_name}" || { echo "docker run failed."; exit 1; }


echo "Waiting for container to start..."
for i in {10..1}
do
   echo "$i seconds remaining..."
   sleep 1
done

container_id=$(docker ps -f "name=${project_name}" -f "status=running" -q)

if [ -z "$container_id" ]
then
 echo "$project_name start failed.please check log"
 docker logs --tail 100 "${project_name}"
 exit 1
fi

echo "Container ${project_name} (ID: ${container_id}) started successfully"
echo "done"
