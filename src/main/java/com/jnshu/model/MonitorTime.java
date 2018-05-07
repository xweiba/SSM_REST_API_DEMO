package com.jnshu.model;

import java.util.Date;

/**
 * @program: taskTwo
 * @description: 性能监控信息类
 * @author: Mr.xweiba
 * @create: 2018-05-07 01:29
 **/

public class MonitorTime {
    // 类名
    private String className;
    // 方法名
    private String methodName;
    // 时间
    private Date logTime;
    // 计算时间
    private Long comsumeTime;

    @Override
    public String toString() {
        return "MonitorTime{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", logTime='" + logTime + '\'' +
                ", comsumeTime=" + comsumeTime +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Long getComsumeTime() {
        return comsumeTime;
    }

    public void setComsumeTime(Long comsumeTime) {
        this.comsumeTime = comsumeTime;
    }
}
