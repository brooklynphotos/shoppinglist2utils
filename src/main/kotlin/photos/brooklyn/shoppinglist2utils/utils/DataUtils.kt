package photos.brooklyn.shoppinglist2utils.utils

object DataUtils {
    private val uncertaintyRegex = "\\?+$".toRegex()
    private val unknownRegex = "^\\?+$".toRegex()
    private val quoteRegex = "'".toRegex()

    fun cleanSectionUncertainty(sectionName: String): Pair<String, Boolean>{
        if(unknownRegex.matches(sectionName)) return Pair("Unknown", false)
        val cleaned = sectionName.replace(uncertaintyRegex, "")
        return Pair(cleaned, cleaned.length !=sectionName.length)
    }

    /**
     * useful for building sql statements that uses nullable string values
     */
    fun nullableSqlStringValue(stringValue: String?): String = if(stringValue.isNullOrBlank()) "null" else "'$stringValue'"

    fun escapeQuote(stringValue: String?): String? = stringValue?.replace("'", "''")
}