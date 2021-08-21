package com.zhou.lums.aop;


import com.zhou.lums.model.User;
import com.zhou.lums.respository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect {

    @Autowired
    UserRepository userRepository;

    //    @After("com.zhou.lums.aop.PointCutExpressions.forBlockUser()")
    @After("@annotation(SaveUserHandled)")
    public void print(JoinPoint thisJoinPoint) {
        System.out.println("==========");
        System.out.println("AOP TEST");
        System.out.println(thisJoinPoint);
        Object[] args = thisJoinPoint.getArgs();
        User user = (User) args[0];
        System.out.println(user);
        userRepository.save(user);
        System.out.println("==========");
    }

}
