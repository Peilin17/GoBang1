package com.example.gobang

/*
test push Xiaolin
 */

//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.gobang.com.example.gobang.AIChessboardView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.URL
import java.util.*


const val MAX_COUNT_IN_LINE = 5
const val MAX_LINE = 15
private var isbattlemode = true
private var Path = "https://s3.amazonaws.com/appsdeveloperblog/Micky.jpg"
private var model: MyViewModel? = null


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")
        val factory = Factory()
        model = ViewModelProvider(this, factory).get(MyViewModel::class.java)
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    if (intent.getBooleanExtra("state", false)) {
                        findViewById<Button>(R.id.download_button).setBackgroundColor(Color.GRAY)
                    }
                    else
                    {
                        findViewById<Button>(R.id.download_button).setBackgroundColor(Color.LTGRAY)
                    }
                }
            }
        }

        registerReceiver(receiver, intentFilter)

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


        download_button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        55
                    )
                }
            } else {
                // Permission has already been granted
//                Toast.makeText(this, "in 1st else", Toast.LENGTH_SHORT).show()
                val externalStorageState = Environment.getExternalStorageState()
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                    DonwloadSaveImg.donwloadImg(this, Path)
                }
            }
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
        //upload route
        const val URL = "https://posthere.io/"
        //upload route key
        const val ROUTE ="0ccd-4a3d-9d01"

        const val TAG = "GoBang_TAG"

        const val USERNAME = "GoBang"
        //val COMPLETE_INTENT = "complete intent"
        //val MUSICNAME = "music name"
        fun event(color: String, x: Int, y: Int)
        {
            model?.appendEvent(color, x, y)
        }

    }
//    fun createDialog()
//    {
//        // build alert dialog
//        val dialogBuilder = AlertDialog.Builder(this)
//
//        // set message of alert dialog
//        dialogBuilder.setMessage("Do you want to try again ?")
//            // if the dialog is cancelable
//            .setCancelable(false)
//            // positive button text and action
//            .setPositiveButton("Yes", DialogInterface.OnClickListener {
//                    dialog, id ->
//                if (isbattlemode) {
//                    boardView.visibility = View.VISIBLE
//                    boardView2.visibility = View.GONE
//                    findViewById<chessboardView>(R.id.boardView).playAgain()
//                } else {
//                    boardView2.visibility = View.VISIBLE
//                    boardView.visibility = View.GONE
//                    findViewById<AIChessboardView>(R.id.boardView2).playAgain()
//                }
//
//
//            })
//            // negative button text and action
//            .setNegativeButton("No", DialogInterface.OnClickListener {
//                    dialog, id -> dialog.cancel()
//            })
//
//        // create dialog box
//        val alert = dialogBuilder.create()
//        // set title for alert dialog box
//        alert.setTitle("Confirm")
//        // show alert dialog
//        alert.show()
//    } 已弃用 对话框






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
//        Toast.makeText(this, "requestCode is : " + requestCode, Toast.LENGTH_SHORT).show()
        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    pickImageFromGallery()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            55 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    DonwloadSaveImg.donwloadImg(this, Path)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            background_imageview.setImageURI(data?.data)
        }
    }
}


object DonwloadSaveImg {
    private var context: Context? = null
    private var filePath: String? = null
    private var mBitmap: Bitmap? = null
    private var mSaveMessage = "失败"
    private const val TAG = "PictureActivity"
    private var mSaveDialog: ProgressDialog? = null
    fun donwloadImg(contexts: Context?, filePaths: String?) {
        context = contexts
        filePath = filePaths
        mSaveDialog =
            ProgressDialog.show(context, "保存图片", "图片正在保存中，请稍等...", true)
        Thread(saveFileRunnable).start()
    }

    private val saveFileRunnable = Runnable {
        try {
            if (!TextUtils.isEmpty(filePath)) { //网络图片
                // 对资源链接
                val url = URL(filePath)
                //打开输入流
                val inputStream: InputStream = url.openStream()
                //对网上资源进行下载转换位图图片
                mBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            }
            saveFile(mBitmap)
            mSaveMessage = "图片保存成功！"
        } catch (e: IOException) {
            mSaveMessage = "图片保存失败！"
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        messageHandler.sendMessage(messageHandler.obtainMessage())
    }
    private val messageHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            mSaveDialog!!.dismiss()
            Log.d(TAG, mSaveMessage)
            Toast.makeText(
                context,
                mSaveMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 保存图片
     * @param bm
     * @throws IOException
     */
    @Throws(IOException::class)
    fun saveFile(bm: Bitmap?) {
        val dirFile = File(Environment.getExternalStorageDirectory().getPath())
        if (!dirFile.exists()) {
            dirFile.mkdir()
        }
        val fileName: String = UUID.randomUUID().toString().toString() + ".jpg"
        val myCaptureFile = File(
            Environment.getExternalStorageDirectory().getPath()
                .toString() + "/DCIM/Camera/" + fileName
        )
        val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
        bm!!.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        bos.flush()
        bos.close()
        //把图片保存后声明这个广播事件通知系统相册有新图片到来
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri: Uri = Uri.fromFile(myCaptureFile)
        intent.data = uri
        context?.sendBroadcast(intent)
    }







}
