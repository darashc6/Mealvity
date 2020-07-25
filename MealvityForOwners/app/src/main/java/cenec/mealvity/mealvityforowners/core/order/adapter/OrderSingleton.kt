package cenec.mealvity.mealvityforowners.core.order.adapter

import cenec.mealvity.mealvityforowners.core.order.Order

/**
 * Static class used for saving an order
 * Used for communicating between Activities
 */
class OrderSingleton {
    companion object {
        private var mInstance: OrderSingleton? = null // Instance of the singleton
        private var mOrder = Order() // Instance of Order

        /**
         * Returns an instance of the singleton
         * @return Instance of the same class
         */
        fun getInstance(): OrderSingleton {
            if (mInstance == null) {
                mInstance = OrderSingleton()
            }

            return mInstance!!
        }
    }

    /**
     * Sets a new order for the singleton class
     * @param newOrder New Order
     */
    fun setOrder(newOrder: Order) {
        mOrder = newOrder
    }

    /**
     * Returns the order currently saved in the singleton
     * @return Order
     */
    fun getOrder(): Order {
        return mOrder
    }
}