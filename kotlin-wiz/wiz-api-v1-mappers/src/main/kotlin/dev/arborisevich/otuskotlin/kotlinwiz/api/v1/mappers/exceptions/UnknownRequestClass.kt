package dev.arborisevich.otuskotlin.kotlinwiz.api.v1.mappers.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to QuizContext")
