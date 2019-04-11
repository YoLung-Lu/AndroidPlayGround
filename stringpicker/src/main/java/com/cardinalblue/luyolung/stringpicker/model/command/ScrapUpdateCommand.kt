package com.cardinalblue.luyolung.stringpicker.model.command

import com.cardinalblue.luyolung.stringpicker.model.Canvas
import com.cardinalblue.luyolung.stringpicker.model.TextScrapModel
import com.cardinalblue.luyolung.stringpicker.protocol.ICommand

class ScrapUpdateCommand(val scrap1: TextScrapModel,
                         val scrap2: TextScrapModel
): ICommand {

    fun doo(target: Canvas) {
        target.apply(scrap2)
    }

    fun undo(target: Canvas) {
        target.apply(scrap1)
    }
}