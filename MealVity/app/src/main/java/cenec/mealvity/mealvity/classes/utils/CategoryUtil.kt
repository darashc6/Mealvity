package cenec.mealvity.mealvity.classes.utils

import cenec.mealvity.mealvity.classes.restaurant.menu.Item

/**
 * Class acting as utility methods for generating categories
 */
class CategoryUtil {
    companion object {
        /**
         * Generates a list of starters depending on the category name
         * @param categoryName Category name
         * @return List of menu items
         */
        fun generateStarters(categoryName: String): ArrayList<Item> {
            val listStarters = arrayListOf<Item>()

            when (categoryName) {
                "Italian", "Pizza" -> {
                    listStarters.add(Item("Minestrone Soup", "Thick soup with beans, vegetables and cheese", 4f))
                    listStarters.add(Item("Sea-food Soup", "Soup with seafood vegetables", 4.90f))
                    listStarters.add(Item("Garlic Bread with Cheese", "Garlic bread with mozzarrella cheese", 2.80f))
                    listStarters.add(Item("Mix Salad", "Lettuce Leaves, Olives, Cheese, Onion, Sweet Corn, Tomato, Cucumber & Carrot", 5.90f))
                    listStarters.add(Item("Seafood Salad", "Italian Mix Salad with Prawns, Crab meat stick & Yogurt Sauce", 7.90f))
                }
                "Spanish", "Tapas Bar", "Seafood", "Arroceria / Paella", "Tapas/Small plates", "Mediterranean", "Bars" -> {
                    listStarters.add(Item("Ham croquettes", "Croquetas de jamón", 2.50f))
                    listStarters.add(Item("Chicken Skewers", "Pinchitos de pollo", 4f))
                    listStarters.add(Item("Russian salad", "Ensaladilla rusa", 3f))
                    listStarters.add(Item("Boiled potatoes", "Patatas bravas", 2f))
                    listStarters.add(Item("Garlic shrimps", "Gambas al ajillo", 7.90f))
                }
                "Chinese", "Japanese", "Sushi Bars" -> {
                    listStarters.add(Item("Chinese salad", "Fresh mixed green, deta cheese, tomatoes and black olives", 3.75f))
                    listStarters.add(Item("Spring rolls", "Crispy spring rolls, shrimp, pork and mix vegetable", 2f))
                    listStarters.add(Item("Prawn crackers", "Prawn crackers", 2.05f))
                    listStarters.add(Item("Wan-Tun soup", "Lean pork and shrimp dumpling gently boiled in clear chicken broth with lettuce, sallion and cilantro", 3.65f))
                    listStarters.add(Item("Sweet corn soup", "Creamy thick soup with sweet corn", 3.75f))
                }
                "Indian" -> {
                    listStarters.add(Item("Onion Bhaji", "Deep Fried Onion Rings with Gram Flour and Spices", 3.90f))
                    listStarters.add(Item("Patra", "Tender curried leaves with spices", 4.50f))
                    listStarters.add(Item("Prawn Puri", "Cooked in a Special Sauce and Served on Deep Fried Brown Bread (puri)", 5.90f))
                    listStarters.add(Item("Nan tandoori", "Plain Nan Bread", 2.75f))
                    listStarters.add(Item("Keema Rice", "Special Basmati Rice with Minced Meat", 3.75f))
                }
                "Bakeries", "Bistro" -> {
                    listStarters.add(Item("Chocolate peanut butter cup", "Chocolate peanut butter cup", 2.50f))
                    listStarters.add(Item("Key Lime", "Key Lime", 2.50f))
                    listStarters.add(Item("Boston Creme", "Boston Creme", 2.90f))
                    listStarters.add(Item("Pink Lemonade", "Pink Lemonade", 2.75f))
                    listStarters.add(Item("Carrot Cake", "Carrot Cake", 2.75f))
                }
            }

            return listStarters
        }

        /**
         * Generates a list of main course dishes depending on the category name
         * @param categoryName Category name
         * @return List of menu items
         */
        fun generateMainCourse(categoryName: String): ArrayList<Item> {
            val listMainCourse = arrayListOf<Item>()

            when (categoryName) {
                "Italian", "Pizza" -> {
                    listMainCourse.add(Item("Chicken Breast", "With Stroganoff or Pepper Sauce, incl. Chips", 8.90f))
                    listMainCourse.add(Item("Fish Fillets", "Coted with Bread Crums, incl. Chips", 7.90f))
                    listMainCourse.add(Item("Spaghetti Bolognesa", "Minced Meat in a Tomato Sauce", 7.95f))
                    listMainCourse.add(Item("Penne Arrabiata", "Garlic & Spicy Tomato Sauce", 6.95f))
                    listMainCourse.add(Item("Pizza Margherita", "Tomato, Mozzarela, Oregano", 6.25f))
                }
                "Spanish", "Tapas Bar", "Seafood", "Arroceria / Paella", "Tapas/Small plates", "Mediterranean", "Bars" -> {
                    listMainCourse.add(Item("Paella", "Ración de paella", 5.50f))
                    listMainCourse.add(Item("Potato omelettes", "Tortilla de patatas", 3.50f))
                    listMainCourse.add(Item("Chicken and tuna pie", "Empanada de pollo y atún", 6.75f))
                    listMainCourse.add(Item("Spanish spinach and chickpeas", "Garbanzos españolas con espinaca", 5f))
                    listMainCourse.add(Item("Gazpacho", "Gazpacho", 7f))
                }
                "Chinese", "Japanese", "Sushi Bars", "Lebanese" -> {
                    listMainCourse.add(Item("Vegetable fried rice", "Fried rice with egg, cabbage, broccoli, baby corn, carrot, mushroom, tomato, white onion and green onion", 4.05f))
                    listMainCourse.add(Item("Mixed vegetables", "Bowl of vegetable ingredients (Egg, mushroom, tomato, onion)", 4.95f))
                    listMainCourse.add(Item("Beef with oyster sauce", "Beef with oyster sauce", 6.45f))
                    listMainCourse.add(Item("Chicken with curry sauce", "Chicken with curry sauce", 6.05f))
                    listMainCourse.add(Item("Pork with sweet sour sauce", "Pork with sweet sour sauce", 5.85f))
                    listMainCourse.add(Item("Duck with orange sauce", "Duck with orange sauce", 7.85f))
                }
                "Indian" -> {
                    listMainCourse.add(Item("Mix vegetable curry", "Mix Vegetables Cooked in Thick Sauce and Spices", 6.80f))
                    listMainCourse.add(Item("Paneer Palak", "Spinach with Home Made Cheese and Spices", 6.75f))
                    listMainCourse.add(Item("Chicken Laziz", "Boneless Pieces Cooked with Mint, Spinach & Spices", 9f))
                    listMainCourse.add(Item("Chicken Lever Mushroom Masala", "Cooked with Almonds in a Rich Masala Sauce", 9.50f))
                    listMainCourse.add(Item("Lamb Rara", "Meat Pieces, Minced Meat, Green Peas Cooked in Rich/Medium Thick Sauce", 10.50f))
                    listMainCourse.add(Item("Special Biryani", "With King Prawns & Spinach, Mint (spicy)", 22f))
                }
                "Bakeries", "Bistros" -> {
                    listMainCourse.add(Item("Tuna", "Tuna - with mayonese, onion, red pepper and green, tomato and lettuce", 5.99f))
                    listMainCourse.add(Item("Gazpacho", "Desalted cod with onion and avocado", 6.75f))
                    listMainCourse.add(Item("Media Noche", "Roast pork, smoke ham, mustard, pickles with salad on a yellow bread", 5.99f))
                    listMainCourse.add(Item("Medio Dia", "Roast turkey, smoke ham, mustard, pickles on a yellow bread", 5.69f))
                    listMainCourse.add(Item("Cupavo", "Roast turkey, smoke ham, mustard, pickles with salad", 5.99f))
                    listMainCourse.add(Item("Cubano", "Roast pork, smoke ham, mustard, pickles with salad", 5.69f))
                }
            }

            return listMainCourse
        }

        /**
         * Generates a list of desserts
         * @return List of menu items
         */
        fun generateDesserts(): ArrayList<Item> {
            val listDesserts = arrayListOf<Item>()

            listDesserts.add(Item("Tiramisu", "Tiramisu", 3.00f))
            listDesserts.add(Item("Cold Coffee with Milk", "Cold Espresso coffee with milk", 3.00f))

            return listDesserts
        }

        /**
         * Generates a list of drinks
         * @return List of menu items
         */
        fun generateDrinks(): ArrayList<Item> {
            val listDrinks = arrayListOf<Item>()

            listDrinks.add(Item("Water", "50cl bottle of water", 1.50f))
            listDrinks.add(Item("Coca-cola", "33cl bottle of coca-cola", 2.00f))
            listDrinks.add(Item("Aquarius", "30cl tin of Aquarius", 1.90f))
            listDrinks.add(Item("House Wine", "1l of house wine", 10.00f))

            return listDrinks
        }

        /**
         * Generates a list of extras
         * @return List of menu items
         */
        fun generateExtras(): ArrayList<Item> {
            val listExtras = arrayListOf<Item>()

            listExtras.add(Item("Variety of Sauces", "Variety of our home-made sauces", 3.00f))
            listExtras.add(Item("Small bread", "Small bread", 0.90f))
            listExtras.add(Item("Plastic utensils", "Set of plastic utensils", 1.00f))

            return listExtras
        }
    }
}