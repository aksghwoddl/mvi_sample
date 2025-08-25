package com.example.test.base

import android.os.Build
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(
    application = HiltTestApplication::class,
    sdk = [Build.VERSION_CODES.P],
    manifest = Config.NONE,
)
@RunWith(RobolectricTestRunner::class)
abstract class RobolectricTest
