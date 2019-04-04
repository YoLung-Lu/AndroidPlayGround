package com.cardinalblue.luyolung.util.protocol


interface ILifecycleAware {

    /**
     * Start the widget and caller is responsible for completing the subscription.
     *
     * @return An observable that gives true if initialization is successful, and
     * call [stop] when it gets disposed.
     */
    fun start()

    /**
     * Stop the widget, where it would automatically execute if you return [autoStop]
     * in the [start] method.
     */
    fun stop()
}
