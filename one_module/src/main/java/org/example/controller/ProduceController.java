package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class ProduceController {
    

    //服务发现
    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/user")
    public String get(@RequestParam String id){
        log.info("get producer 收到id:"+id);
        return id+":one_module_produce_controller";
    }

    @PostMapping("/user")
    public String add(@RequestBody String id){
        log.info("post producer 收到id:"+id);
        return id+":one_module_produce_controller";
    }

    @PutMapping("/user")
    public String update(@RequestBody String id){
        log.info("put producer 收到id:"+id);
        return id+":one_module_produce_controller";
    }

    @DeleteMapping("/user")
    public String delete(String id){
        log.info("delete producer 收到id:"+id);
        return id+":one_module_produce_controller";
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
