package com.jazepsivulis.concentration

import android.app.Application
import com.jazepsivulis.concentration.common.LineNumberDebugTree
import com.jazepsivulis.concentration.injection.DaggerInjectionComponent
import com.jazepsivulis.concentration.injection.InjectionComponent
import com.jazepsivulis.concentration.injection.InjectionModule
import timber.log.Timber

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
        component = makeComponent()
    }

    open fun makeComponent(): InjectionComponent = DaggerInjectionComponent
        .builder()
        .injectionModule(InjectionModule(this))
        .build()

    companion object {
        lateinit var component: InjectionComponent
            private set
    }
}
