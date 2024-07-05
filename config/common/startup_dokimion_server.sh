#!/bin/bash -x

# $1 - SERVER_NO

cd /home/dokimion/dokimion

if [ "$1" == "4" ] || [ "$1" == "5" ]
then
  /usr/bin/javac RoleCapability.java aes.java AdminPassword.java
  /usr/bin/java RoleCapability
  /usr/bin/javac TestcaseSizes.java aes.java AdminPassword.java
  /usr/bin/java TestcaseSizes
  /usr/bin/javac DefaultProjectAttributes.java aes.java AdminPassword.java
  /usr/bin/java DefaultProjectAttributes
  /usr/bin/javac User.java aes.java AdminPassword.java
  /usr/bin/java User
fi

/usr/bin/java -XX:+UseParallelGC -XX:-UseCompressedOops -Xmx1500m -XX:+HeapDumpOnOutOfMemoryError -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=1700m -Dmail.debug=true -Xbootclasspath/a:/etc/dokimion -jar /home/dokimion/dokimion/jetty-runner.jar /home/dokimion/dokimion/dokimion.war | awk -v SERVER_NO=$1 '{printf "dokimion_server_" SERVER_NO; print $0}' | logger -p user.info



