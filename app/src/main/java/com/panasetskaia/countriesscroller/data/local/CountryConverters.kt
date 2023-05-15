package com.panasetskaia.countriesscroller.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class CountryConverters {

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), list)
    }

    @TypeConverter
    fun stringToList(s: String): List<String> {
        return Json.decodeFromString(ListSerializer(String.serializer()),s)
    }
}