package com.hsn.rc_placement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navCtrl: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navCtrl = findNavController(R.id.navHostFragment)

        setupActionBarWithNavController(navCtrl)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navCtrl.navigateUp() || super.onSupportNavigateUp()
    }
}