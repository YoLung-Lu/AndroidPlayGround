package com.cardinalblue.luyolung.stringpicker.picker

import com.cardinalblue.luyolung.stringpicker.picker.StringModel
import com.cardinalblue.luyolung.stringpicker.protocol.IWidget
import com.jakewharton.rxrelay2.BehaviorRelay

class PickerWidget(val data: MutableList<StringModel>): IWidget {

    val selectionSignal = BehaviorRelay.create<StringModel>()

    fun select(selection: StringModel) {
        if (data.contains(selection)) {
            selectionSignal.accept(selection)
        }
    }
}