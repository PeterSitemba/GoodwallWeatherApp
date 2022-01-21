package faba.app.goodwallweatherapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.DateUtil
import kotlinx.android.synthetic.main.host_frag.*
import kotlinx.android.synthetic.main.list_forecast.view.*
import kotlin.math.roundToInt

class ForecastAdapter(
    context: Context,
    private val dataSource: MutableList<ForecastDays>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.list_forecast, parent, false)
        return ViewHolderForecast(view)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class ViewHolderForecast(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (dataSource[position].weather[0].main) {
            "Clear" -> {
                holder.itemView.ivWeatherIcon.setImageResource(R.drawable.clear)
            }
            "Clouds" -> {
                holder.itemView.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
            }
            "Rain" -> {
                holder.itemView.ivWeatherIcon.setImageResource(R.drawable.ic_rainy)
            }
            "Snow" -> {
                holder.itemView.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
            }
        }


        holder.itemView.txtDay.text = DateUtil.getDay(dataSource[position].dt_txt)
        holder.itemView.txtTempForecast.text = "${dataSource[position].main.temp.roundToInt()}\u00B0"

    }

}