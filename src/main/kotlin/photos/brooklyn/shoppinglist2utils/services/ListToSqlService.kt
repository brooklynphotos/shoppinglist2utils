package photos.brooklyn.shoppinglist2utils.services

import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem
import java.io.PrintWriter

interface ListToSqlService {
    /**
     * writes SQL based on the shopping list into writer
     */
    fun convert(shoppingList: List<ShoppingListItem>, writer: PrintWriter)
}