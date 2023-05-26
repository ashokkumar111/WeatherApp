package com.org.weathertestapp.view_model
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.weathertestapp.model.WeatherParentModel
import com.org.weathertestapp.repositry.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {
    val weatherLiveData: LiveData<WeatherParentModel> get() = searchRepository.weatherData


    fun citySearchFun(cityName: String, appId: String) {
        viewModelScope.launch {
            searchRepository.getWeatherData(cityName, appId)
        }
    }

    fun citySearchByLatLongFun(lat: String, long: String, appId: String) {
        viewModelScope.launch {
            searchRepository.getWeatherDataFromLatLng(lat, long, appId)
        }
    }

}