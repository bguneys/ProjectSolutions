package com.everydaysolutions.bguneys.everydaysolutions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //change theme to default after showing splash screen
        setTheme(R.style.AppTheme_NoActionBar)
        window.setBackgroundDrawableResource(R.color.colorWhite)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()

    }

}
