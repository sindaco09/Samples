package com.example.samples.ui.main.coffee

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.models.coffee.Coffee
import com.example.samples.data.models.coffee.CoffeeMachine
import com.example.samples.data.models.coffee.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoffeeMachineViewModel : ViewModel() {

    private val dispatcher = Dispatchers.Default

    // TODO: convert status to StateFlow or SharedFlow
    var statusLiveData = MutableLiveData("Status: ")
    var progress = MutableLiveData(Pair(0,0))

    var coffeeOrder: Coffee? = null

    var coffeeMachine: CoffeeMachine = CoffeeMachine(scope = viewModelScope)

    var orderFormExists = MutableLiveData<Boolean>()

    // xml functions
    fun refillWater() {
        Log.d("TAG","refillWater: coffeeMachine waterlevel: ${coffeeMachine.waterLevel}")
        coffeeMachine.refillWater()
    }

    fun refillBeans() {
        Log.d("TAG","refillBeans: coffeeMachine beanLevel: ${coffeeMachine.beanLevel}")
        coffeeMachine.refillBeans()
    }

    fun togglePower() {
        Log.d("TAG","togglePower: coffeeMachine powerIsOn: ${coffeeMachine.powerIsOn}")
        if (coffeeMachine.powerIsOn)
            coffeeMachine.powerDown()
        else
            coffeeMachine.powerUp()
    }

    fun makeCoffee() {
        if (coffeeMachine.state == State.STAND_BY || coffeeMachine.state == State.ERROR) {
            coffeeMachine.coffeeOrder = coffeeOrder
            viewModelScope.launch(dispatcher) {
                coffeeMachine.makeCoffee()
                coffeeMachine.stateFlow.collect {
                    statusLiveData.postValue(it.status)
                }
            }
        } else {
            Log.e("TAG","makeCoffee: badState: ${coffeeMachine.state.name}")
        }
    }
}

