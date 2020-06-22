package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import cenec.mealvity.mealvity.classes.singleton.RestaurantMoreInfoSingleton
import cenec.mealvity.mealvity.classes.utils.CategoryUtil
import java.util.*


class FragmentOrder : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private var restaurantMenu = Menu(arrayListOf())
    private val restaurantMoreInfo by lazy { RestaurantMoreInfoSingleton.getInstance().getRestaurantMoreInfo() }
    private lateinit var newOrderCart: OrderCart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        setupNewOrder()
        setupRestaurantMenuList()
        setupOrderCart()
        setupRecyclerView()
        return binding.root
    }

    private fun setupNewOrder() {
        newOrderCart = OrderCart()
        newOrderCart.setOrderListener(object : OrderListener {
            override fun onOrderCartUpdated() {
                binding.cardViewOrderCart.textViewNumberItems.text = "${newOrderCart.quantityTotal} items"
                binding.cardViewOrderCart.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", newOrderCart.totalPrice)
            }

        })
    }

    private fun setupRestaurantMenuList() {
        val categoryName = restaurantMoreInfo!!.categories[0].title
        val listStarters = CategoryUtil.generateStarters(categoryName)
        val listMainCourse = CategoryUtil.generateMainCourse(categoryName)

        if (listStarters.isNotEmpty() && listMainCourse.isNotEmpty()) {
            var sectionNameStarters = ""
            var sectionNameMainCourse = ""
            if (categoryName == "Bakeries" || categoryName == "Bistro") {
                sectionNameStarters = "Cupcakes"
                sectionNameMainCourse = "Sandwiches"
            } else {
                sectionNameStarters = "Starters"
                sectionNameMainCourse = "Main Course"
            }
            restaurantMenu.menu.add(Section(sectionNameStarters, listStarters, false))
            restaurantMenu.menu.add(Section(sectionNameMainCourse, listMainCourse, false))
            restaurantMenu.menu.add(Section("Desserts", CategoryUtil.generateDesserts(), false))
            restaurantMenu.menu.add(Section("Drinks", CategoryUtil.generateDrinks(), false))
            restaurantMenu.menu.add(Section("Extras", CategoryUtil.generateExtras(), false))

            binding.layoutOrderCart.visibility = View.VISIBLE
        } else {
            binding.textViewOrderNotAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupOrderCart() {
        val cvOrderCart = binding.root.findViewById<View>(R.id.card_view_order_cart)

        cvOrderCart.setOnClickListener {
            val orderCartBottomSheet = OrderCartBottomSheet(context!!, newOrderCart)
            orderCartBottomSheet.setOrderCartBottomSheetListener(object : OrderCartBottomSheet.OrderCartBottomSheetListener{
                override fun onConfirmOrderClick() {
                    if (newOrderCart.orderCart.isNotEmpty()) {
                        val intent = Intent(context!!, OrderPaymentActivity::class.java)
                        intent.putExtra("order", newOrderCart)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show()
                    }
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

        binding.recyclerViewMenu.layoutManager = rvLayoutManager
        binding.recyclerViewMenu.adapter = rvAdapter
    }

}
