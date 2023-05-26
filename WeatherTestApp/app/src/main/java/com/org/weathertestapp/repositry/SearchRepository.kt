package com.org.weathertestapp.repositry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.org.weathertestapp.model.WeatherParentModel
import com.org.weathertestapp.retrofit.WeatherApi
import com.org.weathertestapp.utils.Constant
import com.org.weathertestapp.utils.SharedPref
import javax.inject.Inject


class SearchRepository @Inject constructor(private val weatherApi: WeatherApi) {

    private val _weatherData= MutableLiveData<WeatherParentModel>()
    val weatherData:LiveData<WeatherParentModel> get() = _weatherData


    suspend  fun getWeatherData(cityName:String,appId:String){
      val result =weatherApi.getWeatherReport1(cityName,appId)
        if (result.isSuccessful&&result.body()!=null){
            _weatherData.postValue(result.body())
            SharedPref.getPrefsHelper().savePref(Constant.CITY_NAME,cityName)
        }
    }

    suspend fun getWeatherDataFromLatLng(lat:String,lng:String,appId:String){
        val result =weatherApi.getWeatherReportFromLatLng(lat,lng,appId)
        if (result.isSuccessful&&result.body()!=null){
            _weatherData.postValue(result.body())
        }
    }
}