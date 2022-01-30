package net.tandem.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * this class is created in order to convert data types into appropriate type to be saved in room database
 * */
class DataConverter {
    private val gson by lazy { Gson() }

    /**
     * Converts List of String into String object for saving in database
     *
     * @param stringList the list to be converted into String
     */
    @TypeConverter
    fun fromStringList(stringList: List<String?>?): String {
        return if (stringList.isNullOrEmpty())
            ""
        else
            gson.toJson(stringList)
    }

    /**
     * Converts String object fetched from database into List of String
     *
     * @param data String object to be converted into List of String
     */
    @TypeConverter
    fun toStringList(data: String?): List<String> {
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(data, type) ?: listOf()
    }
}