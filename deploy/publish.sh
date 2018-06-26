#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == "false" ]]; then
    mvn clean deploy -Dmaven.test.skip=true --settings $GPG_DIR/settings.xml -Dci=true
    exit $?
fi
