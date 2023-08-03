#!/bin/bash
# assume in quack repo - root dir

# prepare files for move to /deploy
pushd ui
tar cvzf ui.tgz src/
popd
cp assembly/target/quack.war /deploy/.
cp assembly/target/lib/jetty-runner.jar /deploy/.
cp ui/ui.tgz /deploy/.

# move files from /deploy to 
cd /home/dokimion
rm -rf dokimion
mkdir dokimion
cd dokimion
mv /deploy/ui.tgz .
mv /deploy/quack.war dokimion.war
mv /deploy/jetty-runner.jar .
tar xvzf ui.tgz

# restart relevant system services
systemctl restart dokimion$1_server.service
systemctl restart dokimion$1_ui.service
systemctl status dokimion$1_server.service
systemctl status dokimion$1_ui.service
