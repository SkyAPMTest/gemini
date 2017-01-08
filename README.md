# Gemini
通过交换机镜像端口或者HUB采集“端到端”的性能信息，尽量完整地准确地测量“客户体验”，以此评估应用性能的各项指标。

* 无需修改或者侵入所需监视的应用程序
* 无视应用程序繁杂的编程语言
* 不会对所需监视的应用程序造成任何性能损耗

详情请见[Wiki](https://github.com/skywalking-developer/gemini/wiki)

Network sniffer and performance analysis.

* Read data from the network core switch or a hub, which set a mirror network input/output.
* Base on these data, **Gemini** can analysis system performance, but have no influences to the application.
* Combine with the [sky-walking](https://github.com/wu-sheng/sky-walking) tracer, reduce the tracer's cost, improve traced application performance.

### Quickstart
#### Step 1. Download the lastest release
[Download](https://github.com/skywalking-developer/gemini/releases) the lastest released version, and untar.

    > tar -xzf gemini-1.0.0-alpha1.tgz  
    
#### Step 2. Get the network card number  

    > cd gemini-sniffer
    > java -jar gemini-sniffer-1.0.0-alpha1.jar
    > 2016-12-31 13:05:27,630 [main] ERROR c.a.e.g.s.c.GeminiCmd - 未指定需要执行的操作
    > -c,--card-id <card id>               指定扫描的网卡编号
    > -D <arg>
    > -i,--interval-time <interval-time>   设定离线抓取模式下的新文件生成的间隔时间，单位(秒)
    > -o,--operation <operation>           [ card  | online | offline ] 指定需要执行的操作，card：获取网卡列表，online：扫描网卡数据立
    >                                                      即发送，offline：扫描网卡数据存储到文件，再读取文件发送
    > -p,--file-path <path>                指定离线抓取模式下文件存储的路径
    > -r,--read                            当选择离线模式时，读取文件发送分析模块
    > -w,--write                           当选择离线模式时，抓取网卡数据写入文件

windows  

    > java -Djava.library.path=./native/windows -jar gemini-sniffer-1.0.0-alpha1.jar -o card
    
linux

    > java -Djava.library.path=./native/linux -jar gemini-sniffer-1.0.0-alpha1.jar -o card
    
* After run the command, you may see some outputs like these:
```shell
     #0: \Device\NPF_{D0548256-0B61-4535-9651-DC1B91E124DD} [Microsoft]
     #1: \Device\NPF_{38D46CC0-57E8-4BEC-AA69-A902D879F06C} [VMware Virtual Ethernet Adapter]
     #2: \Device\NPF_{18D24468-9011-4691-89BF-E7FA75B20934} [Intel(R) 82579LM Gigabit Network Connection]
     #3: \Device\NPF_{4B226C94-45B4-4E1B-B32C-22689E4777BD} [VMware Virtual Ethernet Adapter]
 ```
 * Then you should choose ...

#### Step 3. Start the sniffer 
* First, you need a **Kafka** MQ.
* Then, start up the **gemini**.

windows  

    > java -Djava.library.path=./native/windows -jar gemini-sniffer-1.0.0-alpha1.jar -o online  
    
linux  

    > java -Djava.library.path=./native/linux -jar gemini-sniffer-1.0.0-alpha1.jar -o online

# Contributors
* 彭勇升 [@pengys5](https://github.com/pengys5)
* 吴晟 [@wusheng](https://github.com/wu-sheng)  Assist on Architecture, Design and APM tech.
