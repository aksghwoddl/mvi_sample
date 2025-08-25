/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * 테스트할 대상의 코루틴 컨텍스트를 변경 한다. 생성자에 dispatcher를 전달 하지 않을 경우 기본적으로
 * `UnconfinedTestDispatcher`을 사용 한다. 필요에 따라 `StandardTestDispatcher`을 사용할 수 도 있다.
 * 이 Rule을 사용 하려면 `@get:Rule`어노테이션을 사용 한다.
 * - UnconfinedTestDispatcher : 코루틴의 시작 되는 순서를 보장하지 않고 바로 실행 한다. 그렇기 때문에 `StandardTestDispatcher`
 * 와 달리 runCurrent(), advanceUntilIdle()등을 호출할 필요가 없다.
 * - StandardTestDispatcher : 코루틴 실행을 자동으로 하지 않고 보류 상태로 남김. 그러다가 원하는 때 에 코루틴을 실행 하여
 * 테스트를 진행 할 수 있음.
 *     - 코루틴을 실행 하거나 다시 보류 시키려면 runCurrent(), advanceUntilIdle(), advanceTimeBy()등 참고.
 * - 코루틴을 테스트 할 때 좀 더 자세한 제어를 하고 싶다면 `StandardTestDispatcher`을, 실행 순서와 상관없으며 간단하다면
 * `UnconfinedTestDispatcher`을 사용 한다.
 * - [참고](https://craigrussell.io/2022/01/19/comparing-standardtestdispatcher-and-unconfinedtestdispatcher)
 */

class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
