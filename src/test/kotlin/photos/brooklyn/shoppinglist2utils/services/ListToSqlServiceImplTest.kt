package photos.brooklyn.shoppinglist2utils.services

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import photos.brooklyn.shoppinglist2utils.models.Section
import photos.brooklyn.shoppinglist2utils.models.Shop
import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem
import java.io.PrintWriter

/**
 * tests service to convert list of shopping list items to sql for storage in db
 */
class ListToSqlServiceImplTest {
    val service = ListToSqlServiceImpl()

    @Test
    fun convert() {
        val mockPrintWriter = mock(PrintWriter::class.java)
        val shoppingList = listOf<ShoppingListItem>(
            ShoppingListItem(0,"""""00" four""", 0, false, "1", Section(1,"Baking"),null,null,false,null,"bag",Shop(1,"WF"))
        )
        service.convert(shoppingList, mockPrintWriter)
        verify(mockPrintWriter).println()
    }
}