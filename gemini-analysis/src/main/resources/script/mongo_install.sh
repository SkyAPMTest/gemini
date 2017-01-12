groupadd mongo
useradd -d /aifs01/users/devmongo -g mongo -m devmongo

passwd devmongo
1qaz@WSX

wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.4.1.tgz

ssh-keygen -b 2048 -t rsa
scp -P 22 /aifs01/users/devmongo/.ssh/id_rsa.pub devmongo@10.1.241.18:/aifs01/users/devmongo/.ssh/authorized_keys
scp -P 22 /aifs01/users/devmongo/.ssh/id_rsa.pub devmongo@10.1.241.19:/aifs01/users/devmongo/.ssh/authorized_keys
scp -P 22 /aifs01/users/devmongo/.ssh/id_rsa.pub devmongo@10.1.241.20:/aifs01/users/devmongo/.ssh/authorized_keys

chmod 755 authorized_keys

tar -xzf mongodb-linux-x86_64-3.4.1.tgz