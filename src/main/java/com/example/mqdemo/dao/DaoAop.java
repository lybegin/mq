package com.example.mqdemo.dao;

import com.example.mqdemo.serverConfig.MultiDataSourceHold;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class DaoAop {

  @Pointcut("execution(* com.example.mqdemo.mapper.*.*(..))")
  public void cutPoint() {

  }

  @Before(value = "cutPoint()", argNames = "joinPoint")
  public void beforeMethod(JoinPoint joinPoint) {
    System.out.println(joinPoint.getTarget());
  }

  @Around(value = "cutPoint()")
  public Object proces(ProceedingJoinPoint proceedingJoinPoint) {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    String methodName = signature.getName();
    try {
      Class clazz = proceedingJoinPoint.getTarget().getClass();
      Class bean = clazz.getInterfaces()[0];
      Dao annotation = (Dao) bean.getAnnotation(Dao.class);
      if(isMatch(methodName)) {
        //说明是从库读取
        if (annotation.dbName() == null) {
          MultiDataSourceHold.setDataSourceKey(MultiDataSourceHold.getSlaveDataSource(DataSourceType.test1));
        } else {
          MultiDataSourceHold.setDataSourceKey(MultiDataSourceHold.getSlaveDataSource(annotation.dbName()));
        }
      } else {
        //主库写操作
        if (annotation.dbName() == null) {
          MultiDataSourceHold.setDataSourceKey(DataSourceType.test1);
        } else {
          MultiDataSourceHold.setDataSourceKey(annotation.dbName());
        }
      }
      log.info("operator dataSource is:{}", MultiDataSourceHold.getDataSourceKey());
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      MultiDataSourceHold.clearDataSourceKey();
    }
    Object result = null;
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      result = proceedingJoinPoint.proceed();
      watch.stop();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      log.info("dao method: {},cost:{}", signature.getName(), watch.getLastTaskTimeMillis());
    }

    return result;
  }

  public boolean isMatch(String methodName){
    return methodName.startsWith("query") || methodName.startsWith("find") || methodName.startsWith("get");
  }

}
