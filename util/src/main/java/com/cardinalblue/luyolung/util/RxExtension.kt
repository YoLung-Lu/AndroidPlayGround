package com.cardinalblue.luyolung.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.takeUntil(control: Completable) =
    takeUntil(control.toObservable<T>())

fun <T> Observable<T>.subscribeUntil(control: Completable, onNext: ((T) -> Unit) = {}) =
    takeUntil(control).subscribe(onNext)

fun <T> Single<T>.subscribeUntil(control: Completable, onNext: ((T) -> Unit) = {}) =
    takeUntil(control).subscribe(onNext)

fun observableTimer(timeStep: Int = 3): Observable<Int> =
    Observable.fromIterable(IntRange(1, 10).toList())
        .zipWith(Observable.interval(1, timeStep.toLong(), TimeUnit.SECONDS),
             BiFunction { intValue, _ -> intValue })