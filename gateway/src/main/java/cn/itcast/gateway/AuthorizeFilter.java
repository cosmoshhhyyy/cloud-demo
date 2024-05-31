package cn.itcast.gateway;

import org.checkerframework.checker.units.qual.C;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

//@Order(-1) // 过滤器优先级，越小越高
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 通过上下文获取请求
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();

        // 2. 获取authorization 参数
        String authorization = queryParams.getFirst("authorization");

        // 3. 判断是否是admin
        if ("admin".equals(authorization)) {
            // 是，放行
            return chain.filter(exchange); // 传递给下一个过滤链
        }

        // 否，设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 无权限

        // 拦截，返回
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
