package com.dreampany.framework.inject.annote

import javax.inject.Scope

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ChildFragmentScope

