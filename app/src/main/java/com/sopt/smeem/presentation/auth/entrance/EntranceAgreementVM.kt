package com.sopt.smeem.presentation.auth.entrance

import androidx.lifecycle.ViewModel
import com.sopt.smeem.presentation.auth.entrance.EntranceSelection.*

class EntranceAgreementVM : ViewModel() {
    private val selections = mutableSetOf<EntranceSelection>()
    fun selectAll() {
        selections.addAll(EntranceSelection.getAll())
    }

    fun cancelAll() {
        selections.clear()
    }

    fun cancel(selection: EntranceSelection) {
        selections.remove(selection)
    }

    fun add(selection: EntranceSelection) {
        selections.add(selection)
    }

    fun isSelected(element: EntranceSelection) = selections.contains(element)

    fun allSelected() = selections.size == EntranceSelection.getAll().size
    fun canGoNext() = selections.containsAll(setOf(SERVICE, PERSONAL, LOCATION))
    fun getSelected() = selections
}

enum class EntranceSelection {
    SERVICE,
    PERSONAL,
    LOCATION,
    MARKETING,
    ;

    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<EntranceSelection> = setOf(SERVICE, PERSONAL, LOCATION, MARKETING)
        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
    }
}