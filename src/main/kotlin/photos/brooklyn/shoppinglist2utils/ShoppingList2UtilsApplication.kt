package photos.brooklyn.shoppinglist2utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import photos.brooklyn.shoppinglist2utils.services.ListToSqlService
import photos.brooklyn.shoppinglist2utils.services.RawDataService
import java.io.File

@SpringBootApplication
class ShoppingList2UtilsApplication: CommandLineRunner {
    @Autowired
    lateinit var listToSqlService: ListToSqlService
    @Autowired
    lateinit var rawDataService: RawDataService

    override fun run(vararg args: String?) {
        val f = File(args[0],"shoppinglist.sql")
        val writer = f.printWriter()
        listToSqlService.convert(
            rawDataService.loadRawData() ?: throw IllegalStateException("Should have gotten data"),
            writer
        )
        println("wrote to ${f.absolutePath}")
    }
}

fun main(args: Array<String>) {
    runApplication<ShoppingList2UtilsApplication>(*args)
}
