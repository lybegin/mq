package com.example.mqdemo.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.mqdemo.dao.entity.TestTable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;


@Component
@Aspect
public class ApiAop {

    private final Logger log = LoggerFactory.getLogger(ApiAop.class);

    @Pointcut(value = "execution(* com.example.mqdemo.api.FirstApi.*(..))  && @annotation(requestMapping)" )
    public void cutController(RequestMapping requestMapping){

    }

    @Before(value = "cutController(requestMapping)")
    @Order(1)
    public void before(RequestMapping requestMapping){
        log.info("this is before!!!");
    }

    @Before(value = "cutController(requestMapping)")
    @Order(2)
    public void before1(RequestMapping requestMapping) {
        log.info("this is before1!!!");

    }


    @After(value = "cutController(requestMapping)")
    public void after(RequestMapping requestMapping){
        log.info("this is after!!!");
    }

   /* @AfterReturning(value = "cutController(requestMapping)" , returning = "table")
    public void afterReturning(RequestMapping requestMapping,TestTable table){
        log.info("this is afterReturning !!!");
        log.info(JSONObject.toJSONString(table));
    }*/

    @Around(value = "cutController(requestMapping)")
    public void record(ProceedingJoinPoint joinPoint , RequestMapping requestMapping){
       Object[] args =  joinPoint.getArgs();
       Class<?> clazz = joinPoint.getTarget().getClass();
       String functionName = joinPoint.getSignature().getName();
       log.info("function start ["+clazz.getSimpleName()+"."+ functionName +"]");
       log.info("function param" + "["+ JSONObject.toJSONString(args)+ "]");
        StopWatch watch = new StopWatch();
        watch.start();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if(result != null){
            result = (TestTable) result;
            ((TestTable) result).setLogId(System.currentTimeMillis());
        }
        watch.stop();
        log.info("total cost "+watch.getTotalTimeSeconds());
        if(result != null){
            log.info("function result " + JSONObject.toJSONString(result));
        }
    }

}
