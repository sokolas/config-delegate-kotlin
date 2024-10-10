import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.kotlin.treeToValue
import java.io.File
import kotlin.reflect.KProperty

val om = ObjectMapper().registerKotlinModule()

class PropertyDelegate(val json: JsonNode) {
    inline operator fun <reified T>getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (json.has(property.name)) {
            print("${property.name} accessed, ")
            return om.treeToValue(json.get(property.name))  // TODO cache
        } else {
            return null
        }
    }
}

class ConfigDTO(json: JsonNode) {
    val i: Int? by PropertyDelegate(json)
    val inner: InnerDTO? by PropertyDelegate(json)
}

class InnerDTO(val name: String?) {}

fun main() {
    val s = File("config.json").readText()

    val json = om.readTree(s)

    var c = ConfigDTO(json)

    println(c.i)
    println(c.inner?.name)
}