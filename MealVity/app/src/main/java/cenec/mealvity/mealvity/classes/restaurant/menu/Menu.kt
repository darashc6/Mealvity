package cenec.mealvity.mealvity.classes.restaurant.menu


import java.io.Serializable

data class Menu(
    var menu: ArrayList<Section>
)

data class Section(
    var name: String,
    var items: ArrayList<Item>,
    var isExpanded: Boolean
)


data class Item(
    var name: String,
    var description: String,
    var price: Float
): Serializable