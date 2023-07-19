# Variables:
# IMAGE_NAME            The name of the Docker image
# IMAGE_TAG             The tag of the Docker image
# DOCKER_USER_HOME      The home directory of the user in the Docker container (without trailing slash)
# DOCKER_USER_OUTPUT    The output directory of the user in the Docker container (without trailing slash)
# REGISTRY              The hostname and port of the registry; or empty
# REGISTRY_PATH         The path in the registry, ending with a slash; or empty
# REGISTRY_USERNAME     The username for logging into the registry
# REGISTRY_PASSWORD     The password for logging into the registry
# NAME                  The container name
# CMD                   The command to execute
# RUN_ARGS              The container run arguments
# EXEC_ARGS             The container exec arguments
# FILENAME              The filename for image saving to and loading from TAR file
# CURRENT_DIR           The path to the original Makefile
# SRC                   The file or folder to copy from the host
# DEST                  The path to copy to in the container

# NOTE: By default the containers are removed when stopped
# (through the use of the --rm flag).  To prevent this, redefine RUN_ARGS="":
#   make run RUN_ARGS=""

IMAGE_TAG               ?= latest
DOCKER_USER_HOME        ?= /user_code
DOCKER_USER_OUTPUT      ?= $(DOCKER_USER_HOME)/output
REGISTRY                ?= gitlab.ewi.tudelft.nl:4242
REGISTRY_USERNAME       ?=
REGISTRY_PASSWORD       ?=
REGISTRY_PATH           ?= /awe/weblab-docker-images/
REGISTRY_URL            ?= $(REGISTRY)$(REGISTRY_PATH)
CMD                     ?= /bin/bash
