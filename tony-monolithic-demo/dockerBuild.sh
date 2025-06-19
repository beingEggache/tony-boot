#!/bin/sh

print_help() {
  echo "\nUsage: $0 [options]"
  echo "\nOptions:"
  echo "  -r, --docker-registry     Docker registry address (required)"
  echo "  -d, --project-dir         Project directory (optional, not used by default)"
  echo "  -p, --port                Exposed port (required)"
  echo "  -n, --project-name        Project name (required)"
  echo "  -P, --profile             Profile (default: qa)"
  echo "  -N, --docker-org-name     Docker organization name (default: publisher)"
  echo "  -t, --image-tag           Image tag (default: latest)"
  echo "  -e, --env-file            Env file to load (optional, will auto export if exists)"
  echo "  -o, --overwrite-config    Overwrite config (optional, not used by default)"
  echo "  -h, --help                Show this help message and exit"
  echo "\nExample:"
  echo "  $0 -r my.registry.com -p 8080 -n myapp -P prod -t v1.0.0"
  echo
}

# Check for help flag before getopt
for arg in "$@"; do
  case $arg in
    -h|--help)
      print_help
      exit 0
      ;;
  esac
  # do not shift here
}

# Check docker command
if ! command -v docker >/dev/null 2>&1; then
  echo "docker command not found. Please install Docker first."
  exit 1
fi

# Check getopt compatibility
if ! getopt --test > /dev/null; then
  echo "Your system's getopt is not compatible with this script. Please use GNU getopt or switch to the getopts version."
  exit 1
fi

TEMP=$(getopt -o r:d:p:n:P:N:t:e:o: --long \
    docker-registry:,project-dir:,port:,project-name:,profile:,docker-org-name:,image-tag:,env-file:,overwrite-config \
    -n 'dockerBuild.sh' -- "$@")
if [ $? != 0 ]; then
  echo "Failed to parse options." >&2
  exit 1
fi

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
log_dir=${log_dir:-/logs}

# xargs -r compatibility (macOS does not support -r)
xargs_r() {
  if xargs --no-run-if-empty echo test >/dev/null 2>&1; then
    xargs --no-run-if-empty "$@"
  else
    xargs "$@"
  fi
}

check_arg() {
   if [ -z "$1" ]
   then
    echo "Parameter $2 must be set. Current value: $1"
    echo "Usage: $0 -r <docker-registry> -p <port> -n <project-name>"
    exit 1
   fi
}

# Load env file if specified and exists
auto_load_env() {
  if [ -n "$env_file" ] && [ -f "$env_file" ]; then
    echo "==================== Loading env file: $env_file ======================"
    set -a
    . "$env_file"
    set +a
  fi
}

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

clean_project_resources() {
  echo "==================== Cleaning up project resources ===================="
  docker ps -a --filter "name=${project_name}" --filter "status=exited" -q | xargs_r docker rm
  docker images --filter "dangling=true" --filter "reference=*${project_name}*" -q | xargs_r docker rmi
}

pull_image_with_retry() {
  echo "==================== Pulling Docker image ============================"
  local n=0
  while [ $n -lt 3 ]; do
    docker pull "$image_name" && break
    n=$((n+1))
    echo "Retrying docker pull ($n/3)..."
    sleep 2
  done
  if [ $n -eq 3 ]; then
    echo "Failed to pull image after 3 attempts."
    exit 1
  fi
}

start_container() {
  echo "==================== Starting container =============================="
  docker run -d --name="${project_name}" \
  -p "${port}":"${port}" \
  -v "${dir}"/logs:${log_dir} \
  -e "PROFILE=${profile}" \
  "${image_name}" || { echo "docker run failed."; exit 1; }
}

wait_for_container() {
  echo "==================== Waiting for container to start ==================="
  for i in $(seq 10 -1 1)
  do
     echo "$i seconds remaining..."
     sleep 1
  done
}

check_container_status() {
  container_id=$(docker ps -f "name=${project_name}" -f "status=running" -q)
  if [ -z "$container_id" ]
  then
   echo "==================== Container failed to start ========================"
   echo "$project_name failed to start. Please check the log below."
   docker logs --tail 100 "${project_name}"
   echo "Common reasons:"
   echo "1. Image pull failed or image is invalid"
   echo "2. Port is already in use"
   echo "3. Configuration or environment variable error"
   exit 1
  fi
  echo "==================== Container started successfully ==================="
  echo "Container ${project_name} (ID: ${container_id}) started successfully"
  echo "done"
}

# Main process
check_arg "$docker_registry" "-r|--docker-registry"
check_arg "$port" "-p|--port"
check_arg "$project_name" "-n|--project-name"
auto_load_env
clean_project_resources
pull_image_with_retry

echo "==================== Start parameters ================================"
echo "Image: ${image_name}"
echo "Port: ${port}"
echo "Profile: ${profile}"
echo "Log directory: ${dir}/logs"

start_container
wait_for_container
check_container_status
