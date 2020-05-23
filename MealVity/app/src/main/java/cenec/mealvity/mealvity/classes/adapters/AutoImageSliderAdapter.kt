package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cenec.darash.mealvity.R
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class AutoImageSliderAdapter(var list: List<String>): SliderViewAdapter<AutoImageSliderAdapter.AutoImageSliderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?): AutoImageSliderViewHolder {
        return AutoImageSliderViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_list_image, parent, false))
    }

    override fun onBindViewHolder(viewHolder: AutoImageSliderViewHolder, position: Int) {
        viewHolder.bind(list[position])
    }

    override fun getCount(): Int {
        return list.size
    }

    class AutoImageSliderViewHolder(itemView: View): SliderViewAdapter.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.slide_image)

        fun bind(image_url: String) {
            Glide.with(itemView)
                .load(image_url)
                .centerCrop()
                .into(image)
        }
    }
}