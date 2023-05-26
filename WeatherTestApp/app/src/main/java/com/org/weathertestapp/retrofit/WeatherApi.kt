package com.org.weathertestapp.retrofit
import com.org.weathertestapp.model.ProductModel
import com.org.weathertestapp.model.WeatherParentModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getProduct(): Response<List<ProductModel>>

    @GET("weather")
    suspend fun getWeatherReport1(
        @Query("q") city: String,
        @Query("appid") apikey: String): Response<WeatherParentModel>

    @GET("weather")
    suspend fun getWeatherReportFromLatLng(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") apikey: String): Response<WeatherParentModel>
}