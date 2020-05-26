package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.menu.Menu

class MenuRecyclerViewAdapter(private var restaurantMenu: Menu): RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuViewHolder>() {
    private lateinit var rvListener: MenuRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_section, parent, false))
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(restaurantMenu, position, rvListener)
    }

    override fun getItemCount(): Int {
        return restaurantMenu.menu.size
    }

    fun setMenuRecyclerViewListener(listener: MenuRecyclerViewListener) {
        rvListener = listener
    }

    class MenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val sectionName = itemView.findViewById<TextView>(R.id.text_view_section_name)
        private val rvListItems = itemView.findViewById<RecyclerView>(R.id.recycler_view_list_items)

        fun bind(restaurantMenu: Menu, position: Int, listener: MenuRecyclerViewListener) {
            sectionName.text = restaurantMenu.menu[position].name
            setupRecyclerView(restaurantMenu, position, listener)

            itemView.setOnClickListener {
                restaurantMenu.menu[position].isExpanded = !restaurantMenu.menu[position].isExpanded
                if (restaurantMenu.menu[position].isExpanded) {
                    rvListItems.visibility = View.VISIBLE
                } else {
                    rvListItems.visibility = View.GONE
                }
            }
        }

        private fun setupRecyclerView(restaurantMenu: Menu, position: Int, listener: MenuRecyclerViewListener) {
            val rvLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            val rvAdapter = SectionRecyclerViewAdapter(restaurantMenu.menu[position].items)

            rvListItems.layoutManager = rvLayoutManager
            rvListItems.adapter = rvAdapter
            rvAdapter.setSectionRecyclerViewListener(object: SectionRecyclerViewAdapter.SectionRecyclerViewListener{
                override fun onItemClick(itemPosition: Int) {
                    listener.onMenuItemClick(position, itemPosition)
                }

            })
        }
    }

    interface MenuRecyclerViewListener {
        fun onMenuItemClick(menuPosition: Int, itemPosition: Int)
    }
}