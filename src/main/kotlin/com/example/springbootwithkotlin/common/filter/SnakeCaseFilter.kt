package com.example.springbootwithkotlin.common.filter

import com.fleshgrinder.extensions.kotlin.toLowerCamelCase
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.util.ContentCachingRequestWrapper
import java.util.Collections
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SnakeCaseFilter(
    val localeResolver: LocaleResolver,
) : OncePerRequestFilter() {
    companion object : KLogging()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        localeResolver.setLocale(request, response, null)
        filterChain.doFilter(contentCachingRequest(request), response)
    }

    override fun destroy() {
        logger.debug("================== SNAKE CASE FILTER : destroy ==================")
    }

    private fun contentCachingRequest(request: HttpServletRequest): ContentCachingRequestWrapper {
        val parameters = mutableMapOf<String, Array<String>>()

        request.parameterMap.keys.forEach {
            parameters[it.toLowerCamelCase()] = request.getParameterValues(it)
            logger.debug(
                "snakeCase filter result : $it -> ${it.toLowerCamelCase()} : ${
                parameters[it.toLowerCamelCase()]?.joinToString(
                    ","
                )
                }"
            )
        }

        return object : ContentCachingRequestWrapper(request) {
            override fun getParameter(name: String) = if (parameters.containsKey(name)) parameters[name]!![0] else ""

            override fun getParameterNames() = Collections.enumeration(parameters.keys)

            override fun getParameterValues(name: String) =
                if (parameters.containsKey(name)) parameters[name]!! else arrayOf()

            override fun getParameterMap() = parameters
        }
    }
}
