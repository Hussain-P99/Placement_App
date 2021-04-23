package com.hsn.rc_placement.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsn.rc_placement.model.Company

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Company)

    @Update
    suspend fun update(company: Company)

    @Query("SELECT * FROM company_table")
    fun getAll(): LiveData<List<Company>>

    @Query("SELECT * FROM company_table WHERE companyId = :id")
    suspend fun getCompanyById(id: Long): Company?


}