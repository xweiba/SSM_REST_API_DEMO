package com.jnshu.aop;

import com.jnshu.model.MonitorTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @program: taskTwo
 * @description: Controller handler 性能分析
 * @author: Mr.xweiba
 * @create: 2018-05-07 01:18
 * @Explain: Controller 切面配置只能配置在Spring MVC中. 详情 https://segmentfault.com/q/1010000003901757
 **/

@Aspect
public class TimerAspectController {

    private static Logger logger = LoggerFactory.getLogger(TimerAspectController.class);
    /** 
    * @Description: 定义切入点 
    * @Param: timer
    * @return: void 
    * @Author: Mr.Wang 
    * @Date: 2018/5/7 
    * @Explain: com.jnshu.controller.*Controller*.* 切入所有controller包下类名包含Controller的类的所有方法
    */ 
    @Pointcut(value = "execution(* com.jnshu.controller.*Controller*.*(..))")
    private void handlerTimer(){}

    @Around(value = "handlerTimer()")
    public Object controllerLogTimer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 创建性能监控信息类
        MonitorTime monitorTime = new MonitorTime();
        // 获取目标类名
        String clazzName = proceedingJoinPoint.getTarget().getClass().getName();
        // 获取目标方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        // 记录目标类名
        monitorTime.setClassName(clazzName);
        // 记录目标方法名
        monitorTime.setMethodName(methodName);
        // 记录开始时间
        monitorTime.setLogTime(new Date());

        // 计时并调用目标函数
        logger.debug("====Controller切面开始====");
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long time = System.currentTimeMillis() -start;
        monitorTime.setComsumeTime(time);
        logger.debug( "性能日志 Controller类: "+monitorTime.getClassName()+" 中方法 "+monitorTime.getMethodName()+" 已执行 "
                + "执行时间: " + monitorTime.getLogTime() + " 执行时长: " + monitorTime.getComsumeTime() );
        return result;
    }
}
