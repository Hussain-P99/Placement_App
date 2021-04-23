package com.hsn.rc_placement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// class representing a single student

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val studentId: Long = 0,
    @ColumnInfo(name = "studentName")
    val studentName: String,
    @ColumnInfo(name = "companyId")
    val companyId: Long
)
