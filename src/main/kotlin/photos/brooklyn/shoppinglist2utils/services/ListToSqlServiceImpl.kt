package photos.brooklyn.shoppinglist2utils.services

import org.springframework.stereotype.Service
import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem
import photos.brooklyn.shoppinglist2utils.utils.DataUtils
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.ZoneId

const val SECTION_TABLE = "section"
const val ITEM_TABLE = "item"
const val SHOP_TABLE = "shop"
const val SHOPPING_LIST_ITEM_TABLE = "shopping_list_item"
const val SHOPPING_LIST_TABLE = "shopping_list"

@Service
class ListToSqlServiceImpl: ListToSqlService{
    private val trimRegex = "\n\\s*".toRegex()

    override fun convert(shoppingList: List<ShoppingListItem>, writer: PrintWriter) {
        shoppingList.associate { Pair(it.shop.id, it.shop) }.values.forEach{
            writer.println("INSERT INTO $SHOP_TABLE (id, name) VALUE('${it.id}', '${it.name}')")
        }
        shoppingList.associate { Pair(it.section.id, it.section) }.values.forEach{
            writer.println("INSERT INTO $SECTION_TABLE (id, name) VALUE('${it.id}', '${it.name}')")
        }
        // shopping list
        val shoppingListId = 1 // always one for this import
        val effectiveDate = LocalDateTime.now()
        val effectiveEpochTime = effectiveDate.atZone(ZoneId.systemDefault()).toEpochSecond()
        val endDate = effectiveDate.plusWeeks(1)
        val endDateEpochTime = endDate.atZone(ZoneId.systemDefault()).toEpochSecond()
        writer.println("INSERT INTO $SHOPPING_LIST_TABLE (id, createdDate, effectiveDate, endDate, active) VALUES($shoppingListId, to_timestamp($effectiveEpochTime), to_timestamp($effectiveEpochTime), to_timestamp($endDateEpochTime, 1)")

        // each item on the list
        shoppingList.forEach {
            writer.println("INSERT INTO $ITEM_TABLE (id, name, description, default_section, section_uncertain) VALUES(${it.id}, '${it.item}', ${DataUtils.nullableSqlStringValue(it.description)}, ${it.section.id}, ${if(it.section.uncertain) 1 else 0})")
            val note =
            writer.println("""
                INSERT INTO $SHOPPING_LIST_ITEM_TABLE (id, itemId, shoppingListId, shopId, quantity, note)
                VALUES(${it.id}, ${it.id}, $shoppingListId, ${it.shop.id}, '${it.quantity}', ${DataUtils.nullableSqlStringValue(it.note)})
            """.replace(trimRegex, ""))
        }
    }

}
