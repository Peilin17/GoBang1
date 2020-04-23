package com.example.gobang

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class ViewModel (application: Application): AndroidViewModel(application){
    companion object{


    }
    private fun appendEvent(color: String, x: Int, y: Int){

        WorkManager.getInstance().beginUniqueWork(
            MainActivity.TAG, ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<UploadWorker>().setInputData(
                    workDataOf("username" to MainActivity.USERNAME, "color" to color, "x" to x, "y" to y)
                )
                .build()).enqueue()
    }
}