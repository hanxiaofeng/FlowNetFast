package com.xykk.flownetfast.network

import com.xykk.flownetfast.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val versionCode = BuildConfig.VERSION_CODE.toString()

        val builder = chain.request().newBuilder()
        builder.addHeader("appName", "flow_test").build()
        builder.addHeader("versionCode", versionCode).build()
        return chain.proceed(builder.build())
    }

}