#!/bin/bash
# expects variables to be set:
# - OSSRH_USER
# - OSSRH_PASS
# - GPG_KEY_NAME
# - GPG_PASSPHRASE
# expects file to exist:
# - .travis/gpg.asc

set -e

# Check the variables are set
if [ -z "$OSSRH_USER" ]; then
  echo "missing environment value: OSSRH_USER" >&2
  exit 1
fi

if [ -z "$OSSRH_PASS" ]; then
  echo "missing environment value: OSSRH_PASS" >&2
  exit 1
fi

if [ -z "$GPG_KEY_NAME" ]; then
  echo "missing environment value: GPG_KEY_NAME" >&2
  exit 1
fi

if [ -z "$GPG_PASSPHRASE" ]; then
  echo "missing environment value: GPG_PASSPHRASE" >&2
  exit 1
fi

# Prepare the local keyring (requires travis to have decrypted the file
# beforehand)
gpg --fast-import .travis/gpg.asc
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar
mvn clean test jacoco:report coveralls:report