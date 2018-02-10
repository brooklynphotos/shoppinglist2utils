package photos.brooklyn.shoppinglist2utils.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import photos.brooklyn.shoppinglist2utils.google.GoogleSheetService
import photos.brooklyn.shoppinglist2utils.models.Section
import photos.brooklyn.shoppinglist2utils.models.Shop
import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem

@Service
class RawDataServiceGSImpl: RawDataService{
    @Autowired
    private lateinit var service: GoogleSheetService

    override fun loadRawData(): List<ShoppingListItem>? {
        val rawData = service.retrieveData("1YSyR5SZda_t8m31FbOEzCIs-SRs3yF_j0SP44sCqRn4", "master list!A2:K900")
        if(rawData==null) return null
        val unsaved = rawData.map(::convertCells)
        val shopNames = unsaved.associate { Pair(it.shop.name, it) }.keys.toList().sorted()
        val sectionNames = unsaved.associate { Pair(it.section.name, it) }.keys.toList().sorted()
        // index + 1 because we need to start with 1 for the id
        val shopsByName = shopNames.withIndex().map { (index, shopName) -> Shop(index+1,shopName) }.associate { Pair(it.name, it) }
        val sectionByName = sectionNames.withIndex().map { (index, sectionName) -> Section(index+1, sectionName) }.associate { Pair(it.name, it) }
        return unsaved.withIndex().map {
            (index, item) ->
                item.copy(
                    id=index+1,
                    shop=shopsByName.getOrElse(item.shop.name) {throw Exception("Shop Name doesn't exist: ${item.shop.name}")},
                    section=sectionByName.getOrElse(item.section.name) { throw Exception("Section name doesn't exist: ${item.section.name}")}
                )
        }
    }

    fun convertCells(cells: List<*>): ShoppingListItem {
        return ShoppingListItem(
            0,
            cells[0] as String,
            cells[1] as Int,
            toBoolean(cells[2] as? Int),
            cells[3] as Int,
            Section(0, cells[4] as String),
            cells[5] as? String,
            cells[6] as? String,
            toBoolean(cells[7] as? Int),
            cells[8] as? Double,
            cells[9] as String,
            Shop(0, cells[10] as String)
        )
    }
}

private fun toBoolean(cellVal: Int?): Boolean{
    return cellVal==1
}