package photos.brooklyn.shoppinglist2utils.models

data class ShoppingListItem (
    val id: Int = 0,
    val item: String,
    val priority: Int,
    val bought: Boolean,
    val quantity: Int,
    val section: Section,
    val description: String?,
    val note: String?,
    val repeated: Boolean,
    val price: Double?,
    val unit: String,
    val shop: Shop
)