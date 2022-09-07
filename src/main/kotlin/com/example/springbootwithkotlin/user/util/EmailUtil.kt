package com.example.springbootwithkotlin.user.util

import java.util.*
import javax.naming.directory.InitialDirContext

class EmailUtil {
    companion object {
        fun isValidDomain(email: String): Boolean {
            val domain = getDomain(email)
            val env: Hashtable<String, String> = Hashtable<String, String>()
            env["java.naming.factory.initial"] = "com.sun.jndi.dns.DnsContextFactory"
            return runCatching {
                InitialDirContext(env).getAttributes(domain, arrayOf("MX"))
            }.isSuccess
        }

        private fun getDomain(email: String): String = email.split("@").last()
    }
}