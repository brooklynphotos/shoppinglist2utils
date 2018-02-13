package photos.brooklyn.shoppinglist2utils.google

/**
 * wrapper around the google util; this allows for mock testing that doesn't rely on the google util
 */
interface GoogleSheetService {
    fun retrieveData(spreadsheetId: String, range: String): List<List<String?>>?
}