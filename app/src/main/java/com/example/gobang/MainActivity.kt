package com.example.gobang

/*
test push Xiaolin
 */

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.example.gobang.com.example.gobang.AIChessboardView
import kotlinx.android.synthetic.main.activity_main.*

const val MAX_COUNT_IN_LINE = 5
const val MAX_LINE = 15
private var isbattlemode = true

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isbattlemode) {
            battlemode_button.setTextColor(Color.RED)
            aimode_button.setTextColor(Color.BLACK)
            boardView.visibility = View.VISIBLE
            boardView2.visibility = View.GONE
        } else {
            aimode_button.setTextColor(Color.RED)
            battlemode_button.setTextColor(Color.BLACK)
            boardView2.visibility = View.VISIBLE
            boardView.visibility = View.GONE
            findViewById<AIChessboardView>(R.id.boardView2).playAgain()
        }

        // AI mode
        aimode_button.setOnClickListener {
            aimode_button.setTextColor(Color.RED)
            battlemode_button.setTextColor(Color.BLACK)

            isbattlemode = false
            boardView2.visibility = View.VISIBLE
            boardView.visibility = View.GONE
            findViewById<AIChessboardView>(R.id.boardView2).switchModel()
        }

        // battle mode
        battlemode_button.setOnClickListener {
            battlemode_button.setTextColor(Color.RED)
            aimode_button.setTextColor(Color.BLACK)

            isbattlemode = true
            boardView.visibility = View.VISIBLE
            boardView2.visibility = View.GONE
            findViewById<chessboardView>(R.id.boardView).switchModel()
        }

        // restart button
        restart_button.setOnClickListener {
            if (isbattlemode) {
                boardView.visibility = View.VISIBLE
                boardView2.visibility = View.GONE
                findViewById<chessboardView>(R.id.boardView).playAgain()
            } else {
                boardView2.visibility = View.VISIBLE
                boardView.visibility = View.GONE
                findViewById<AIChessboardView>(R.id.boardView2).playAgain()
            }
        }

        // regret button
        regret_button.setOnClickListener {
            if (isbattlemode) {
                findViewById<chessboardView>(R.id.boardView).regretPlay()
            } else {
                findViewById<AIChessboardView>(R.id.boardView2).regretPlay()
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.action_menu, menu)
//
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//
//        when (item.itemId) {
//            R.id.action_setting ->{
//                boardView2.visibility = View.GONE
//                boardView.visibility = View.VISIBLE
//                findViewById<chessboardView>(R.id.boardView).playAgain()
//            }
//
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
