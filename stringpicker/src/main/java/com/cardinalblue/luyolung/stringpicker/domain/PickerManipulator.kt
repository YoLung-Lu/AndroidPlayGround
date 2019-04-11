package com.cardinalblue.luyolung.stringpicker.domain

import com.cardinalblue.luyolung.stringpicker.picker.PickerWidget
import com.cardinalblue.luyolung.stringpicker.model.command.ScrapUpdateCommand
import com.cardinalblue.luyolung.stringpicker.database.StringRepository
import com.cardinalblue.luyolung.stringpicker.model.TextScrapModel
import com.cardinalblue.luyolung.stringpicker.protocol.ICommand
import com.cardinalblue.luyolung.stringpicker.protocol.IWidgetContainer
import com.cardinalblue.luyolung.util.subscribeUntil
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.subjects.CompletableSubject

class PickerManipulator(val context: IWidgetContainer,
                        val startModel: TextScrapModel
) {

    val commandSignal = BehaviorRelay.create<ICommand>()

    private val pickerWidget: PickerWidget
    private val lifeCycle = CompletableSubject.create()

    init {
        val repository = StringRepository()
        pickerWidget = PickerWidget(repository.loadRandomData())
        context.putWidget(pickerWidget)

        subscribeToWidget()
    }

    private fun subscribeToWidget() {
        pickerWidget.selectionSignal
            .subscribeUntil(lifeCycle) {
                val textScrapModel = TextScrapModel(startModel.id, it)
                commandSignal.accept(
                    ScrapUpdateCommand(
                        startModel,
                        textScrapModel
                    )
                )
            }
    }

    fun stop() {
        context.removeWidget(pickerWidget)
        lifeCycle.onComplete()
    }
}