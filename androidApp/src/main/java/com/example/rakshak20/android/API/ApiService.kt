package com.example.rakshak20.android.API

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

//    @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("login-api.php")
    suspend fun login(@Body loginRequest: LoginRequest): Response<connectionResponseData>

    @Headers("Content-Type: application/json")
    @POST("registration-final.php")
    suspend fun register(@Body registrationdata: Registrationdata): Response<connectionResponseData>

    @Headers("Content-Type: application/json")
    @POST("medical-data-api.php")
    suspend fun sharedata(@Body medicaldata: medicaldata):Response<connectionResponseData>

//    @FormUrlEncoded
    @GET("data-retrieval-api.php")
    suspend fun getdata(@Query("patient_id") patientid: String) :Response<List<medicaldata>>

    @GET("this.php")
    suspend fun connect() : Response<connectionResponseData>

}


data class LoginRequest(val patientid: String, val password: String)
data class connectionResponseData(
    val status: String,
    val message : String
)

data class Registrationdata(
    var patientid : String,
    var name : String,
    var age: Int,
    var gender : String,
    var height : Float,
    var weight : Float,
    var password : String
)

data class medicaldata(
    var patientid: String?,
    var spo2:Float?,
    var heartrate:Float?,
    var ecg : Float?,
    var temperature : Float?,
    var timestamp : String?
)
