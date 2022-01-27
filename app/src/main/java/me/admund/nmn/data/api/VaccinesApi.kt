package me.admund.nmn.data.api

import retrofit2.http.GET

interface VaccinesApi {

    @GET("vaccines")
    suspend fun vaccines(): Map<String, Map<String, VaccineApiEntity>>

    companion object {
        const val BASE_URL = "https://covid-api.mmediagroup.fr/v1/"
    }
}
