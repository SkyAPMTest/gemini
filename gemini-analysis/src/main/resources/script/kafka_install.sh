groupadd kafka
useradd -d /aifs01/users/devkafka -g kafka -m devkafka

passwd devkafka
Abc@1234

ssh-keygen -b 2048 -t rsa
scp -P 22 /aifs01/users/devkafka/.ssh/id_rsa.pub devkafka@10.1.245.163:/aifs01/users/devkafka/.ssh/authorized_keys
scp -P 22 /aifs01/users/devkafka/.ssh/id_rsa.pub devkafka@10.1.245.164:/aifs01/users/devkafka/.ssh/authorized_keys

chmod 755 authorized_keys

tar -xzf kafka_2.11-0.10.1.0.tgz
scp -r kafka_2.11-0.10.1.0 devkafka@10.1.245.164:/aifs01/users/devkafka/
scp -r dataDir devkafka@10.1.245.164:/aifs01/users/devkafka/
scp -r logDir devkafka@10.1.245.164:/aifs01/users/devkafka/