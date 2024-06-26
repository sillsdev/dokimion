#!/bin/bash -x
# assume in quack repo - root dir

pushd ui
tar cvzf ui.tgz src/
popd

# move files 
pushd /home/dokimion
rm -rf dokimion
mkdir dokimion
cd dokimion

cp ~1/ui/ui.tgz .
cp ~1/assembly/target/quack.war dokimion.war
cp ~1/assembly/target/lib/jetty-runner.jar .
cp ~1/tools/aes.java .
cp ~1/tools/RoleCapability.java .
cp ~1/tools/AdminPassword.java .
cp ~1/tools/TestcaseSizes.java .
cp ~1/tools/User.java .
cp ~1/tools/ConfigurationAttributes.java .
cp ~1/tools/DefaultProjectAttributes.java .
cp ~1/tools/mongodb_RoleCapability_init.js .
cp ~1/tools/mongodb_User_init.js .
cp ~1/tools/mongodb_TestcaseSizes_init.js .
cp ~1/tools/mongodb_User_init.js .
cp ~1/tools/mongodb_DefaultProjectAttributes_init.js .
cp ~1/tools/mongodb_ConfigurationAttributes_init.js .
tar xvzf ui.tgz
cp ~1/config/common/startup_dokimion_server.sh ../bin/.
cp ~1/config/common/startup_dokimion_ui.sh ../bin/.
cp ~1/config/common/run_jstatd.sh ../bin/.
chown dokimion:dokimion .
chown dokimion:dokimion -R *
chmod -R a+w *
chmod -R a+w .
cd src
cp -r node_modules/tinymce* public/.

# restart relevant system services
if [ $1 == "_dev" ]
then
   systemctl restart dokimion_server$1.service
   systemctl restart dokimion_ui$1.service
   systemctl status dokimion_server$1.service
   systemctl status dokimion_ui$1.service
else
   systemctl restart dokimion$1_server.service
   systemctl restart dokimion$1_ui.service
   systemctl status dokimion$1_server.service
   systemctl status dokimion$1_ui.service
fi
