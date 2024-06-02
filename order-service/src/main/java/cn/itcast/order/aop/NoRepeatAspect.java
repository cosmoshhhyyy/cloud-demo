package cn.itcast.order.aop;

import cn.itcast.order.annotation.RepeatAnnotation;
import cn.itcast.order.constant.UserConstant;
import com.itcast.feign.pojo.User;
import org.apache.http.HttpRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class NoRepeatAspect {

    private RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

    @Pointcut("@annotation(cn.itcast.order.annotation.RepeatAnnotation)")
    public void pointCut(){}


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();

        User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);

        if (user == null) {
            // 用户未登录

            return null; // 返回用户未登录
        }

        Long id = user.getId();
        MethodSignature signature = (MethodSignature) point.getSignature();

        Method method = signature.getMethod();
        String name = method.getName();
        if (redisTemplate.hasKey(id + name)) {
            System.out.println("重复提交");
        } else {
            // 获取注解
            RepeatAnnotation annotation = method.getAnnotation(RepeatAnnotation.class);
            Long timeout = annotation.timeOut();

            redisTemplate.opsForValue().set(id + name, id + name, 1, TimeUnit.SECONDS);

        }
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            System.out.println("操作失败");
            throwable.printStackTrace();
        }

        return null;  // 返回错误运行
    }

}
