package photos.brooklyn.shoppinglist2utils.google

import org.springframework.stereotype.Service

@Service
class GoogleSheetServiceImpl: GoogleSheetService {
    override fun retrieveData(spreadsheetId: String, range: String): List<List<Any?>>? {
        return GoogleSheetUtils.retrieveData(spreadsheetId, range)
    }
}