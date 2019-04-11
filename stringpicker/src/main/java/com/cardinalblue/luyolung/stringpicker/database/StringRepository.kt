package com.cardinalblue.luyolung.stringpicker.database

import com.cardinalblue.luyolung.stringpicker.picker.StringModel

class StringRepository {

    private val STRING_LENGTH = 10
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun loadRandomData(): MutableList<StringModel> {
        val stringList = mutableListOf<StringModel>()
        for (i in 1..10) {
            stringList.add(StringModel(randomString()))
        }
        return stringList
    }

    private fun randomString(): String =
        (1..STRING_LENGTH)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
}