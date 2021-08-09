#!/usr/bin/env bash
docker_registry=$1
port=$2
project_name=$3
profile=$4
docker_org_name=$5
image_tag=$6

echo "start script"

#remove empty tag images
none_image_ids=$(docker images | grep '<none>' | awk '{print $3}' | awk '!(a[$0]++)')
for image_id in $none_image_ids
do
   if [ -n "$image_id" ]
   then
	 echo "docker rmi tag <none> image: ${image_id}"
	 docker rmi "${image_id}"
   fi
done

if [ -z "$docker_registry" ]
then
 echo "1th arg docker_registry mustn't be null or empty"
 exit 1
fi

if [ -z "$port" ]
then
 echo "2th arg port mustn't be null or empty"
 exit 1
fi

if [ -z "$project_name" ]
then
 echo "3th arg project_name mustn't be null or empty"
 exit 1
fi

image_name="${docker_registry}/${docker_org_name:=publisher}/${project_name}:${image_tag:=latest}"
dir=/data/java-instances/"${profile:=qa}"-"${port}"

mkdir -p "${dir}"
echo "mkdir project_path"

container_id=$(docker ps -a | grep "${project_name}" | awk '{print $1}')

if [ -n "$container_id" ]
then
  echo "docker stop container_id:${container_id}"
  docker stop "${container_id}";
  echo "docker rm container_id:${container_id}"
  docker rm "${container_id}";
fi

image_ids=$(docker images | grep "${project_name}" | awk '{print $3}')

for image_id in $image_ids
do
   if [ -n "$image_id" ]
   then
	 echo "docker rmi ${image_id}"
	 docker rmi "${image_id}"
   fi
done

echo "docker run image ${image_name}"
docker run -d --name="${project_name}" -p "${port}":"${port}" -e "SPRING_PROFILES_ACTIVE=${profile:=qa}" -v "${dir}"/logs:/logs "${image_name}"
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

echo "end script"
