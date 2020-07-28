package cenec.mealvity.mealvity.classes.help

data class Help(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)