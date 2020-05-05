package com.example.gobang


//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
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
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.gobang.com.example.gobang.AIChessboardView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.URL
import java.util.*


const val MAX_COUNT_IN_LINE = 5
const val MAX_LINE = 15
private var isbattlemode = true
//private var Path = "https://vignette.wikia.nocookie.net/naruto/images/0/09/Naruto_newshot.png"
private var Path = ""
private var model: MyViewModel? = null


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")
        val factory = Factory()
        model = ViewModelProvider(this, factory).get(MyViewModel::class.java)
        val urltext: EditText = findViewById(R.id.background_inputurl)
        urltext.isVisible = false
        download_button.isVisible = false
        val back_spinner: Spinner = findViewById(R.id.background_spinner)


        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    if (intent.getBooleanExtra("state", false)) {
                        findViewById<Button>(R.id.download_button).setBackgroundColor(Color.GRAY)
                    } else {
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




        //download button
        download_button.setOnClickListener {
            Path = urltext.text.toString()
            urltext.isVisible = false
            download_button.isVisible = false
            back_spinner.setSelection(0)

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
                val externalStorageState = Environment.getExternalStorageState()
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                    DonwloadSaveImg.donwloadImg(this, Path)
                    pickImageFromGallery();
                }
            }
        }


        /*
        spinner for changing background
         */
        ArrayAdapter.createFromResource(
            this,
            R.array.backgroundselection_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            back_spinner.adapter = adapter
        }

        back_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = parent?.getItemAtPosition(position).toString()
                if (selected == "select local picture") {
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
                        back_spinner.setSelection(0)
                    } else {
                        //system OS is < Marshmallow
                        pickImageFromGallery();
                    }
                } else if (selected == "use picture online") {
                    urltext.isVisible = true
                    download_button.isVisible = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
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
        const val ROUTE = "0ccd-4a3d-9d01"

        const val TAG = "GoBang_TAG"

        const val USERNAME = "GoBang"

        //val COMPLETE_INTENT = "complete intent"
        //val MUSICNAME = "music name"
        fun event(color: String, x: Int, y: Int) {
            model?.appendEvent(color, x, y)
        }

    }

    var diaListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { dialog, buttonId -> // TODO Auto-generated method stub
            when (buttonId) {
                AlertDialog.BUTTON_POSITIVE -> finish()
                AlertDialog.BUTTON_NEGATIVE -> {
                }
                else -> {
                }
            }
        }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //back key Constant Value: 4 (0x00000004)
            //创建退出对话框
            val isExit: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            //设置对话框标题
            isExit.setTitle("Message")
            //设置对话框消息
            isExit.setMessage("You want to quit?")
            // 添加选择按钮并注册监听
            isExit.setPositiveButton("Yes", diaListener)
            isExit.setNegativeButton("No", diaListener)
            //对话框显示
            isExit.show()
        }
        return false
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
                    Thread.sleep(1000L)
                    pickImageFromGallery();
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
    private var mSaveMessage = "Failed ! ! ! Please input the correct URL"
    private const val TAG = "PictureActivity"
    private var mSaveDialog: ProgressDialog? = null
    fun donwloadImg(contexts: Context?, filePaths: String?) {
        context = contexts
        filePath = filePaths
        mSaveDialog =
            ProgressDialog.show(
                context,
                "Save image",
                "Please wait, downloading image to phone",
                true
            )
        Thread(saveFileRunnable).start()
    }

    private val saveFileRunnable = Runnable {
        try {
            if (!TextUtils.isEmpty(filePath)) { //online image
                // 对资源链接
                val url = URL(filePath)
                //打开输入流
                val inputStream: InputStream = url.openStream()
                //对网上资源进行下载转换位图图片
                mBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            }
            saveFile(mBitmap)
            mSaveMessage = "Image saved ! ! !"
        } catch (e: IOException) {
            mSaveMessage = "Fail to download image ! ! !"
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
     * save image
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
        //broadcast the new image is here
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri: Uri = Uri.fromFile(myCaptureFile)
        intent.data = uri
        context?.sendBroadcast(intent)
    }


}
