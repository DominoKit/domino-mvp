#!/bin/sh
# start.bat/sh 172.20.207.160 22 ---> will deploy on remote machine
# start.bat/sh 127.0.0.1 10022 --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
# start.bat/sh --> then don't provide any values when prompted --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
# start.bat/sh --> then provide 127.0.0.1 and 10022 when prompted --> will call vagrant destroy , vagrant up , then deploy on local vagrant machine
# start.bat/sh --> then provide 172.20.207.160 and 22 when prompted --> will deploy on remote machine

ip=$1
port=$2

if [ -z $port ]; then

export ip=127.0.0.1
export port=10022

fi



if [ -z $port ]; then
export ip=127.0.0.1
export port=10022
fi

if [ $ip == 127.0.0.1 ]; then
if [ $port == 10022 ]; then
vagrant destroy -f
vagrant up
fi
fi

chmod 600 ./deploy/id_rsa

export user=root

os="$(ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip uname)"

if [ -z $os ]; then
export os=Linux
fi

#if ! [ $os == Linux ]; then
#export user=Administrator
#fi

ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  rm -rf temp; echo "temp folder deleted"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  mkdir temp; echo "temp folder created"
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port *.war $user@$ip:temp
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port ./deploy/deploy.ps1 $user@$ip:
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  chmod 777 deploy.ps1; echo "Deploy permission Changed"

echo Start Deploying process
if [ $os == Linux ]; then
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port ./deploy/fix-path-unx.ps1 $user@$ip:fix-path.ps1
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port ./deploy/svc-control-unx.ps1 $user@$ip:svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  chmod 777 svc-control.ps1 fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  dos2unix svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  dos2unix fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  ./fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  ./deploy.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  rm -rf temp fix-path.ps1 svc-control.ps1 deploy.ps1
else
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port ./deploy/fix-path-win.ps1 $user@$ip:fix-path.ps1
scp -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -P $port ./deploy/svc-control-win.ps1 $user@$ip:svc-control.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  chmod 777 svc-control.ps1 fix-path.ps1
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  Powershell.exe -Command "./fix-path.ps1 & Exit-PSSession"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  Powershell.exe -Command "./deploy.ps1 & Exit-PSSession"
ssh -i ./deploy/id_rsa  -oStrictHostKeyChecking=no   -p $port $user@$ip  rm -rf temp fix-path.ps1 svc-control.ps1 deploy.ps1
fi

echo Deploying Completed Successfully
