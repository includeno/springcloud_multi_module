Producer=one_moudle

Consumer=two_moudle

配置使用docker运行的

# 环境配置

## zookeeper

```
#连接外部服务
docker run --net=host  --name zookeepername  -p 2181:2181  -d zookeeper:3.5.7
docker exec -it zookeepername  /bin/bash

修改配置application.properties中zookeeper链接
change zookeeper link in application.properties
```

# Producer

## 参考/reference

https://registry.hub.docker.com/u/includeno
https://registry.hub.docker.com/r/includeno/springcloud_multi_moduleproducer

## 镜像/image

```
docker pull includeno/springcloud_multi_moduleproducer:1.0
```



## 运行/run

```
docker run -itd -p 8003:8000 --net=host --env application_name="producer8000"  --name producer_container includeno/springcloud_multi_moduleproducer:1.0

docker run -itd -p 8003:8000 --net=host --name producer_container includeno/springcloud_multi_moduleproducer:1.0
```



## 查看/view

```
docker logs producer_container
docker logs -ft producer_container
docker exec -it  producer_container /bin/bash
```



## 清理/clean

```
docker stop producer_container
docker rm producer_container
docker image rm includeno/springcloud_multi_moduleproducer:1.0
```



# Consumer

## 参考/reference

https://registry.hub.docker.com/u/includeno
https://registry.hub.docker.com/r/includeno/springcloud_multi_modulecomsumer

## 镜像/image

```
docker pull includeno/springcloud_multi_modulecomsumer:1.0
```



## 运行/run

```
docker run -itd -p 5000:9000 --env application_name="consumer9000"  --name consumer_container includeno/springcloud_multi_modulecomsumer:1.0

docker run -itd -p 8080:9000 --name consumer_container includeno/springcloud_multi_modulecomsumer:1.0
```



## 查看/view

```
docker logs consumer_container
docker logs -ft consumer_container
docker exec -it  consumer_container /bin/bash
```



## 清理/clean

```
docker stop consumer_container
docker rm consumer_container
docker image rm includeno/springcloud_multi_modulecomsumer:1.0
```



# Request of Consumer

Get  http:// ip :port/user?id=1&&type=get

Get  http:// ip :port/user?id=1&&type=post

Get  http:// ip :port/user?id=1&&type=put

Get  http:// ip :port/user?id=1&&type=delete