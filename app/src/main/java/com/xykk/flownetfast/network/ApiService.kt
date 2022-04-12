package com.xykk.flownetfast.network

import com.xykk.flownetfast.model.UsuallyWebSites
import retrofit2.http.*

/**
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "https://www.wanandroid.com/"
    }

    /**
     * 常用网站
     */
    @GET("friend/json")
    suspend fun website(): ApiResponse<UsuallyWebSites>
}