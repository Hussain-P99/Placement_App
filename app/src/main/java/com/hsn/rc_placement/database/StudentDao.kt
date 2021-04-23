package com.hsn.rc_placement.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsn.rc_placement.model.Student

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Query("SELECT * FROM student_table")
    fun getAll(): LiveData<List<Student>>

    @Query("SELECT * FROm student_table WHERE companyId = :id")
    fun getStudentByCompanyId(id: Long): LiveData<List<Student>>
}