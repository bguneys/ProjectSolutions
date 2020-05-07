package com.bguneys.app652020.info

import com.bguneys.app652020.info.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://everyday-solutions.firebaseio.com"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


//Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi object
private val retrofit = Retrofit.Builder()
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


//A public interface that exposes the method
interface RetrofitService {
    @GET(".json")
    fun getUsers() : Observable<List<User>>
}

//A public Api object that exposes the lazy-initialized Retrofit service
object RetrofitObject {
    val retrofitService : RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}
