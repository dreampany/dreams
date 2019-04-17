package com.dreampany.frame.worker

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@MapKey
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)