package cenec.mealvity.mealvity.classes.singleton

import cenec.mealvity.mealvity.classes.user.Order

class OrderSingleton {
    companion object {
        private var mInstance: OrderSingleton? = null
        private var mOrder = Order()

        fun getInstance(): OrderSingleton {
            if (mInstance == null) {
                mInstance = OrderSingleton()
            }

            return mInstance!!
        }
    }

    fun setOrder(newOrder: Order) {
        mOrder = newOrder
    }

    fun getOrder(): Order {
        return mOrder
    }
}