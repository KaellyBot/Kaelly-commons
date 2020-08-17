#!/bin/bash
if [ -n "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    mvn --settings "${TRAVIS_BUILD_DIR}/.travis/settings.xml" org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion="$TRAVIS_TAG" 1>/dev/null 2>/dev/null
else
    echo "not on a tag -> keep snapshot version in pom.xml"
fi

# Run the maven deploy steps
mvn deploy -P publish -DskipTests=true --settings "${TRAVIS_BUILD_DIR}/.travis/settings.xml"