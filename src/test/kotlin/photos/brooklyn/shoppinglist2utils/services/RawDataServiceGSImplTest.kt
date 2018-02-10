package photos.brooklyn.shoppinglist2utils.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import photos.brooklyn.shoppinglist2utils.google.GoogleSheetService

@RunWith(MockitoJUnitRunner::class)
class RawDataServiceGSImplTest{

    @InjectMocks
    private val service = RawDataServiceGSImpl()

    @Mock
    private lateinit var googleServiceMock: GoogleSheetService

    @Test
    fun convertCells() {
        val givenData = listOf("Abate Fetel Pears", 0, null, 3, "Produce", "On sale for 1.99/lb", "Some note", 1, 23.45, "lb", "WF")
        val actualData = service.convertCells(givenData)
        assertEquals("Some note", actualData.note)
    }

    @Test
    fun convertDoc() {
        val givenData = listOf(
            listOf("Abate Fetel Pears", 0, null, 3, "Produce", "On sale for 1.99/lb", "Some note", 1, 23.45, "lb", "WF"),
            listOf("Almond", 3, null, 4, "Bulk", null, null, 1, null, "cup", "TJ"),
            listOf("Alaska Wild Salmon", 1, 1, 2, "Fish", "Near belly area", "No note", null, null, "lb", "WF")
        )
        Mockito.`when`(googleServiceMock.retrieveData(anyString(), anyString())).thenReturn(givenData)
        val actualData = service.loadRawData()
        assertNotNull(actualData)
        val first = actualData?.get(0)
        assertEquals("Some note", first?.note)
        assertEquals(1, first?.id)
        assertEquals(2, first?.shop?.id)
        assertEquals(3, first?.section?.id)
    }

}
