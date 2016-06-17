#!/bin/sh
#=============================================================================
# Title       : runJobLauncher.sh
# Description : The script to run job application.
# Author      : liyangbin
# Version     : 1.0
# Date        : 2016-03-12
#=============================================================================
echo 
echo "Job started @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`."
echo 
warning_message="Syntax: runJobLauncher.sh <job name> <job parameters ...>"
if (test -z "$1") then 
    echo "Missing job name." 
    echo "$warning_message"; 
    echo 
    echo "Job ended @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`." 
    exit; 
fi; 
# fix env. issues
#./etc/profile 
if [ -f ~/.bashrc ]; then 
	. ~/.bashrc 
fi

# set jobName
export JOB_NAME=$1

# check existence of control file in the running location
CTRL_FILE=$HOME/$JOB_NAME.lock;
if [ -f $CTRL_FILE ]
then 
    echo "$JOB_NAME job is still executing, please wait for a while and try again later.";
     echo
    echo "Job ended @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`."
    exit 1;
fi
# create the control file in the running location
touch $CTRL_FILE

# log file
export LOG_FILE=$HOME/$JOB_NAME.log
export ZIP_LOG_FILE=$HOME/logs/$JOB_NAME.log.`date +%Y'.'%m%d'.'%H%M'.'`$$

# memory, default is 1G
if [ "$1" = "-javaMax" ]
then
    shift
    export memoryX=$1
    shift
else
    export memoryX="1024"
fi

# ----- Verify and Set Required Environment Variables -------------------------
export CLOUD_HOME=$HOME/sklcloud

# ----- Set Up The Classpath -------------------------------------------
CP=${CLOUD_HOME}/target/sklcloud/WEB-INF/classes 
CP=$CP:${CLOUD_HOME}/target/sklcloud/WEB-INF/lib/*
CP=$CP:${CLOUD_HOME}/src/main/resources/spring-config

if [ -f "$JAVA_HOME/lib/tools.jar" ] ; then
  CP=$CP:"$JAVA_HOME/lib/tools.jar"
fi

# ----- Execute The Requested Command -----------------------------------------
export JAVA_OPTS="-verbosegc -XX:+PrintGCDetails -XX:+PrintTenuringDistribution"
JAVA_OPTS=$JAVA_OPTS" "-server" "-Xms256M" "-Xmx$memoryX"M "-XX:NewSize=128m" "-XX:MaxNewSize=128m" "-XX:+UseConcMarkSweepGC" "-Xconcurrentio" "-Xnoclassgc"" 
JAVA_OPTS=$JAVA_OPTS" "-Doracle.jdbc.V8Compatible=true

echo "+================================================================================================================"
echo "+ CLASSPATH:       $CP"
echo "+ CLOUD_HOME:      $CLOUD_HOME"
echo "+ JAVA_HOME:       $JAVA_HOME"
echo "+ JOB_NAME:        $JOB_NAME"
echo "+ MEMORY:          $memoryX M"
echo "+ PARAMETERS:      $@"
echo "+ LOG_FILE:        $LOG_FILE"
echo "+ ZIP_LOG_FILE:    $ZIP_LOG_FILE".gz
echo "+================================================================================================================"
echo

echo "$JOB_NAME started @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`" >> $LOG_FILE

###########################
#
# java -classpath ./lib/*;./src;./classes;./spring-config com.skl.cloud.foundation.batchjob.JobRunner batchDownloadUserInfoJob gridSize=10 fetchSize=2 maxRows=10
#
###########################
$JAVA_HOME/bin/java ${JAVA_OPTS} -classpath $CP com.skl.cloud.foundation.batchjob.JobRunner $@ >> $LOG_FILE  2>&1

# clean, zip/move logs
echo "$JOB_NAME ended @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`" >> $LOG_FILE
if (test -f $LOG_FILE) then 
	cp $LOG_FILE $ZIP_LOG_FILE;
	gzip $ZIP_LOG_FILE;
	cat /dev/null > $LOG_FILE;
fi
rm -rf $CTRL_FILE
echo
echo "Job ended @ `date +%Y'-'%m'-'%d' '%H':'%M':'%S`." 
