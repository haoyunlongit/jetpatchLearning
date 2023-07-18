package com.example.jetpatchlearning.data.repository.api

import com.example.jetpatchlearning.data.login_register.LoginRegisterResponse
import com.example.jetpatchlearning.data.login_register.LoginRegisterResponseWrapper
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WanAndroidApi {


    @POST("/user/login")
    @FormUrlEncoded
    fun loginAction(@Field("username") username: String,
                    @Field("password") password: String)
    : Observable<LoginRegisterResponseWrapper<LoginRegisterResponse>>

    @POST("/user/register")
    @FormUrlEncoded
    fun registerAction(@Field("username") username: String,
                       @Field("password") password: String,
                       @Field("repassword") repassword: String)
            : Observable<LoginRegisterResponseWrapper<LoginRegisterResponse>>


    // TODO >>>>>>>只有协程了 下面是协程API

    /** https://www.wanandroid.com/blog/show/2
     * 登录API
     * username=Derry-vip&password=123456
     * username=Derry888&password=123456
     */
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun loginActionCoroutine(@Field("username") username: String,
                                     @Field("password") password: String)
            : LoginRegisterResponseWrapper<LoginRegisterResponse> // 返回值

    /** https://www.wanandroid.com/blog/show/2
     * 注册的API
     */
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun registerActionCoroutine(@Field("username") username: String,
                                        @Field("password") password: String,
                                        @Field("repassword") repassword: String)
            : LoginRegisterResponseWrapper<LoginRegisterResponse> // 返回值
}