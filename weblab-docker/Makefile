IMAGE_NAME := weblab/cse1110-andy
DOCKERFILE_PATH := Dockerfile
RUN_ARGS :=

MAKEFILE := $(abspath $(lastword $(MAKEFILE_LIST)))
SELF_DIR := $(dir $(MAKEFILE))
CMD := /bin/bash
BUILD_PATH := $(SELF_DIR)/../

all: build

include $(SELF_DIR)Makefile.inc