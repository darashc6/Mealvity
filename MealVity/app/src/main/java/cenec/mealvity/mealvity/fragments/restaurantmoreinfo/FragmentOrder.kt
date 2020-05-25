package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentOrderBinding
import cenec.mealvity.mealvity.classes.adapters.MenuRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import cenec.mealvity.mealvity.classes.restaurant.menu.Menu
import cenec.mealvity.mealvity.classes.restaurant.menu.Section


class FragmentOrder : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding
    private var restaurantMenu = Menu(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        setupFakeList()
        setupRecyclerView()
        return binding!!.root
    }

    private fun setupFakeList() {
        for (i in 1..15) {
            restaurantMenu.menu.add(Section("Starters", arrayListOf(
                Item("Item 1", "Description 1", 8.50f),
                Item("Item 1", "Description 1", 8.50f),
                Item("Item 1", "Description 1", 8.50f),
                Item("Item 1", "Description 1", 8.50f),
                Item("Item 1", "Description 1", 8.50f)
            ), false))
        }
    }

    private fun setupRecyclerView() {
        val rvAdapter = MenuRecyclerViewAdapter(restaurantMenu)
        val rvLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

        binding!!.recyclerViewMenu.layoutManager = rvLayoutManager
        binding!!.recyclerViewMenu.adapter = rvAdapter
    }

}
