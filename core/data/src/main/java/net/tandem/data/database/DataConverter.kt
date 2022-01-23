package net.tandem.data.database

import androidx.room.TypeConverter

/**
 * this class is created in order to convert data types into appropriate type to be saved in room database
 * */
class DataConverter {
    /**
     * Converts List of String into String object for saving in database
     *
     * @param stringList the list to be converted into String
     */
    @TypeConverter
    fun fromStringList(stringList: List<String?>?): String? {
        if (stringList.isNullOrEmpty()) return null
        return stringList.joinToString(",")
    }

    /**
     * Converts String object fetched from database into List of String
     *
     * @param data String object to be converted into List of String
     */
    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        if (data.isNullOrEmpty()) return null
        return data.split(",")
    }
}