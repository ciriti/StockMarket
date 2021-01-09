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
        sut.assertEquals("""
            Ciao
            io.github.ciriti:okhttp-socket-ext:2.2.2
             io.github.ciriti:okhttp-socket-ext:2.2.2
            Test
        """.trimIndent())
    }

}