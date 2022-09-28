package com.bell.exercise.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bell.exercise.data.model.CarModel
import com.bell.exercise.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuidomiaViewModel @Inject constructor(private val repo: CarRepository) : ViewModel() {

    private val itemsList = MutableStateFlow(listOf<CarModel>())
    val items: StateFlow<List<CarModel>> get() = itemsList

    private val dropdownItemsList = MutableStateFlow(listOf<CarModel>())
    val dropdownItems: StateFlow<List<CarModel>> get() = dropdownItemsList

    private val itemIdsList = MutableStateFlow(listOf<Int>())
    val itemIds: StateFlow<List<Int>> get() = itemIdsList

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
               itemsList.emit(repo.loadData())
               dropdownItemsList.emit(repo.loadData())
            }
        }
    }

    fun filterData(make:String, model:String) {

        if (make.isNotEmpty() || model.isNotEmpty()) {
            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    val filteredList = mutableListOf<CarModel>()
                    for (car in dropdownItems.value) {

                        if(car.make?.equals(make) == true || car.model?.equals(model)==true){
                            filteredList.add(car)
                        }
                    }
                    itemsList.value = filteredList
                }
            }
        }
        else{
            itemsList.value = dropdownItems.value
        }
    }

    fun onItemClicked(itemId: Int) {
        itemIdsList.value = itemIdsList.value.toMutableList().also { list ->
            if (list.contains(itemId)) {
                list.remove(itemId)
            } else {
                list.add(itemId)
            }
        }
    }
}


