package com.org.weathertestapp.view.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.org.weathertestapp.R
import com.org.weathertestapp.databinding.ActivitySearchBinding
import com.org.weathertestapp.utils.CommonCallBack
import com.org.weathertestapp.utils.Constant
import com.org.weathertestapp.utils.PermissionUtils
import com.org.weathertestapp.utils.SharedPref
import com.org.weathertestapp.view.adapter.WeatherAdapter
import com.org.weathertestapp.view_model.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity2 : AppCompatActivity(), View.OnClickListener {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchTV.setOnClickListener(this)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        /**
         * Here get data using live data from viewModel and set to adapter
         */
        searchViewModel.weatherLiveData.observe(this, Observer { listData ->
            listData?.let {
                binding.progressBar.visibility = View.INVISIBLE
                binding.dataRecyclerView.layoutManager = LinearLayoutManager(this)
                val weatherAdapter = WeatherAdapter(this@SearchActivity2, it.weather, it)
                binding.dataRecyclerView.adapter = weatherAdapter
            }
        })
    }

    /**
     * Here click on search button API will call search by city name
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.searchTV -> {
                if (binding.citySearchET.text.isNotEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    searchViewModel.citySearchFun(
                        binding.citySearchET.text.toString().trim(),
                        Constant.App_ID
                    )
                    binding.citySearchET.setText("")
                } else {
                    binding.citySearchET.error = "Please Enter city name"
                    binding.citySearchET.isFocusable = true
                }
            }
        }
    }

    /**
     * Here give runtime permission to get location
     */
    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this, CommonCallBack {
                            /**
                             * When click on cancel button of GPS enable dialog so this API will be call
                             */
                            if (SharedPref.getPrefsHelper()
                                    .getPref<Boolean?>(Constant.CITY_NAME) != null
                            ) {
                                searchViewModel.citySearchFun(
                                    SharedPref.getPrefsHelper().getPref(Constant.CITY_NAME),
                                    Constant.App_ID
                                )
                            }
                        })
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this, LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Here get current  lat long and call API search by city name
     */
    var isLatLonFind = false

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 4 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(8000).setFastestInterval(8000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    if (isLatLonFind)
                        return
                    for (location in locationResult.locations) {
                        binding.progressBar.visibility = View.VISIBLE
                        isLatLonFind = true
                        searchViewModel.citySearchByLatLongFun(
                            location.latitude.toString(),
                            location.longitude.toString(),
                            Constant.App_ID
                        )
                    }

                }
            },
            Looper.myLooper()
        )
    }

    /**
     * Here get requested permission result
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this, CommonCallBack {

                            })
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}