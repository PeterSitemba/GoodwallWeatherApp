package faba.app.goodwallweatherapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.DateUtil
import kotlinx.android.synthetic.main.list_day_forecast.view.*
import kotlin.math.roundToInt


class DayForecastAdapter(
    private val onClick: (ForecastDays) -> Unit
) : ListAdapter<ForecastDays, DayForecastAdapter.ViewHolderDayForecast>(DayForecastDiffCallback) {

    private var context: Context? = null

    class ViewHolderDayForecast(itemView: View, val onClick: (ForecastDays) -> Unit) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDayForecast {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_day_forecast, parent, false)
        context = parent.context
        return ViewHolderDayForecast(view, onClick)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderDayForecast, position: Int) {
        when (getItem(position).weather[0].main) {
            "Clear" -> {
                holder.itemView.ivTimeTemp.setImageResource(R.drawable.ic_sunny_bg)
            }
            "Clouds" -> {
                holder.itemView.ivTimeTemp.setImageResource(R.drawable.ic_cloudy_bg)
            }
            "Rain" -> {
                holder.itemView.ivTimeTemp.setImageResource(R.drawable.ic_rainy_bg)
            }
            "Snow" -> {
                holder.itemView.ivTimeTemp.setImageResource(R.drawable.ic_snowy_bg)
            }
        }

        holder.itemView.txtTimeOfDay.text = DateUtil.getTime(getItem(position).dt_txt)
        holder.itemView.txtTempAtTime.text =
            "${getItem(position).main.temp.roundToInt()}${context?.resources?.getString(R.string.degree)}"

        holder.bind(getItem(position))
    }

}

object DayForecastDiffCallback : DiffUtil.ItemCallback<ForecastDays>() {
    override fun areItemsTheSame(oldItem: ForecastDays, newItem: ForecastDays): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ForecastDays, newItem: ForecastDays): Boolean {
        return oldItem.dt == newItem.dt
    }
}