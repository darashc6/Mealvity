package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.Open

class OpeningHoursRecyclerViewAdapter(private val listOpeningHours: List<Open>): RecyclerView.Adapter<OpeningHoursRecyclerViewAdapter.OpeningHoursViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpeningHoursViewHolder {
        return OpeningHoursViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_hour, parent, false))
    }

    override fun onBindViewHolder(holder: OpeningHoursViewHolder, position: Int) {
        holder.bind(listOpeningHours[position])
    }

    override fun getItemCount(): Int {
        return listOpeningHours.size
    }

    class OpeningHoursViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvDay = itemView.findViewById<TextView>(R.id.text_view_day)
        private val tvHours = itemView.findViewById<TextView>(R.id.text_view_hours)

        fun bind(hours: Open) {
            tvDay.text = hours.formatDay()
            tvHours.text = hours.formatHours()
        }
    }
}