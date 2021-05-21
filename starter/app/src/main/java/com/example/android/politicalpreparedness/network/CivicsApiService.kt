package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.dtos.ElectionResponseDTO
import com.example.android.politicalpreparedness.network.dtos.RepresentativeResponseDTO
import com.example.android.politicalpreparedness.network.dtos.VoterInfoResponseDTO
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.RepresentativeAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

private val moshi = Moshi.Builder()
        .add(ElectionAdapter())
        .add(RepresentativeAdapter())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

interface CivicsApiService {
    @GET("elections")
    suspend fun getElections(): ElectionResponseDTO

    @GET("voterinfo")
    suspend fun getVoterInfo(
            @Query("electionId") electionId: Int,
            @Query("address") address: String
    ): VoterInfoResponseDTO

    @GET("representatives")
    suspend fun getRepresentativeInfoByAddress(
            @Query("address") address: String): RepresentativeResponseDTO
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}