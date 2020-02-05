#!/bin/bash
# https://stackoverflow.com/questions/1375133/how-to-detect-a-build-error-from-ant-maven-via-a-bash-script
mvn -v
if [[ "$?" -ne 0 ]] ; then
  echo 'Need Maven to compile and run'; exit $rc
fi

mvn compile
mvn exec:java -Dexec.mainClass="org.deeplearning4j.UserInterface"
