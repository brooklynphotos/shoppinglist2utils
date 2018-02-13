package photos.brooklyn.shoppinglist2utils.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import photos.brooklyn.shoppinglist2utils.google.GoogleSheetService
import photos.brooklyn.shoppinglist2utils.models.Section
import photos.brooklyn.shoppinglist2utils.models.Shop
import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem
import photos.brooklyn.shoppinglist2utils.utils.DataUtils

@Service
class RawDataServiceGSImpl: RawDataService{
    @Autowired
    private lateinit var service: GoogleSheetService

    override fun loadRawData(): List<ShoppingListItem>? {
        val rawData = service.retrieveData("1YSyR5SZda_t8m31FbOEzCIs-SRs3yF_j0SP44sCqRn4", "master list!A2:K900") ?: return null
        val unsaved = rawData.map(::convertCells)
        val shopNames = unsaved.associate { Pair(it.shop.name, it) }.keys.toList().sorted()
        val sectionNames = unsaved.associate { Pair(DataUtils.cleanSectionUncertainty(it.section.name), it) }.keys.toList().sortedBy { it.first }
        // index + 1 because we need to start with 1 for the id
        val shopsByName = shopNames.withIndex().map { (index, shopName) -> Shop(index+1,shopName) }.associate { Pair(it.name, it) }
        val sectionByName = sectionNames.withIndex().map { (index, sectionNamePair) -> Section(index+1, sectionNamePair.first, sectionNamePair.second) }.associate { Pair(it.name, it) }
        return unsaved.withIndex().map {
            (index, item) ->
                item.copy(
                    id=index+1,
                    shop=shopsByName.getOrElse(item.shop.name) {throw Exception("Shop Name doesn't exist: ${item.shop.name}")},
                    section=sectionByName.getOrElse(DataUtils.cleanSectionUncertainty(item.section.name).first) { throw Exception("Section name doesn't exist: ${item.section.name}")}
                )
        }
    }

    fun convertCells(cells: List<String?>): ShoppingListItem {
        return ShoppingListItem(
            0,
            cells[0]!!,
            cells[1]!!.toInt(),
            toBoolean(cells[2]),
            cells[3]!!,
            Section(0, cells[4]!!),
            cells[5],
            cells[6],
            toBoolean(cells[7]),
            toDouble(cells[8]),
            cells[9]!!,
            Shop(0, cells[10]!!)
        )
    }
}

/**
 * creates another section with the uncertainty set based on whether the original name has a question mark
 */
private fun applyUncertainty(section: Section, itemSectionName: String): Section {
    return section.copy(uncertain = itemSectionName.endsWith("?"))
}


private fun toBoolean(cellVal: String?): Boolean{
    return cellVal=="1"
}

private fun toDouble(cellVal: String?): Double?{
    return if(cellVal==null || cellVal=="") null else if(cellVal.startsWith("$")) cellVal.substring(1).toDouble() else cellVal.toDouble()
}