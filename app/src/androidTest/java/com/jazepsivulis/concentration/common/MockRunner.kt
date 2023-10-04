package com.jazepsivulis.concentration.common

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.jazepsivulis.concentration.MockApp

class MockRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, MockApp::class.java.name, context)
    }
}
