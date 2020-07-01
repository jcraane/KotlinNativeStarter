package nl.jamiecraane.multiplatform.myapp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ReadWritePropertyTest {
    @Test
    fun testSerialization() {
        val person = PersonHolder().person?.item
        assertEquals("John", person?.firstName)
    }
}

private data class Person(val firstName: String, val lastName: String)

private class PersonHolder {
    var person: CacheItem<Person>? by SharedPrefsGsonCacheItem(
        "key",
        Gson(),
        object : TypeToken<CacheItem<Person>>() {}.type
    )
}

class CacheItem<ITEMTYPE>(val item: ITEMTYPE, val creationTime: Long)

class SharedPrefsGsonCacheItem<T>(private val key: String, private val gson: Gson, private val type: Type) :
    ReadWriteProperty<Any?, CacheItem<T>?> {
    override operator fun getValue(thisRef: Any?, prop: KProperty<*>): CacheItem<T>? {
        val json = """
            {
              "item": {
                "firstName": "John",
                "lastName": "Smith"
              }
            }
        """.trimIndent()

        return if (json.isNotEmpty()) {
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    override operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: CacheItem<T>?) {
        println("putString")
    }
}