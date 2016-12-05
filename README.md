# Qyburn: a distributed resource manager

## Setting up
- From the ```demo``` directory, run ```../bin/compile.sh``` and run ```mvn exec:java```
- Run ```mvn exec:java``` from the ```worker``` directory

- Note about forked Maven [exec](https://github.com/danielsuo/exec-maven-plugin) goal

## To do
- Convert Task and TaskResult so that we don't need to subclass
- Run worker / node manager via scala, not maven?
- Split qyburn-worker into manager and worker
- Task dependencies
- Spark no-op communication overhead measurements