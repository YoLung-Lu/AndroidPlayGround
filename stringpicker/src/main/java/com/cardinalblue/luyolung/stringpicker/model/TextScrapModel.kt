package com.cardinalblue.luyolung.stringpicker.model

import com.cardinalblue.luyolung.stringpicker.picker.StringModel

class TextScrapModel(val id: Int,
                     val model: StringModel
) {

    init {
        var width: Int = model.string.length * 10
        var height: Int = 30
        var scal: Float = 1f
    }
}