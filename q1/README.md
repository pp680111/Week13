## 性能测试
### 测试环境
* CPU: AMD Ryzen 3600 6c12t
* 内存: 32G
* 硬盘: 西数SN550 1T
* 操作系统: Win10 WSL
* kafka版本：3.0.0



启动kafka集群的配置文件内容如下

```
broker.id=1   ## 根据节点不同调整
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
log.dirs=/tmp/kafka/kafka1-logs  ## 每个节点都配置不同的日志目录
num.partitions=1
num.recovery.threads.per.data.dir=1
offsets.topic.replication.factor=1
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1
log.retention.hours=168
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000
zookeeper.connection.timeout.ms=18000
delete.topic.enable=true
group.initial.rebalance.delay.ms=0
message.max.bytes=5000000
replica.fetch.max.bytes=500000
listeners=PLAINTEXT://localhost:9001   ## 当前节点绑定监听的地址
advertised.listeners=PLAINTEXT://localhost:9001  ## 同上
broker.list=localhost:9001,localhost:9002,localhost:9003  ## 所有节点监听地址列表
zookeeper.connect=localhost:2181
```

使用上述配置启动了三个kafka节点组成集群

创建topic的命令如下
```
./bin/kafka-topics.sh --bootstrap-server localhost:9001 --create --topic perfTest --partitions 3 --replication-factor 2
```

### 测试数据
#### producer
由于kafka可控制的参数较多，控制变量测试的话测试用例太多，这里就只测试partition=3,replicas=2的情况下kafka的性能表现

测试结果如下：
```
388786 records sent, 77757.2 records/sec (75.93 MB/sec), 340.2 ms avg latency, 451.0 ms max latency.
453330 records sent, 90666.0 records/sec (88.54 MB/sec), 336.3 ms avg latency, 539.0 ms max latency.
327171 records sent, 65434.2 records/sec (63.90 MB/sec), 420.0 ms avg latency, 1894.0 ms max latency.
406020 records sent, 81204.0 records/sec (79.30 MB/sec), 402.2 ms avg latency, 2035.0 ms max latency.
375314 records sent, 75062.8 records/sec (73.30 MB/sec), 400.7 ms avg latency, 1042.0 ms max latency.
2000000 records sent, 73494.285819 records/sec (71.77 MB/sec), 394.49 ms avg latency, 2037.00 ms max latency, 302 ms 50th, 998 ms 95th, 1746 ms 99th, 2029 ms 99.9th.
```
#### consumer
运行指令如下:
```
./bin/kafka-consumer-perf-test.sh --bootstrap-server localhost:9001 --topic perfTest --fetch-size 1048576 --messages 5000000
```

执行结果格式化之后如下
|start.time| end.time| data.consumed.in.MB| MB.sec| data.consumed.in.nMsg| nMsg.sec| rebalance.time.ms| fetch.time.ms| fetch.MB.sec| fetch.nMsg.sec|
| ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
|2021-11-06 17:26:16:449| 2021-11-06 17:26:22:263| 4882.8125| 839.8370| 5000000| 859993.1201| 239| 5575| 875.8408| 896860.9865|