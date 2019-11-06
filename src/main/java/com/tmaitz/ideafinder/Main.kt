package com.tmaitz.ideafinder

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Wrong number of argument. Should be 2")
        println("Example:")
        println("... <filename> '<pattern>'")
        println("Where: ")
        println(" <filename> - full path to analyzed file")
        println(" '<pattern>' - Intellij IDEA pattern")
        exitProcess(1)
    }
    val pattern = args[1]
    val filePath = args[0]
    val file = File(filePath)
    if (!file.exists()) {
        println("File '$filePath' doesn't exists")
        exitProcess(1)
    }
    if (!file.canRead()) {
        println("Can't read file '$filePath'")
        exitProcess(1)
    }
    val finder = Finder(pattern)
    // use default charset (UTF-8)
    file.readLines()
            .filter { finder.match(it) }
            // ToDo: implement correct sorting
            .sorted()
            .forEach { println(it) }
}
