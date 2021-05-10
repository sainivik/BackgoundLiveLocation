package com.sainivik.backgoundlivelocation.ui.mainactivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sainivik.backgoundlivelocation.model.LocationTable
import com.sainivik.backgoundlivelocation.database.MyAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {


    fun saveLocationToLocalDB(loc: LocationTable, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            MyAppDatabase.getInstance(context).getLocationMaster().insert(loc)
            withContext(Dispatchers.Main) {
                Log.e("SaveLocationToLocalDB", "success")
            }
        }

    }
}