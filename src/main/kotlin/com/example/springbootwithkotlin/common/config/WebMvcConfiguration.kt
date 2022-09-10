package com.example.springbootwithkotlin.common.config

import com.example.springbootwithkotlin.common.interceptor.LogInterceptor
import dev.akkinoc.util.YamlResourceBundle
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class WebMvcConfiguration(
    private val logInterceptor: LogInterceptor,
): WebMvcConfigurer {
    @Bean
    fun localeResolver() = SessionLocaleResolver().apply {
        setDefaultLocale(Locale.KOREAN)
    }

    fun localeChangeInterceptor() = LocaleChangeInterceptor().apply {
        paramName = "lang"
    }

    @Bean
    fun messageSource(
        @Value("\${spring.messages.basename}") basename: String,
        @Value("\${spring.messages.encoding}") encoding: String,
    ) = YamlMessageSource().apply {
        setBasename(basename)
        setDefaultEncoding(encoding)
        setFallbackToSystemLocale(true)
    }


    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(logInterceptor)
        registry.addInterceptor(localeChangeInterceptor())
    }

    class YamlMessageSource : ResourceBundleMessageSource() {
        override fun doGetBundle(basename: String, locale: Locale): ResourceBundle =
            ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control)
    }
}