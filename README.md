# Gemini
通过交换机镜像端口或者HUB采集“端到端”的性能信息，尽量完整地准确地测量“客户体验”，以此评估应用性能的各项指标。

* 无需修改或者侵入所需监视的应用程序
* 无视应用程序繁杂的编程语言
* 不会对所需监视的应用程序造成任何性能损耗

Network sniffer and performance analysis.

* Read data from the network core switch or a hub, which set a mirror network input/output.
* Base on these data, **Gemini** can analysis system performance, but have no influences to the application.
* Combine with the [sky-walking](https://github.com/wu-sheng/sky-walking) tracer, reduce the tracer's cost, improve traced application performance.

# Contributors
* 彭勇升 [@pengys5](https://github.com/pengys5)
* 吴晟 [@wusheng](https://github.com/wu-sheng)  Assist on Architecture, Design and APM tech.
