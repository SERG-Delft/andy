FROM maven:3-amazoncorretto-17-debian

WORKDIR /home/

RUN apt update \
 && apt install --no-install-recommends --yes \
    tini \
    bc \
    git \
    ssh \
    bash \
    sudo \
    wget \
    unzip \
 && rm -rf /var/lib/apt/lists/*

SHELL ["/bin/bash", "-c"]

ARG ANDY_DIR=/home/andy/
ARG ANDY_REPO=SERG-Delft/andy
ARG ANDY_BRANCH=main

# Prevent Docker from using an old version of the branch from cache
ADD "https://api.github.com/repos/${ANDY_REPO}/commits/${ANDY_BRANCH}?per_page=1" latest_commit

# Copy the full repo to the docker image
RUN git clone -n https://github.com/${ANDY_REPO}.git andy

# Checkout the current branch
WORKDIR ${ANDY_DIR}
RUN git checkout ${ANDY_BRANCH}

# Compile and install all dependencies
RUN mvn package -pl weblab-runner -am -DskipTests

ARG GNAME=student
ARG GID=1000
ARG UNAME=student
ARG UID=1000
ARG UHOME=/user_code
RUN set -o errexit -o nounset \
 && addgroup --gid ${GID} "${GNAME}" \
 && useradd --gid ${GID} --home "${UHOME}" --uid ${UID} "${UNAME}"

COPY --chown=${UNAME} user_code/ ${UHOME}
WORKDIR ${UHOME}

# Move the compiled classes
RUN mv /home/andy/weblab-runner/target/weblab-runner-1.0.jar /home/andy.jar

# Clean up
RUN rm -rf /home/andy/

USER ${UNAME}

# Setup the environment for running the JAR file
ENV WORKING_DIR=${UHOME}/code
ENV OUTPUT_DIR=${UHOME}/output

# Run the students solution against andy
ENTRYPOINT ["/usr/bin/tini", "--"]
CMD ( { \
 mv solution.txt ${WORKING_DIR}/Solution.java \
 && mv library.txt ${WORKING_DIR}/Library.java \
 && mv test.txt ${WORKING_DIR}/Configuration.java \
 && java -cp /home/andy.jar -XX:+TieredCompilation -XX:TieredStopAtLevel=1 nl.tudelft.cse1110.andy.AndyOnWebLab "$TASK_MODE" "$WORKING_DIR" "$OUTPUT_DIR"; } )
