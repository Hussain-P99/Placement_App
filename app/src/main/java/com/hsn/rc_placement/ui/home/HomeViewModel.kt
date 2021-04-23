package com.hsn.rc_placement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsn.rc_placement.database.CompanyDao

class HomeViewModel(companyDb: CompanyDao) : ViewModel() {

    val companyList = companyDb.getAll()

}

@Suppress("UNCHECKED_CAST")
class HomeVMFactory(private val companyDb: CompanyDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(companyDb) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }

}