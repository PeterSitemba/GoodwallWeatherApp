package faba.app.goodwallweatherapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.DateUtil
import kotlinx.android.synthetic.main.list_forecast.view.*
import kotlin.math.roundToInt

class ForecastAdapter(
    private val onClick: (ForecastDays) -> Unit
) : ListAdapter<ForecastDays, ForecastAdapter.ViewHolderForecast>(ForecastDiffCallback) {

    class ViewHolderForecast(itemView: View, val onClick: (ForecastDays) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var currentForecast: ForecastDays? = null

        init {
            itemView.setOnClickListener {
                currentForecast?.let {
                    onClick(it)
                }
            }
        }

        fun bind(forecastDays: ForecastDays) {
            currentForecast = forecastDays
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForecast {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_forecast, parent, false)
        return ViewHolderForecast(view, onClick)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderForecast, position: Int) {
        when (getItem(position).weather[0].main) {
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

        var day = DateUtil.getDay(getItem(position).dt_txt)
        if (day == DateUtil.getDayOfWeek()) {
            day = "Today"
        }
        holder.itemView.txtDay.text = day
        holder.itemView.txtTempForecast.text =
            "${getItem(position).main.temp.roundToInt()}\u00B0"

        holder.bind(getItem(position))

    }

}

object ForecastDiffCallback : DiffUtil.ItemCallback<ForecastDays>() {
    override fun areItemsTheSame(oldItem: ForecastDays, newItem: ForecastDays): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ForecastDays, newItem: ForecastDays): Boolean {
        return oldItem.dt == newItem.dt
    }
}