# File included in the Docker Makefiles.
SELF_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
include $(SELF_DIR)Config.inc

NAME              ?= $(subst /,-,tmp-$(IMAGE_NAME))
# The path to the directory whose files are able to be included in the build
BUILD_PATH        ?= .
# The path to the Dockerfile to execute, relative to the build path
DOCKERFILE_PATH   ?= Dockerfile
# Docker arguments
DOCKER_ARGS       ?=
# Build arguments
BUILD_ARGS        ?=
# Run arguments
RUN_ARGS          ?=
# Execution arguments
EXEC_ARGS         ?=
FILENAME          ?= $(notdir $(IMAGE_NAME)).tar
CURRENT_DIR       ?= $(strip $(dir $(realpath $(MAKEFILE))))
TEST_DIRS         ?= $(sort $(patsubst tests/%/,%,$(dir $(wildcard tests/*/))))

# Asserts that a variable is defined:
check_defined = \
    $(strip $(foreach 1,$1, \
        $(call __check_defined,$1,$(strip $(value 2)))))
__check_defined = \
    $(if $(value $1),, \
        $(error Undefined $1$(if $2, ($2))$(if $(value @), \
                required by target `$@')))
lineeq = $(shell printf '%*s\n' "$${COLUMNS:-$$(tput cols)}" '' | tr ' ' =)
linedash = $(shell printf '%*s\n' "$${COLUMNS:-$$(tput cols)}" '' | tr ' ' -)

.PHONY: all generate build clean run start attach exec stop rm put get save load name tag fullname test

.SILENT:

# Generates the Docker image files
# Usage:
#     make generate                 Generates the docker image
generate::

# Builds the Docker image in the current Docker machine using the local cache
# Usage:
#     make                          Builds the docker image
#     make IMAGE_TAG="3.0"          Builds the docker image and assigns the specified tag
build::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, BUILD_PATH, build path)
	@:$(call check_defined, DOCKERFILE_PATH, dockerfile path)
	echo "Building image..."
	docker $(DOCKER_ARGS) image build $(BUILD_ARGS)                            \
	  --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ')                  \
	  --build-arg VCS_REF=$(git rev-parse --short HEAD)                        \
	  -f $(DOCKERFILE_PATH)                                                    \
	  -t $(IMAGE_NAME):$(IMAGE_TAG) $(BUILD_PATH)
	docker $(DOCKER_ARGS) image tag $(IMAGE_NAME):$(IMAGE_TAG) $(IMAGE_NAME):latest

# Builds the Docker image in the current Docker machine using the specified cache
# Usage:
#     make                          Builds the docker image
#     make IMAGE_TAG="3.0"          Builds the docker image and assigns the specified tag
build-cached::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, BUILD_PATH, build path)
	@:$(call check_defined, DOCKERFILE_PATH, dockerfile path)
	echo "Attempt to pull the last image so it can be used as a layer cache..."
	docker $(DOCKER_ARGS) image pull $(REGISTRY_URL)$(IMAGE_NAME):latest || true
	echo "Building image..."
	docker $(DOCKER_ARGS) image build $(BUILD_ARGS)                            \
	  --cache-from $(REGISTRY_URL)$(IMAGE_NAME):latest                         \
	  --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ')                  \
	  --build-arg VCS_REF=$(git rev-parse --short HEAD)                        \
	  -f $(DOCKERFILE_PATH)                                                    \
	  -t $(IMAGE_NAME):$(IMAGE_TAG) $(BUILD_PATH)
	docker $(DOCKER_ARGS) image tag $(IMAGE_NAME):$(IMAGE_TAG) $(IMAGE_NAME):latest


# Deletes the Docker image from the current Docker machine
# Usage:
#     make clean
clean::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	-docker $(DOCKER_ARGS) image rm $(IMAGE_NAME):$(IMAGE_TAG) 2>/dev/null
	-docker $(DOCKER_ARGS) image rm $(IMAGE_NAME):latest 2>/dev/null
	-docker $(DOCKER_ARGS) image rm $(REGISTRY_URL)$(IMAGE_NAME):$(IMAGE_TAG) 2>/dev/null
	-docker $(DOCKER_ARGS) image rm $(REGISTRY_URL)$(IMAGE_NAME):latest 2>/dev/null


# Starts the Docker container from the image and executes the specified command
# Usage:
#     make run
#     make run CMD=/bin/sh
run::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, CMD, command)
	docker $(DOCKER_ARGS) container run -it $(RUN_ARGS)                        \
	  --name $(NAME)                                                           \
	  $(IMAGE_NAME):$(IMAGE_TAG)                                               \
	  $(CMD)
	$(if $(NO_RM),echo "Container '$(NAME)' not removed",docker container rm $(NAME) > /dev/null)


# Starts the detached Docker container from the image
# Usage:
#     make start                    Starts the container with sh
#     make start NAME=mycontainer   Assign a name to the container
#     make start CMD=/bin/sh        Starts the container with a custom command
#     make start CMD=               Starts the container with the CMD in the Dockerfile
start::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, NAME, container name)
	docker $(DOCKER_ARGS) container run -itd $(RUN_ARGS)                       \
	  --name $(NAME)                                                           \
	  $(IMAGE_NAME):$(IMAGE_TAG)                                               \
	  $(CMD)
	$(if $(NO_RM),echo "Container '$(NAME)' not removed",docker container rm $(NAME) > /dev/null)


# Attach to a running container
# Usage:
#     make attach
#     make attach NAME=mycontainer
attach::
	@:$(call check_defined, NAME, container name)
	docker $(DOCKER_ARGS) container attach $(NAME)


# Executes a command in a running container
# Usage:
#     make exec CMD="ls -la"        Executes a custom command in the container
#     make exec NAME=mycontainer    Executes a command in the container with the specified name
exec::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, CMD, command)
	docker $(DOCKER_ARGS) container exec -it $(EXEC_ARGS)                      \
	  $(NAME)                                                                  \
	  $(CMD)


# Stops a running container
# Usage:
#     make stop                     Stops a running container
#     make stop NAME=mycontainer    Stops the container with the specified name
stop::
	@:$(call check_defined, NAME, container name)
	docker $(DOCKER_ARGS) container stop $(NAME)


# Removes a container
# Usage:
#     make rm                      Removes a container
#     make rm NAME=mycontainer     Removes the container with the specified name
rm::
	@:$(call check_defined, NAME, container name)
	docker $(DOCKER_ARGS) container rm $(NAME)


# Copies a file into a running container
# Usage:
#     make put SRC=myfile.txt DEST=/myfolder/myfile.txt     Copies a local file or folder to the specified path in the container
put::
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, SRC, source)
	@:$(call check_defined, DEST, destination)
	docker $(DOCKER_ARGS) container cp $(SRC) $(NAME):$(DEST)


# Copies a file out of a running container
# Usage:
#     make get SRC=/myfolder/myfile.txt DEST=myfile.txt     Copies a file or folder from the container to the specified path
get::
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, SRC, source)
	@:$(call check_defined, DEST, destination)
	docker $(DOCKER_ARGS) container cp $(NAME):$(SRC) $(DEST)


# Save the Docker image to a TAR file
# Usage:
#     make save                          Saves the docker image to a TAR file
#     make save FILENAME="output.tar.gz" Saves the docker image to the specified filename
save::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, FILENAME, output filename)
	docker $(DOCKER_ARGS) save $(IMAGE_NAME):$(IMAGE_TAG) | gzip > $(FILENAME)

# Loads the Docker image from a TAR file
# Usage:
#     make load                          Loads the docker image from a TAR file
#     make load FILENAME="output.tar"    Loads the docker image from the specified filename
load::
	@:$(call check_defined, FILENAME, input filename)
	docker $(DOCKER_ARGS) load -i $(FILENAME)


# Prints the name of the image
name::
	echo "$(IMAGE_NAME)"

# Prints the tag of the image
tag::
	echo "$(IMAGE_TAG)"

# Prints the name and tag of the image
fullname::
	echo "$(IMAGE_NAME):$(IMAGE_TAG)"

# Tests the Docker image.
# Usage:
#     make test                          Runs the tests from the tests/ folder on the Docker image
test:: $(addsuffix .test,$(TEST_DIRS))
	echo "Ran $(words $(TEST_DIRS)) test cases"

# Tests the Docker image with a specific test.
# Usage:
#     make mytest.test
%.test: %.test-prepare %.test-run %.test-cleanup
	echo "x" > /dev/null

# Prepares the test container and enters it before the test is run
# Usage:
#     make mytest.test-enter
%.test-enter: %.test-prepare
	docker $(DOCKER_ARGS) commit $(NAME)-test user/$(NAME)-test > /dev/null
	$(if $(NO_RM),echo "Container '$(NAME)-test' not removed",docker container rm $(NAME)-test > /dev/null)
	docker $(DOCKER_ARGS) container run -it $(RUN_ARGS)                        \
	  --name $(NAME)                                                           \
	  user/$(NAME)-test                                                        \
	  $(CMD)
	$(if $(NO_RM),echo "Image 'user/$(NAME)-test' not removed",docker image rm user/$(NAME)-test > /dev/null)

# Runs the test container and enters it after the test is run
# Usage:
#     make mytest.test-inspect
%.test-inspect: %.test-prepare %.test-run
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, CMD, command)
	docker $(DOCKER_ARGS) commit $(NAME)-test user/$(NAME)-test > /dev/null
	$(if $(NO_RM),echo "Container '$(NAME)-test' not removed",docker container rm $(NAME)-test > /dev/null)
	docker $(DOCKER_ARGS) container run -it $(RUN_ARGS)                        \
	  --name $(NAME)                                                           \
	  user/$(NAME)-test                                                        \
	  $(CMD)
	$(if $(NO_RM),echo "Image 'user/$(NAME)-test' not removed",docker image rm user/$(NAME)-test > /dev/null)


# Prepares the test.
%.test-prepare::
	@:$(call check_defined, IMAGE_NAME, image name)
	@:$(call check_defined, IMAGE_TAG, image tag)
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, DOCKER_USER_HOME, Docker user home dir)
	echo "TEST:" $*
	mkdir -p testresults/$*/
	docker $(DOCKER_ARGS) container create                                     \
	  $(RUN_ARGS)                                                              \
	  --network none                                                           \
	  $(shell test -e tests/$*/.env && echo "--env-file tests/$*/.env")        \
	  --name $(NAME)-test                                                      \
	  $(IMAGE_NAME):$(IMAGE_TAG) > /dev/null;                                  \
	# We tar the source directory, setting the permissions,
	# and then pipe it to Docker
	gtar -cf - --numeric-owner --owner 1000 --group 1000 -C tests/$*/. . | \
	  docker $(DOCKER_ARGS) container cp - $(NAME)-test:$(DOCKER_USER_HOME)/

# Runs the test
%.test-run::
	@:$(call check_defined, NAME, container name)
	@:$(call check_defined, DOCKER_USER_OUTPUT, Docker user output dir)
	docker $(DOCKER_ARGS) container start -a $(NAME)-test; ret=$$?;            \
	docker $(DOCKER_ARGS) container cp $(NAME)-test:$(DOCKER_USER_OUTPUT)/. testresults/$*/.; \
	cat testresults/$*/stdout.txt;                                             \
	if [ -s "testresults/$*/stderr.txt" ]; then                                \
	  echo "$(linedash)";                                                      \
	  cat testresults/$*/stderr.txt;                                           \
	fi
	if grep -Fq -m 1 "<failed" "testresults/$*/results.xml"; then              \
	  echo "$(linedash)";                                                      \
	  cat testresults/$*/results.xml;                                          \
	fi
	echo "$(lineeq)"
	exit $$ret

# Cleans up the test
%.test-cleanup::
	-$(if $(NO_RM),echo "Container '$(NAME)-test' not removed",docker container rm $(NAME)-test > /dev/null)

