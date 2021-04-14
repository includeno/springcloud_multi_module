#指定基础镜像，在其上进行定制 docker pull openjdk:11-jre
FROM openjdk:11-jre

#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8000

WORKDIR /app
ENV application_name="producer8000"
ENV port="8000"
ADD one_module/target/one_module-1.0.jar /app/producer.jar

#自定义变量 传入应用名称之类的https://docs.docker.com/engine/reference/commandline/run/#set-environment-variables--e-env-env-file

CMD java -jar /app/producer.jar --spring.application.name=$application_name --server.port=$port
#springboot 命令行启动 带参数
#java(java命令) -jar(执行jar包命令) xxx.jar(要执行的jar包) #--params(参数名)=aaa(参数值) --params2(参数名)=bbb(参数值)
#java -jar xxx.jar --params=aaa --params2=bbb

#maven 启动主pom的package命令
#在子模块下产生jar文件
#上传

#linux打包docker image
#cd /springboot/producer
#docker build . -t produce8000
#docker run -itd -p 8000:8000 --name produce8000_num1 produce8000



#停止容器
#docker stop consume9000_num1
#删除容器
#docker rm consume9000_num1
#删除镜像
#docker image rm consume9000
#docker stop consume9000_num1
#docker rm consume9000_num1
#docker image rm consume9000
#vscode 终端
#打包 vscode目录为./cloudpayment_8000
#docker build ./cloudpayment_8000 -t cloudpayment_8000

#运行
#docker run -it -p 8003:8000 --env application_name="producer8003"  --name producer_container cloudpayment_8000
#删除镜像
#docker image rm cloudpayment_8000
#停止容器
#docker stop cloudpayment_8000_container
#删除容器
#docker rm cloudpayment_8000_container

#sudo docker exec -it cloudpayment_8000 /bin/bash