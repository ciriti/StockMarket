package com.example.stockmarket

import org.junit.Assert

infix fun <T> T.assertEquals(t: T) = apply { Assert.assertEquals(t, this) }
fun <T> T?.assertNotNull() : T = apply { Assert.assertNotNull(this) }!!