package com.example.gobang

/*
test push Xiaolin
 */

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
const val  MAX_COUNT_IN_LINE = 5
const val MAX_LINE = 15
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.action_setting ->{
                findViewById<chessboardView>(R.id.boardView).playAgain()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
