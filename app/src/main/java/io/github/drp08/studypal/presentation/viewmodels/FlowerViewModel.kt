package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlowerViewModel : ViewModel() {

    private val _boxCount = MutableStateFlow(6)
    val boxCount: StateFlow<Int> = _boxCount

    private val _flowerCounts = MutableStateFlow(listOf(3, 6, 2, 7, 9, 4))
    val flowerCounts: StateFlow<List<Int>> = _flowerCounts

    private val _names = MutableStateFlow(listOf("Garden 1", "Garden 2", "Garden 3", "Garden 4", "Garden 5", "Garden 6"))
    val names: StateFlow<List<String>> = _names

    fun updateData(boxCount: Int, flowerCounts: List<Int>, names: List<String>) {
        viewModelScope.launch {
            _boxCount.emit(boxCount)
            _flowerCounts.emit(flowerCounts)
            _names.emit(names)
        }
    }
}