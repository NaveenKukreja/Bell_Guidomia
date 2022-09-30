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


/**
 *GuidomiaViewModel is viewModel for GuidomiaActivity
 * @param repo - car repository object
 */
@HiltViewModel
class GuidomiaViewModel @Inject constructor(private val repo: CarRepository) : ViewModel() {

    private val carItemsList = MutableStateFlow(listOf<CarModel>())
    val carItems: StateFlow<List<CarModel>> get() = carItemsList

    private val dropdownItemsList = MutableStateFlow(listOf<CarModel>())
    val dropdownItems: StateFlow<List<CarModel>> get() = dropdownItemsList

    private val itemIdsList = MutableStateFlow(listOf<Int>())
    val itemIds: StateFlow<List<Int>> get() = itemIdsList

    init {
        fetchData()
    }

    /**
     * fetchData is used to fetch the data from repo
     */
    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
               val carData = repo.loadData()
               carItemsList.emit(carData)
               dropdownItemsList.emit(carData)
            }
        }
    }

    /**
     * filterData method is used for filter the expandable list
     * @param make - car brand name
     * @param model - car model name
     */
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
                    carItemsList.value = filteredList
                }
            }
        }
        else{
            carItemsList.value = dropdownItems.value
        }
    }


    /**
     * onItemClicked method is used to manage the expand and collapse state
     * @param itemId - it is the id for expanded list
     */
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


