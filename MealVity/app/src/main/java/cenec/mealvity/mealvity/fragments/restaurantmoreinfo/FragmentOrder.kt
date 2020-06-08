package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentOrderBinding
import cenec.mealvity.mealvity.activities.OrderPaymentActivity
import cenec.mealvity.mealvity.activities.RestaurantMoreInfoActivity
import cenec.mealvity.mealvity.classes.adapters.MenuRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.bottomsheet.AddItemToCartBottomSheet
import cenec.mealvity.mealvity.classes.bottomsheet.OrderCartBottomSheet
import cenec.mealvity.mealvity.classes.orders.OrderCart
import cenec.mealvity.mealvity.classes.orders.OrderListener
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import cenec.mealvity.mealvity.classes.restaurant.menu.Menu
import cenec.mealvity.mealvity.classes.restaurant.menu.Section
import java.util.*


class FragmentOrder : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding
    private var restaurantMenu = Menu(arrayListOf())
    private lateinit var newOrderCart: OrderCart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        setupNewOrder()
        setupFakeList()
        setupOrderCart()
        setupRecyclerView()
        return binding!!.root
    }

    private fun setupNewOrder() {
        newOrderCart = OrderCart()
        newOrderCart.setOrderListener(object : OrderListener {
            override fun onOrderCartUpdated() {
                binding!!.cardViewOrderCart.textViewNumberItems.text = "${newOrderCart.quantityTotal} items"
                binding!!.cardViewOrderCart.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", newOrderCart.totalPrice)
            }

        })
    }

    private fun setupFakeList() {
        for (i in 1..15) {
            restaurantMenu.menu.add(Section("Starters", arrayListOf(
                Item("Item 1", "Description 1", 8.50f),
                Item("Item 2", "Description 2", 8.50f),
                Item("Item 3", "Description 3", 8.50f),
                Item("Item 4", "Description 4", 8.50f),
                Item("Item 5", "Description 5", 8.50f)
            ), false))
        }
    }

    private fun setupOrderCart() {
        val cvOrderCart = binding!!.root.findViewById<View>(R.id.card_view_order_cart)

        cvOrderCart.setOnClickListener {
            val orderCartBottomSheet = OrderCartBottomSheet(context!!, newOrderCart)
            orderCartBottomSheet.setOrderCartBottomSheetListener(object : OrderCartBottomSheet.OrderCartBottomSheetListener{
                override fun onConfirmOrderClick() {
                    val intent = Intent(context!!, OrderPaymentActivity::class.java)
                    intent.putExtra("order", newOrderCart)
                    intent.putExtra("restaurant", (context as RestaurantMoreInfoActivity).getRestaurantMoreInfo()!!.name)
                    startActivity(intent)
                }

            })
            orderCartBottomSheet.show(childFragmentManager, orderCartBottomSheet.tag)
        }
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = MenuRecyclerViewAdapter(restaurantMenu)
        rvAdapter.setMenuRecyclerViewListener(object : MenuRecyclerViewAdapter.MenuRecyclerViewListener{
            override fun onMenuItemClick(menuPosition: Int, itemPosition: Int) {
                val addItemToCartBottomSheet = AddItemToCartBottomSheet(restaurantMenu.menu[menuPosition].items[itemPosition])
                addItemToCartBottomSheet.setAddItemToBottomSheetListener(object : AddItemToCartBottomSheet.AddItemToCartBottomSheetListener{
                    override fun onAddItemToCart(menuItem: Item, quantity: Int) {
                        newOrderCart.addItemToOrder(menuItem, quantity)
                    }

                })
                addItemToCartBottomSheet.show(childFragmentManager, addItemToCartBottomSheet.tag)
            }
        })

        binding!!.recyclerViewMenu.layoutManager = rvLayoutManager
        binding!!.recyclerViewMenu.adapter = rvAdapter
    }

}
