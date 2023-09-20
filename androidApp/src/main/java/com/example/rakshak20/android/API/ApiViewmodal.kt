package com.example.rakshak20.android.API

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import co.yml.charts.common.extensions.isNotNull
import com.example.rakshak20.android.functions.getSharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ApiViewmodel(context: Context) : ViewModel()
{

    lateinit var apiService : ApiService
    var sp = getSharedPreferences(context)
    var ipaddress = sp?.getString("ipaddress","0.0.0.0")
    private val trustAllManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    private val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, arrayOf<TrustManager>(trustAllManager), null)
    }

    init {
        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipaddress/SMJ/")
            .client(

                    OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.socketFactory,trustAllManager)
                        .hostnameVerifier { _, _ -> true }
                        .build()
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }


    suspend fun loginByAPI(loginRequest: LoginRequest): String {
        try {
//            val loginRequest = LoginRequest(patientid = "example_patient_id", password = "example_password")

            var responce = apiService.login(loginRequest).body()?.status
            Log.e("TAG1",responce.toString())
            return  responce.toString()
        }
        catch (e:Exception)
        {
            return e.message.toString()
            Log.e("TAG1",e.message.toString())
        }
    }

    suspend fun getconnection(): String{
        try {
            var responce = apiService.connect()
            return responce.body().toString()
        }
        catch (e:IOException)
        {
            return e.message.toString()
        }
    }

    suspend fun registerByAPI(registrationdata: Registrationdata): String {
        return try {
            val response = apiService.register(registrationdata)

            try {
                response.body()?.message.toString()
            } catch (e:Exception) {
                Log.e("TAG1",e.message.toString())
                e.message.toString()

            }
        } catch (e: IOException) {
            Log.e("TAG1",e.message.toString())
            "Network error: ${e.message}"

        }
    }


    suspend fun senddataByAPI(medicaldata: medicaldata): String {
        var responce = apiService.sharedata(medicaldata)

        try {
            if(responce.body().isNotNull()) {

                return responce.body()?.message.toString()
            }
            else
            {
                return "ERROR IN SENDING DATA"
            }
        }
        catch (e:IOException)
        {
            Log.e("TAG1",e.message.toString())
            return e.message.toString()
        }
    }

    suspend fun getdata(patientId: String): String {
        try{ var response = apiService.getdata(patientId)
        return response.body().toString()
        }
        catch (e:Exception)
        {
            return e.message.toString()
        }

    }

}

