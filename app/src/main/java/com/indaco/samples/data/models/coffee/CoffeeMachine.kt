package com.indaco.samples.data.models.coffee

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

/*
 * extracted methods from viewmodel into this "CoffeeMachine" class for visibility
 * purpose is to test State Machine and test StateFlow/SharedFlow
 */
class CoffeeMachine(var coffeeOrder: Coffee? = null, val scope: CoroutineScope) {

    companion object {
        const val MAX_WATER_LEVEL = 48
        const val MAX_BEAN_LEVEL = 100
    }

    private val dispatcher = Dispatchers.Default

    // cannot restart jobs, must cancel/complete and assign new job to restart
    private var stateJob: Job? = null

    private var prevState: State? = null

    var stateFlow = MutableStateFlow(State.STAND_BY)
    val state get() = stateFlow.value

    var powerIsOn: Boolean = false
    var waterLevel: Int = MAX_WATER_LEVEL
    var beanLevel: Int = MAX_BEAN_LEVEL

    fun refillBeans() {
        scope.launch(dispatcher) {
            delay(2_000)
            beanLevel = MAX_BEAN_LEVEL
        }
    }

    fun powerUp() {
        scope.launch(dispatcher) {
            delay(2_000)
            powerIsOn = true
        }
    }

    fun powerDown() {
        scope.launch(dispatcher) {
            delay(1_000)
            powerIsOn = false
        }
    }

    fun refillWater() {
        scope.launch(dispatcher) {
            delay(2_000)
            waterLevel = MAX_WATER_LEVEL
        }
    }

    fun makeCoffee() {
        if (stateJob != null && stateJob!!.isActive)
            stateFlow.value = prevState!!
        else
            collectStateFlow()
    }

    private fun collectStateFlow() {
        stateJob = scope.launch(dispatcher) {
            stateFlow.collect {
                Log.d("TAG", "collectStateFlow: ${it.name}")
                when (state) {
                    State.STAND_BY -> checkPower()
                    State.CHECK_FORM -> checkForm()
                    State.CHECK_WATER -> checkWater()
                    State.CHECK_BEANS -> checkBeans()
                    State.BREWING -> brewCoffee()
                    State.COMPLETE -> stopCollecting()
                    State.ERROR -> Log.e("TAG", "error...")
                }
            }
        }
    }

    private suspend fun stopCollecting() {
        stateJob?.cancel()
        stateFlow.emit(State.STAND_BY)
    }

    private suspend fun checkForm() {
        Log.d("TAG","checkForm: coffeeOrder != null: ${coffeeOrder != null}")
        if (coffeeOrder != null) {
            changeState(State.CHECK_WATER)
            checkWater()
        } else {
            error()
        }
    }

    private suspend fun checkPower() {
        Log.d("TAG","checkPower: powerIsOn: ${powerIsOn}")
        if (powerIsOn) {
            changeState(State.CHECK_FORM)
        } else {
            error()
        }
    }

    private suspend fun checkWater() {
        Log.d("TAG","checkWater: hasEnoughWater: ${hasEnoughWater(coffeeOrder!!.size)}")
        if (hasEnoughWater(coffeeOrder!!.size)) {
            boilWater()
            changeState(State.CHECK_BEANS)
        } else {
            error()
        }
    }

    private fun hasEnoughWater(size: Size): Boolean = waterLevel >= size.oz

    private suspend fun boilWater() {
        delay(5_000)
    }

    private suspend fun checkBeans() {
        Log.d("TAG","checkBeans: hasEnoughBeans: ${hasEnoughBeans()}")
        if (hasEnoughBeans()) {
            grindBeans()
            changeState(State.BREWING)
        } else {
            error()
        }
    }

    private fun hasEnoughBeans() = beanLevel >= MAX_BEAN_LEVEL / 4

    private suspend fun grindBeans() {
        delay(5_000)
    }

    private suspend fun brewCoffee() {
        delay(5_000)
        changeState(State.COMPLETE)
    }

    private suspend fun changeState(newState: State) {
        Log.d("TAG","changeState: currentState: ${stateFlow.value.name} newState: ${newState.name}")
        stateFlow.emit(newState)
    }

    private suspend fun error() {
        prevState = stateFlow.value
        changeState(State.ERROR)
        val cache = stateFlow.replayCache
        val secondLastIndex = cache.lastIndex-1
        val secondToLast = if (secondLastIndex >= 0)
            cache[secondLastIndex]
        else
            cache.last()
        Log.e("TAG","error: prevState: ${prevState!!.name} state: ${stateFlow.value.name}, last: ${secondToLast.name}")
    }
}