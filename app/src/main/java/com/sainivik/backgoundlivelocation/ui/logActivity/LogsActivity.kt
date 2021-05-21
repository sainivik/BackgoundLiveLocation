package com.sainivik.backgoundlivelocation.ui.logActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.adapter.LocationLogsAdapter
import com.sainivik.backgoundlivelocation.databinding.ActivityLogsBinding
import com.sainivik.backgoundlivelocation.interfaces.RecyclerClickListener
import com.sainivik.backgoundlivelocation.model.LocationTable
import com.sainivik.backgoundlivelocation.ui.base.BaseActivity
import com.sainivik.backgoundlivelocation.ui.mapActivity.MapActivity
import com.sainivik.backgoundlivelocation.util.EventTask
import com.sainivik.backgoundlivelocation.util.Status

class LogsActivity : BaseActivity() {
    lateinit var binding: ActivityLogsBinding
    lateinit var viewModel: LogsActivityViewModel
    lateinit var adapter: LocationLogsAdapter
    var list: ArrayList<LocationTable> = ArrayList<LocationTable>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logs)
        setAdapter()
    }

    /*setting adapter to sow logs*/
    private fun setAdapter() {
        adapter = LocationLogsAdapter(list, object
            : RecyclerClickListener {
            override fun click(view: View, position: Int) {
                var temp: ArrayList<LocationTable> = ArrayList<LocationTable>()
                temp.add(list[position])
                var intent = Intent(this@LogsActivity, MapActivity::class.java)
                intent.putExtra("data", temp)
                startActivity(intent)
            }
        })
        binding.adapter = adapter
    }

    override fun attachViewModel() {
        viewModel = ViewModelProvider(this).get(LogsActivityViewModel::class.java)
        /*getting location logs data from DB*/
        viewModel.getLocationToLocalDB(this@LogsActivity)
        viewModel.response.observe(
            this,
            androidx.lifecycle.Observer { parseDBList(it) })
    }

    private fun parseDBList(eventTask: EventTask<Any>) {

        when (eventTask.status) {

            Status.LOADING -> {
                binding.showProgress = true
            }
            Status.SUCCESS -> {
                binding.showProgress = false

                if (eventTask.data != null) {
                    var dataList: List<LocationTable> = eventTask.data as List<LocationTable>
                    if (dataList != null) {
                        list.clear()
                        list.addAll(dataList)
                        adapter.notifyDataSetChanged()
                    }
                }

            }
            Status.ERROR -> {
                binding.showProgress = false

            }
        }

    }

}