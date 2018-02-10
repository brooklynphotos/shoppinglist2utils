package photos.brooklyn.shoppinglist2utils.google

/**
 * holds a row of data in the google spreadsheet
 */
data class ShoppingListRawItem (
        val item: String,
        val priority: Int,
        val bought: Boolean,
        val quantity: Int,
        val section: String,
        val description: String,
        val note: String,
        val repeated: Boolean,
        val price: Double,
        val Unit: String,
        val Store: String
)