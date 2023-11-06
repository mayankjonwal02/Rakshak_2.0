package com.example.rakshak20.android.API

import okhttp3.ResponseBody
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
    @POST("medical-info-api.php")
    suspend fun login_medical(@Body doctorData: DoctorData): Response<connectionResponseData>

//
        @Headers("Content-Type: application/json")
        @POST("medical-data-api.php")
        suspend fun sharedata(@Body medicaldata: medicaldata): ResponseBody

    //    @FormUrlEncoded
    @GET("data-retrieval-api.php")
    suspend fun getdata(@Query("patient_id") patientid: String) :Response<List<medicaldata>>

    @GET("this.php")
    suspend fun connect() : Response<connectionResponseData>
    @Headers("Content-Type: application/json")
    @POST("patients-list-api.php")
    suspend fun checkPatientId(@Body patient: patient) : Response<connectionResponseData>

    @FormUrlEncoded
    @POST("create.php")
    suspend fun senddata(@Field("patientid") patientid: String,
                         @Field("timestamp") timestamp: String,
                         @Field("heartrate") heartrate: Float,
                         @Field("ecg") ecg: Float,
                         @Field("spo2") spo2: Float,
                         @Field("temperature") temperature: Float,

                         ) : ResponseBody

}


data class LoginRequest(val patientid: String, val password: String)

data class  DoctorData(val Medical_id : String ,  val Password : String)
data class connectionResponseData(
    val status: String,
    val message : String
)

data class patient(val patientid : String)

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
    var patientid: String,
    var spo2:Float,
    var heartrate:Float,
    var ecg : Float,
    var temperature : Float,
    var timestamp : String,
    var Medical_Id : String
)
