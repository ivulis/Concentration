package com.jazepsivulis.concentration

import com.jazepsivulis.concentration.injection.DaggerMockInjectionComponent
import com.jazepsivulis.concentration.injection.InjectionComponent
import com.jazepsivulis.concentration.injection.MockInjectionModule

class MockApp : App() {
    override fun makeComponent(): InjectionComponent =
        DaggerMockInjectionComponent.builder().mockInjectionModule(MockInjectionModule(this)).build()
}
