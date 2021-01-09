package com.ciriti.stockmarket

import org.junit.Test

class RegexTest {

    @Test
    fun `regex`(){
        val r = """([\w\.]+):([\w\-]+):(\d)+\.(\d)+\.(\d)+""".toRegex()
        val sut = """
            Ciao
            io.github.ciriti:okhttp-socket-ext:1.1.1
             io.github.ciriti:okhttp-socket-ext:2.1.3
            Test
        """.trimIndent().replace(r, "io.github.ciriti:okhttp-socket-ext:2.2.2")

        r.find(sut)?.let {
            val updated = it.value.updateMavenDependency("3.3.3")
            updated.assertEquals("io.github.ciriti:okhttp-socket-ext:3.3.3")
            it.value.assertEquals("io.github.ciriti:okhttp-socket-ext:2.2.2")
        }

        sut.assertEquals("""
            Ciao
            io.github.ciriti:okhttp-socket-ext:2.2.2
             io.github.ciriti:okhttp-socket-ext:2.2.2
            Test
        """.trimIndent())
    }

    fun String.updateMavenDependency(version : String) : String{
        val list = this.split(":").toMutableList()
        if(list.size < 3) return this
        list[2] = version
        return list.joinToString(separator = ":")
    }

}