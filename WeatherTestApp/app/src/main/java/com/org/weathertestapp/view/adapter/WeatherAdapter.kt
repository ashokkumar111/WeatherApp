package com.org.weathertestapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.weathertestapp.R
import com.org.weathertestapp.model.Weather
import com.org.weathertestapp.model.WeatherParentModel
import com.org.weathertestapp.utils.Constant
import com.org.weathertestapp.utils.MyTimeStamp

class WeatherAdapter(
    private val context: Context,
    private val list: List<Weather>,
    private val weatherParentModel: WeatherParentModel
   ) : RecyclerView.Adapter<WeatherAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherIV: ImageView = itemView.findViewById(R.id.weatherIV)
        val nameTV: TextView = itemView.findViewById(R.id.nameTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        val cityNameTV: TextView = itemView.findViewById(R.id.cityNameTV)
        val cityCodeTV: TextView = itemView.findViewById(R.id.cityCodeTV)
        val tempTV: TextView = itemView.findViewById(R.id.tempTV)
        val pressureTV: TextView = itemView.findViewById(R.id.pressureTV)
        val humidityTV: TextView = itemView.findViewById(R.id.humidityTV)
        val sunriseTV: TextView = itemView.findViewById(R.id.sunriseTV)
        val sunsetTV: TextView = itemView.findViewById(R.id.sunsetTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_adapter_row, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val weather: Weather = list[position]
        holder.nameTV.text = weather.main
        holder.descriptionTV.text = weather.description
        holder.cityNameTV.text = weatherParentModel.name
        holder.cityCodeTV.text = weatherParentModel.cod.toString()

        holder.tempTV.text = "Temp:-" + weatherParentModel.main.temp
        holder.pressureTV.text = "Pressure:-" + weatherParentModel.main.pressure
        holder.humidityTV.text = "Humidity:-" + weatherParentModel.main.humidity
        holder.sunriseTV.text =
            "Sun rise:-" + MyTimeStamp.epochToTime(weatherParentModel.sys.sunrise.toLong())
        holder.sunsetTV.text =
            "Sun set:-" + MyTimeStamp.epochToTime(weatherParentModel.sys.sunset.toLong())

        if (weather.icon != null) {
            Glide.with(context)
                .load(Constant.IMAGE_URL + weather.icon + "@2x.png")
                .placeholder(R.drawable.weather)
                .centerCrop()
                .into(holder.weatherIV)
        } else {
            Glide.with(context)
                .load(R.drawable.weather)
                .placeholder(R.drawable.weather)
                .centerCrop()
                .into(holder.weatherIV)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}