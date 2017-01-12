groupadd sniffer
useradd -d /aifs01/users/sniffer -g sniffer -m sniffer

passwd sniffer
1qaz@WSX

scp -r lib sniffer@10.1.1.245:/aifs01/users/sniffer/
scp -r include sniffer@10.1.1.245:/aifs01/users/sniffer/
scp gemini-sniffer-1.0.0-alpha1.jar sniffer@10.1.1.245:/aifs01/users/sniffer/

yum search java | grep -i --color JDK
yum install java-1.8.0-openjdk.x86_64

java -Djava.library.path=/usr/lib64 -jar gemini-sniffer-1.0.0-alpha1.jar -o online -c 11 -k 10.1.241.18:9092,10.1.241.19:9092,10.1.241.20:9292

wget http://www.tcpdump.org/release/libpcap-1.8.1.tar.gz
tar -xzf libpcap-1.8.1.tar.gz

cp /libpcap-1.8.1/pcap /usr/include