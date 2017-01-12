groupadd redis
useradd -d /aifs01/users/devredis -g redis -m devredis

passwd devredis
1qaz@WSX

wget http://download.redis.io/releases/redis-3.2.6.tar.gz
tar xzf redis-3.2.6.tar.gz
cd redis-3.2.6
make