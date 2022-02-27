package com.jazepsivulis.concentration.injection

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MockInjectionModule::class])
interface MockInjectionComponent : InjectionComponent
