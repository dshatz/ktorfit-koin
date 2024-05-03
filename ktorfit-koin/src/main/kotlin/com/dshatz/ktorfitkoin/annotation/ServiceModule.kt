package com.dshatz.ktorfitkoin.annotation

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ServiceModule(val scanPackage: String)
