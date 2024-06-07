package com.example.mytodo2.infra.aop


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class StopWatch()