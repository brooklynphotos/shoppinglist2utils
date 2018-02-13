package photos.brooklyn.shoppinglist2utils.google


import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * simple utility for retrieving data from a google spreadsheet that you have access to. Based on code from: https://developers.google.com/sheets/api/quickstart/java
 */
object GoogleSheetUtils {
    /** Application name.  */
    private val APPLICATION_NAME = "shopping list"

    /** Directory to store user credentials for this application.  */
    private val DATA_STORE_DIR = java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart")

    /** Global instance of the [FileDataStoreFactory].  */
    private var DATA_STORE_FACTORY: FileDataStoreFactory? = null

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport.  */
    private var HTTP_TRANSPORT: HttpTransport? = null

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private val SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY)

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    private val sheetsService: Sheets
        @Throws(IOException::class)
        get() {
            val credential = authorize()
            return Sheets.Builder(HTTP_TRANSPORT!!, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build()
        }

    init {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
            DATA_STORE_FACTORY = FileDataStoreFactory(DATA_STORE_DIR)
        } catch (t: Throwable) {
            t.printStackTrace()
            System.exit(1)
        }

    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun authorize(): Credential {
        // Load client secrets.
        val `in` = GoogleSheetUtils::class.java.getResourceAsStream("/client_secret.json")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY!!)
                .setAccessType("offline")
                .build()
        val credential = AuthorizationCodeInstalledApp(
                flow, LocalServerReceiver()).authorize("user")
        println(
                "Credentials saved to " + DATA_STORE_DIR.absolutePath)
        return credential
    }

    /**
     * @param spreadsheetId the id in something like https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @param range the range like "master list!A2:K"
     */
    @Throws(IOException::class)
    @JvmStatic
    fun retrieveData(spreadsheetId: String, range: String): List<List<String?>>? {
        // Build a new authorized API client service.
        val service = sheetsService

        val response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute()
        return response.getValues().map{ it.map { it?.toString() } }
    }


}