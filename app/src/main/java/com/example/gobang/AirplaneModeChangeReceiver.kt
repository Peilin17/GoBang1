package com.example.gobang

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeChangeReceiver(val mainActivity: MainActivity?=null) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.getBooleanExtra("state", false))
        {
            //下载按钮变灰
            Toast.makeText(context, "yeah!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            //颜色回复
        }




    }
}