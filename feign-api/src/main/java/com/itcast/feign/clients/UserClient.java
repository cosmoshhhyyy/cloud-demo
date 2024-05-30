package com.itcast.feign.clients;

import com.itcast.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value = "userservice", configuration = DefaultFeignConfiguration.class) // 指定服务名称
@FeignClient(value = "userservice") // 指定服务名称
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
