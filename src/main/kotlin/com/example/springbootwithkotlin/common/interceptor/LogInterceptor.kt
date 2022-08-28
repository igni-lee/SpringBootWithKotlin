package com.example.springbootwithkotlin.common.interceptor

import mu.KLogging
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogInterceptor: HandlerInterceptor {
    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute("requestTime", System.currentTimeMillis())
        MDC.put("requestUuid", UUID.randomUUID().toString()) // MDC객체에 UUID를 넣어 request마다 할당해줌.

        val xForwardedFor = request.getHeader("X-Forwared-For")
        val remoteIp = xForwardedFor?.split(",")?.first() ?: request.remoteAddr
        request.setAttribute("remoteIp", remoteIp)

        val requestHeaderMap = makeRequestHeaderMap(request)
        val parameterMap = makeParameterMap(request)
        val requestBodyMap = makeRequestBodyMap(request)
        logger.info("request user IP: $remoteIp action: ${request.requestURI} / header : $requestHeaderMap / parameter : $parameterMap / body : $requestBodyMap")

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        val duration = System.currentTimeMillis() - request.getAttribute("requestTime") as Long
        if (request.javaClass.name.contains("SecurityContextHolderAwareRequestWrapper")) return

        val contentCachingResponseWrapper = ContentCachingResponseWrapper(response)
        val responseHeaderMap = makeResponseHeaderMap(contentCachingResponseWrapper)
        val responseBody = String(contentCachingResponseWrapper.contentAsByteArray)

        logger.info("HTTP Response code : ${contentCachingResponseWrapper.status} / Response Headers : $responseHeaderMap / response body : $responseBody / duration : $duration" )
    }

    private fun makeRequestHeaderMap(request: HttpServletRequest): MutableMap<String, String> {
        val requestHeaderMap = mutableMapOf<String, String>()
        request.headerNames
            .toList()
            .map { headerName -> requestHeaderMap.put(headerName, request.getHeader(headerName)) }

        return requestHeaderMap
    }

    private fun makeParameterMap(request: HttpServletRequest): MutableMap<String, String> {
        val parameterMap = mutableMapOf<String, String>()
        request.queryString?.split("&")
            ?.map { pairs ->
                val splitPair = pairs.split("=")
                parameterMap[splitPair[0]] = if (splitPair.size == 2) {
                    splitPair[1]
                } else {
                    ""
                }
            }

        return parameterMap
    }

    private fun makeRequestBodyMap(request: HttpServletRequest): MutableMap<String, String> {
        val requestBodyMap = mutableMapOf<String, String>()
        request.parameterMap
            .keys
            .toList()
            .associate {
                if (request.parameterMap[it]!!.size > 1) {
                    it to requestBodyMap.put(it, request.parameterMap[it].toString())
                } else {
                    it to requestBodyMap.put(it, request.parameterMap[it]?.get(0).toString())
                }
            }

        return requestBodyMap
    }

    private fun makeResponseHeaderMap(response: ContentCachingResponseWrapper): MutableMap<String, String> {
        val responseHeaderMap = mutableMapOf<String, String>()
        response.headerNames
            .toList()
            .map { headerName -> responseHeaderMap.put(headerName, response.getHeader(headerName)) }

        return responseHeaderMap
    }
}