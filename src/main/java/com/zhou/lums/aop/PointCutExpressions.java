package com.zhou.lums.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCutExpressions {

    @Pointcut("execution(* com.zhou.lums.service.*.*blockUser(..))")
    public void forBlockUser() {

    }

}
