#!/bin/bash
mvn archetype:generate \
    -DarchetypeGroupId=org.dominokit.domino.archetypes \
    -DarchetypeArtifactId=domino-gwt-single-module-archetype \
    -DarchetypeVersion=1.0-rc.4 \
    -DarchetypeParentGroupId=${groupId} \
    -DarchetypeParentArtifactId=${artifactId} \
    -DarchetypeParentVersion=${version} \
    -DgroupId=${groupId} \
    -DartifactId=$1 \
    -Dmodule=${1^} \
    -Dsubpackage=$1

