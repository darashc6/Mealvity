package cenec.mealvity.mealvityforowners.core.order.adapter

import cenec.mealvity.mealvityforowners.core.order.Order

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