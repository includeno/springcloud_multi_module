package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.util.RedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ConsumeController {
    //服务发现
    @Autowired
    DiscoveryClient discoveryClient;

    public static final String ZOOKEEPER_URL="http://produce8000";
    @Autowired
    RestTemplate restTemplate;

    @Value("${server.port}")
    String port;

    @Value("${spring.application.name}")
    String appname;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisLock redisLock;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/user")
    public String get(String id,String type){
        String result="";
        switch (type){
            case "get":{
                ResponseEntity<String> response = restTemplate.getForEntity(ZOOKEEPER_URL + "/user"+ "?id={id}", String.class,id);
                result=response.getBody();

                break;
            }
            case "post":{
                result= restTemplate.postForObject(ZOOKEEPER_URL + "/user", id ,String.class);
                break;
            }
            case "put":{
                restTemplate.put(ZOOKEEPER_URL + "/user",id);
                result="put";
                break;
            }
            case "delete":{
                restTemplate.delete(ZOOKEEPER_URL + "/user?id={id}",id);
                result="delete";
                break;
            }
        }
        log.info("consumer:"+result);
        return "consumer:"+result;
    }

    @GetMapping("/lock")
    public String getRedisLock(String id) throws InterruptedException {
        String result="";
        Long threadid=Thread.currentThread().getId();
        String threadname=Thread.currentThread().getName();
        String uuidString= UUID.randomUUID().toString();
        String key="redislock";
        String value=""+uuidString+threadname+threadid;
        String lockvalue=redisLock.get(key);

        while (lockvalue!=null &&!lockvalue.equals(value)){
            lockvalue=redisLock.get(key);
            Thread.sleep(1);
        }
        if(lockvalue==null){
            redisLock.lock(key,value);
            System.out.println(value+"=>"+appname+"加锁");
        }
        //抢到锁之后执行


        result= restTemplate.postForObject(ZOOKEEPER_URL + "/user", id ,String.class);
        result=result+"=>"+value+"=>"+appname;
        redisLock.unlock(key,lockvalue);
        System.out.println(value+"=>"+appname+"解锁");

        log.info("consumer:"+result);
        return "consumer:"+result;
    }

    public void tryLockOnce() {
        String key = "lock_";
        //1. 获取锁
        RLock lock = redissonClient.getLock(key);
        try {
            //加锁,此处使用和java的ReentrantLock类似
            lock.lock();
            //2.执行业务逻辑
            String num = (String) redisTemplate.opsForValue().get("num");
            Integer i = Integer.parseInt(num);
            i = i + 1;
            redisTemplate.opsForValue().set("num",i.toString());
        } finally {
            //3.释放锁
            lock.unlock();
            System.out.println("-----------------释放锁OK==>"+Thread.currentThread().getName());
        }
    }

    @GetMapping("/redissonlock")
    public String redissonLock(String id) throws InterruptedException {
        tryLockOnce();
        return "consumer:"+appname;
    }


    @GetMapping("/discovery")
    public Object discovery(String instancename){
        //获取所有的服务名称
        List<String> services=discoveryClient.getServices();
        for(String element:services){
            log.info("element: "+element);
        }
        //根据服务名称查询所有服务
        List<ServiceInstance> instances=discoveryClient.getInstances(instancename);
        for(ServiceInstance element:instances){
            log.info("instence:InstanceId "+element.getInstanceId()+" ,Host"+element.getHost()+" ,Port"+element.getPort()+" ,Scheme "+element.getScheme());
        }
        return this.discoveryClient;
    }
}
