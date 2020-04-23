package com.example.gobang

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
class MyViewModel: ViewModel(){
    init {

    }
    companion object{

    }
    fun appendEvent(color: String, x: Int, y: Int){

        WorkManager.getInstance().beginUniqueWork(
            MainActivity.TAG, ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<UploadWorker>().setInputData(
                    workDataOf("username" to MainActivity.USERNAME, "color" to color, "x" to x, "y" to y)
                )
                .build()).enqueue()
    }

}

