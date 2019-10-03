#!/bin/bash

echo $GPG_DIR
echo "-----------------------------------"
if [[ $TRAVIS_PULL_REQUEST == "false" ]]; then
    mvn clean deploy -DskipTests --settings $GPG_DIR/settings.xml -Dci=true
    exit $?
fi
