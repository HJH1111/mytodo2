package com.example.mytodo2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy


@EnableAspectJAutoProxy
@SpringBootApplication
class Mytodo2Application

fun main(args: Array<String>) {
    runApplication<Mytodo2Application>(*args)
}
