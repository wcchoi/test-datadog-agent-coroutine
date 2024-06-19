DataDog java agent Kotlin coroutine test
---


Build:

```
./gradlew :buildFatJar
wget https://github.com/DataDog/dd-trace-java/releases/download/v1.35.2/dd-java-agent.jar
export MODE=dev; export DD_WRITER_TYPE=LoggingWriter;export DD_RUNTIME_METRICS_ENABLED=false;export DD_SERVICE=ktor-coroutine;export DD_ENV=dev; export DD_VERSION=0.0.1;
java -javaagent:./dd-java-agent.jar -Ddd.integration.kotlin_coroutine.experimental.enabled=true -jar build/libs/com.example.test-datadog-agent-coroutine-all.jar
```

Make request:

1.

```
curlie :8084/testtimeout
```

Output:
```
[dd.trace 2024-06-19 15:05:11:990 +0800] [eventLoopGroupProxy-4-6] INFO datadog.trace.agent.common.writer.LoggingWriter - write(trace): [{"service":"ktor-coroutine","name":"afterTimeout","resource":"afterTimeout","trace_id":7995344675690629422,"span_id":5869192984530146339,"parent_id":0,"start":1718780711977310833,"duration":12672792,"error":0,"metrics":{"process_id":89440,"_dd.profiling.enabled":0,"_dd.agent_psr":1.0,"_sample_rate":1,"_dd.trace_span_attribute_schema":0,"thread.id":44},"meta":{"language":"jvm","runtime-id":"98dd3710-f2cb-4b4b-8501-a5105d50bceb","env":"dev","thread.name":"eventLoopGroupProxy-4-6","version":"0.0.1"}}]
[dd.trace 2024-06-19 15:05:12:017 +0800] [eventLoopGroupProxy-3-6] INFO datadog.trace.agent.common.writer.LoggingWriter - write(trace): [{"service":"ktor-coroutine","name":"netty.request","resource":"GET /testtimeout","trace_id":9010165622855630356,"span_id":3803398843301178024,"parent_id":0,"start":1718780711953203000,"duration":63611083,"type":"web","error":0,"metrics":{"process_id":89440,"_dd.profiling.enabled":0,"_dd.agent_psr":1.0,"peer.port":51514,"http.status_code":200,"_sample_rate":1,"_dd.trace_span_attribute_schema":0,"thread.id":43},"meta":{"http.url":"http://localhost:8084/testtimeout","language":"jvm","env":"dev","thread.name":"eventLoopGroupProxy-3-6","version":"0.0.1","peer.ipv6":"0:0:0:0:0:0:0:1","http.hostname":"localhost","component":"netty","span.kind":"server","runtime-id":"98dd3710-f2cb-4b4b-8501-a5105d50bceb","http.method":"GET","http.useragent":"curl/8.4.0"}},{"service":"ktor-coroutine","name":"afterTimeout3","resource":"afterTimeout3","trace_id":9010165622855630356,"span_id":6050472471448711532,"parent_id":3803398843301178024,"start":1718780712003157542,"duration":12679958,"error":0,"metrics":{"thread.id":44},"meta":{"env":"dev","thread.name":"eventLoopGroupProxy-4-6","version":"0.0.1"}},{"service":"ktor-coroutine","name":"afterTimeout2","resource":"afterTimeout2","trace_id":9010165622855630356,"span_id":4351420088726825244,"parent_id":3803398843301178024,"start":1718780711990440125,"duration":12654542,"error":0,"metrics":{"thread.id":44},"meta":{"env":"dev","thread.name":"eventLoopGroupProxy-4-6","version":"0.0.1"}},{"service":"ktor-coroutine","name":"insideTimeout","resource":"insideTimeout","trace_id":9010165622855630356,"span_id":3718919667548218321,"parent_id":3803398843301178024,"start":1718780711966724083,"duration":10367875,"error":0,"metrics":{"thread.id":44},"meta":{"env":"dev","thread.name":"eventLoopGroupProxy-4-6","version":"0.0.1"}},{"service":"ktor-coroutine","name":"beforeTimeout","resource":"beforeTimeout","trace_id":9010165622855630356,"span_id":522852667439804958,"parent_id":3803398843301178024,"start":1718780711953835167,"duration":12707041,"error":0,"metrics":{"thread.id":44},"meta":{"env":"dev","thread.name":"eventLoopGroupProxy-4-6","version":"0.0.1"}}]
```

ie `afterTimeout` is not a child of `netty.request` trace. It is a separate trace

2.
```
curlie :8084/testflow
```

Output:

```
[dd.trace 2024-06-19 15:06:19:227 +0800] [eventLoopGroupProxy-4-1] INFO datadog.trace.agent.common.writer.LoggingWriter - write(trace): [{"service":"ktor-coroutine","name":"outsideFlowSpan","resource":"outsideFlowSpan","trace_id":6625527789691766097,"span_id":702713430515886817,"parent_id":0,"start":1718780779225173458,"duration":1263750,"error":0,"metrics":{"process_id":89674,"_dd.profiling.enabled":0,"_dd.agent_psr":1.0,"_sample_rate":1,"_dd.trace_span_attribute_schema":0,"thread.id":29},"meta":{"language":"jvm","runtime-id":"56c19ce5-63ca-4b88-bac5-2ed26b3dc283","env":"dev","thread.name":"eventLoopGroupProxy-4-1","version":"0.0.1"}}]
[dd.trace 2024-06-19 15:06:19:245 +0800] [eventLoopGroupProxy-4-1] INFO datadog.trace.agent.common.writer.LoggingWriter - write(trace): [{"service":"ktor-coroutine","name":"netty.request","resource":"GET /testflow","trace_id":3235629852882306235,"span_id":8033169902748046340,"parent_id":0,"start":1718780779161627916,"duration":82240292,"type":"web","error":0,"metrics":{"process_id":89674,"_dd.profiling.enabled":0,"_dd.agent_psr":1.0,"peer.port":51529,"http.status_code":200,"_sample_rate":1,"_dd.trace_span_attribute_schema":0,"thread.id":28},"meta":{"http.url":"http://localhost:8084/testflow","language":"jvm","env":"dev","thread.name":"eventLoopGroupProxy-3-1","version":"0.0.1","peer.ipv6":"0:0:0:0:0:0:0:1","http.hostname":"localhost","component":"netty","span.kind":"server","runtime-id":"56c19ce5-63ca-4b88-bac5-2ed26b3dc283","http.method":"GET","http.useragent":"curl/8.4.0"}},{"service":"ktor-coroutine","name":"insideFlowSpan","resource":"insideFlowSpan","trace_id":3235629852882306235,"span_id":5773497874884428538,"parent_id":8033169902748046340,"start":1718780779221521333,"duration":105250,"error":0,"metrics":{"thread.id":24},"meta":{"env":"dev","thread.name":"DefaultDispatcher-worker-1","version":"0.0.1"}}]
```

ie `outsideFlowSpan` is not a child of `netty.request` trace. It is a separate trace