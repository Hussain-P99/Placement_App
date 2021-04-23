package com.hsn.rc_placement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hsn.rc_placement.model.Company
import com.hsn.rc_placement.model.Student


@Database(entities = [Student::class, Company::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract val companyDb: CompanyDao
    abstract val studentDb: StudentDao

    companion object {

        @Volatile
        private lateinit var appdatabase: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if (!this::appdatabase.isInitialized) {
                    appdatabase =
                        Room.databaseBuilder(context, AppDatabase::class.java, "RC_Placement_DB")
                            .fallbackToDestructiveMigration().build()
                }
                return appdatabase
            }
        }
    }

}