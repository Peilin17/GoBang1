package com.example.gobang

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload user interaction history.

        val json = JSONObject()
//        json.accumulate("username",inputData.getString("username"))
//        json.accumulate("event",inputData.getString("event"))
//        json.accumulate("seekBar 1",inputData.getLong("seekBar1", 0))
//        json.accumulate("seekBar 2",inputData.getLong("seekBar2", 0))
//        json.accumulate("seekBar 3",inputData.getLong("seekBar3", 0))

        Log.d(MainActivity.TAG, "params:"+json.toString()+ " url "+MainActivity.URL)
        return   uploadLog(json, MainActivity.URL)
    }

    fun uploadLog(json: JSONObject, url: String): Result {
        Log.d(MainActivity.TAG, "uploadLog() "+url)
        var call =
            TrackerRetrofitService.create(url).postLog(json)

        var response = call.execute()

        if(response.isSuccessful){
            return Result.success()

        }
        else{
            if (response.code() in (500..599)) {
                // try again if there is a server error
                return Result.retry()
            }
            Log.d(MainActivity.TAG, "response is:  "+response)
            return Result.failure()
        }

    }
}