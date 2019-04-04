package com.cardinalblue.luyolung.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Observable<T>.takeUntil(control: Completable) =
    takeUntil(control.toObservable<T>())

fun <T> Observable<T>.subscribeUntil(control: Completable, onNext: ((T) -> Unit) = {}) =
    takeUntil(control).subscribe(onNext)

fun <T> Single<T>.subscribeUntil(control: Completable, onNext: ((T) -> Unit) = {}) =
    takeUntil(control).subscribe(onNext)
