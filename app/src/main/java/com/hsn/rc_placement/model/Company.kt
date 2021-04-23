package com.hsn.rc_placement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// class representing a single company

@Entity(tableName = "company_table")
data class Company(
    @PrimaryKey(autoGenerate = true)
    val companyId: Long = 0,
    @ColumnInfo(name = "companyName")
    val companyName: String,
    @ColumnInfo(name = "studentsHired")
    var studentsHired: Int = 0
)

