
docker logs zookeepername
docker restart consume9000_num1
docker logs consume9000_num1
docker restart consume9000_num2
docker logs consume9000_num2

docker build . -t consume9000
docker run --env application_name=abc -itd -p 9000:9000 --name consume9000_num1 consume9000
docker run --env application_name=def -itd -p 9001:9000 --name consume9000_num2 consume9000
docker run --env application_name=ddd -itd -p 9002:9000 --name consume9000_num3 consume9000

docker stop consume9000_num1
docker rm consume9000_num1
docker stop consume9000_num2
docker rm consume9000_num2
docker image rm consume9000

docker build . -t produce8000
docker run -itd -p 8000:8000 --net=host --name produce8000_num1 produce8000
docker logs produce8000_num1
docker stop produce8000_num1
docker rm produce8000_num1
docker image rm produce8000