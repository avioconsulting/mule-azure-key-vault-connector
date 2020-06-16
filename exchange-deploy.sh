#!/bin/bash

GROUPID=${1}

# ensure a parameter was passed...
if [ ! $GROUPID ]; then
   echo "Please include a group id parameter."
   echo "   Example: "
   echo "   /$ ./exchange-deploy.sh xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
   exit;
fi

echo "Replacing groupId with <groupId>$GROUPID</groupId>"
sed 's|<groupId>com.avioconsulting.mule.connector</groupId>|<groupId>'$GROUPID'</groupId>|g' pom.xml > exchange-pom.xml

echo "Executing: mvn clean deploy -f exchange-pom.xml -P exchange" 
mvn clean deploy -f exchange-pom.xml -P exchange -DskipTests

echo "Removing temp files."
rm exchange-pom.xml

echo "Done!"
