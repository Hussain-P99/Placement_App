package com.hsn.rc_placement.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsn.rc_placement.database.StudentDao

class StudentViewModel(val studentDb: StudentDao, val companyId: Long) : ViewModel() {
    val students = studentDb.getStudentByCompanyId(companyId)
}

@Suppress("UNCHECKED_CAST")
class StudentVMFactory(val studentDb: StudentDao, val companyId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            return StudentViewModel(studentDb, companyId) as T
        }
        throw IllegalArgumentException()
    }

}