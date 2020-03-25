package com.example.gobang

/*
test push Xiaolin
 */

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.example.gobang.com.example.gobang.AIChessboardView
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.os.Build
import android.os.Build.*

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

        // change background button
        changebackground_button.setOnClickListener {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            background_imageview.setImageURI(data?.data)
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
