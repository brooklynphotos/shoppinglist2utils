package photos.brooklyn.shoppinglist2utils.services

import photos.brooklyn.shoppinglist2utils.models.ShoppingListItem

interface RawDataService {
    /**
     * loads the raw data.
     * @return might be an empty list if nothing is in the sheet
     */
    fun loadRawData(): List<ShoppingListItem>?
}