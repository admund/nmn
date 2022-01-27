package me.admund.nmn.di

import com.google.gson.GsonBuilder
import me.admund.nmn.data.api.VaccinesApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//private const val BASE_URL = "https://covid-api.mmediagroup.fr/v1/"
//
//val retrofitModule = module {
//    single<VaccinesApi> {
//        val okHttp = OkHttpClient.Builder().build()
//        val gsonConverter = GsonConverterFactory.create(GsonBuilder().create())
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(gsonConverter)
//            .client(okHttp)
//            .build()
//
//        retrofit.create(VaccinesApi::class.java)
//    }
//}
