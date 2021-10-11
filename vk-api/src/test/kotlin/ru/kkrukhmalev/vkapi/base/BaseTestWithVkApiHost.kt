package ru.kkrukhmalev.vkapi.base

import org.testng.SkipException
import org.testng.annotations.BeforeClass
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseTestWithVkApiHost {
    companion object {
        const val TIMEOUT: Long = 1_000
    }
    
    private val properties = Properties()
    
    protected val host: String
        get() = properties.getProperty("vkapi.host")

    protected val port: Int
        get() = properties.getProperty("vkapi.port").toInt()
    
    protected val version: String
        get() = properties.getProperty("vkapi.version")
    
    protected val accessToken: String
        get() = properties.getProperty("vkapi.access.token")

    @BeforeClass
    fun loadProperties() =
        this.javaClass.classLoader.getResourceAsStream("config.properties").use {
            properties.load(it)
        }
    
    @BeforeClass(dependsOnMethods = ["loadProperties"])
    fun checkHost() {
        if (!checkHost(host)) 
            throw SkipException("Host '$host' is not available")
    }


    private fun checkHost(host: String): Boolean = nativePing(host) || nativePing6(host)

    private fun nativePing(host: String) = nativePingImpl("ping", host)

    private fun nativePing6(host: String) = nativePingImpl("ping6", host)

    private fun nativePingImpl(cmd: String, host: String): Boolean {
        return try {
            val pingProcess = ProcessBuilder(cmd, "-c", "1", host).start()
            if (!pingProcess.waitFor(TIMEOUT, TimeUnit.MILLISECONDS))
                false
            else
                pingProcess.exitValue() == 0
        } catch (e: Exception) {
            when (e) {
                is IOException,
                is InterruptedException,
                -> false
                else -> throw e
            }
        }
    }
}