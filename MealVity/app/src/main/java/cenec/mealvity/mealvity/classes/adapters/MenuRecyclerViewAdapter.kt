package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.menu.Menu

/**
 * RecyclerView adapter binding a restaurant menu
 * @param restaurantMenu menu of a restaurant
 */
class MenuRecyclerViewAdapter(private var restaurantMenu: Menu): RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuViewHolder>() {
    private lateinit var rvListener: MenuRecyclerViewListener // Listener for the RecyclerView adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_section, parent, false))
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(restaurantMenu, position)
    }

    override fun getItemCount(): Int {
        return restaurantMenu.menu.size
    }

    /**
     * Sets the RecyclerView listener
     * @param listener New listener of the RecyclerView
     */
    fun setMenuRecyclerViewListener(listener: MenuRecyclerViewListener) {
        rvListener = listener
    }

    inner class MenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val sectionName = itemView.findViewById<TextView>(R.id.text_view_section_name)
        private val rvListItems = itemView.findViewById<RecyclerView>(R.id.recycler_view_list_items)

        /**
         * Binds the menu to the itemview
         * @param restaurantMenu menu of the restaurant
         * @param position Item's position
         */
        fun bind(restaurantMenu: Menu, position: Int) {
            sectionName.text = restaurantMenu.menu[position].name
            setupRecyclerView(restaurantMenu, position)

            itemView.setOnClickListener {
                restaurantMenu.menu[position].isExpanded = !restaurantMenu.menu[position].isExpanded
                if (restaurantMenu.menu[position].isExpanded) {
                    rvListItems.visibility = View.VISIBLE
                } else {
                    rvListItems.visibility = View.GONE
                }
            }
        }

        /**
         * Sets up the RecyclerView containing a list of items from each section of the menu
         * @param restaurantMenu Menu of the restaurant
         * @param position Item's position
         */
        private fun setupRecyclerView(restaurantMenu: Menu, position: Int) {
            val rvLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            val rvAdapter = SectionRecyclerViewAdapter(restaurantMenu.menu[position].items)

            rvListItems.layoutManager = rvLayoutManager
            rvListItems.adapter = rvAdapter
            rvAdapter.setSectionRecyclerViewListener(object: SectionRecyclerViewAdapter.SectionRecyclerViewListener{
                override fun onItemClick(itemPosition: Int) {
                    rvListener.onMenuItemClick(position, itemPosition)
                }

            })
        }
    }

    /**
     * Interface of the RecyclerView
     */
    interface MenuRecyclerViewListener {
        /**
         * Executed when an item is clicked
         * @param menuPosition Position of the menu (Section of the menu)
         * @param itemPosition Position of the item
         */
        fun onMenuItemClick(menuPosition: Int, itemPosition: Int)
    }
}