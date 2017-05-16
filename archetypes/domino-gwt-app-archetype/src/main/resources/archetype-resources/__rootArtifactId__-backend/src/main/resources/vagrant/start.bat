rem start.bat/sh 172.20.207.160 22 ---> will deploy on remote machine 
rem start.bat/sh 127.0.0.1 10022 --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
rem start.bat/sh --> then don't provide any values when prompted --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
rem start.bat/sh --> then provide 127.0.0.1 and 10022 when prompted --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
rem start.bat/sh --> then provide 172.20.207.160 and 22 when prompted --> will deploy on remote machine

@echo off
set ip=%1
set port=%2

IF [%port%]==[] (
set ip=127.0.0.1
set port=10022
)

IF [%port%]==[] (
set ip=127.0.0.1
set port=10022
)

IF %ip%==127.0.0.1 (
IF %port%==10022 (
chcp 1252
vagrant destroy -f
vagrant up
)
)

chmod 600 ./deploy/id_rsa

set user=root

ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip% uname > user.log
set /p os=<user.log
del user.log

IF [%os%]==[] (
set os=Linux
)

ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  rm -rf temp; echo "temp folder deleted"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  mkdir temp; echo "temp folder created"
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% *.war %user%@%ip%:temp
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% deploy.ps1 %user%@%ip%:
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  chmod 777 deploy.ps1; echo "Deploy permission Changed"

echo Start Deploying process
IF %os%==Linux (
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% ./deploy/fix-path-unx.ps1 %user%@%ip%:fix-path.ps1
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% ./deploy/svc-control-unx.ps1 %user%@%ip%:svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  chmod 777 svc-control.ps1 fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  dos2unix svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  dos2unix fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  ./fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  ./deploy.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  rm -rf temp fix-path.ps1 svc-control.ps1 deploy.ps1
) ELSE (
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% ./deploy/fix-path-win.ps1 %user%@%ip%:fix-path.ps1
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P %port% ./deploy/svc-control-win.ps1 %user%@%ip%:svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  chmod 777 svc-control.ps1 fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  Powershell.exe -Command "./fix-path.ps1 & Exit-PSSession"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  Powershell.exe -Command "./deploy.ps1 & Exit-PSSession"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p %port% %user%@%ip%  rm -rf temp fix-path.ps1 svc-control.ps1 deploy.ps1
)

echo Deploying Completed Successfully
pause
