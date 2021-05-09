package com.sainivik.backgoundlivelocation.ui.logActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sainivik.backgoundlivelocation.util.EventTask
import com.sainivik.backgoundlivelocation.util.Task
import com.sainivik.backgoundlivelocation.database.MyAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogsActivityViewModel : ViewModel() {

    internal var response = MutableLiveData<EventTask<Any>>()

     fun getLocationToLocalDB(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
          var list=  MyAppDatabase.getInstance(context).getLocationMaster().getAllLocations()
            withContext(Dispatchers.Main) {
                response.postValue(EventTask.success(list, Task.LOCATION_FROM_DB))
            }
        }

    }

}