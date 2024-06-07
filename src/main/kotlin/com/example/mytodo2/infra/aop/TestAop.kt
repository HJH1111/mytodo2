package com.example.mytodo2.infra.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around

//@Aspect
//@Component
class TestAop {

    @Around("execution(* com.example.mytodo2.todoapp.service.TodoService.getTodo(..))")
    fun thisIsAdvice(joinPoint: ProceedingJoinPoint) {
        println("AOP START!!!")
        joinPoint.proceed()
        println("AOP END!!!")
    }

}