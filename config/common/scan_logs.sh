#!/bin/bash 
#
SERVERNO=$1

TODAYSDATE=`date | awk -F" " '{print $3 $2}'`
JAVAOUTMEM=`grep -i outofmem /var/log/syslog | grep java` 
JAVAOUTMEMDATE=`echo $JAVAOUTMEM | awk -F" " '{print $1 $2}'`

if [[ "$JAVAOUTMEMDATE" == "$TODAYSDATE" ]]
then

echo "Java Dates EQUAL"

if [ -z "$JAVAOUTMEM" ]
then
  echo ""
else
  echo $JAVAOUTMEM | /usr/bin/mail -s "Java out of memory error" root
fi

fi

MONGOSOCK=`grep -i mongosocket /var/log/syslog`
MONGOSOCKDATE=`echo $MONGOSOCK | awk -F" " '{print $1 $2}'`

if [[ "$MONGOSOCKDATE" == "$TODAYSDATE" ]]
then

echo "MongoSocket Dates EQUAL"
if [ -z "$MONGOSOCK" ]
then
  echo ""
else
  echo $MONGOSOCK | /usr/bin/mail -s "Mongo Socket Exception" root
fi

fi

