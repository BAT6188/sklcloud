#!/bin/sh
while true; do
pidlist=`ps -ef|grep tomcat|grep -v "grep"|grep -v "tomcatdaemon.sh"|grep -v "tail"|awk '{print $2}'`
if [ "$pidlist" = "" ];    then #若未找到tomcat进程启动tomcat
echo "no tomcat pid alive";
echo "start tomcat";
$CATALINA_HOME/bin; 
./startup.sh ;
tail -f ../logs/catalina.out;
else
echo $(date)  "tomcat pid alive"
echo "tomcat Id list :$pidlist"    
echo $(date)  "tomcat pid alive" >>tomcatdaemon.log    
echo "tomcat Id list :$pidlist">>tomcatdaemon.log
fi
 
done
