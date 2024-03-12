package com.rick.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ParkingActivity : AppCompatActivity() {
    private val TAG = ParkingActivity::class.java.simpleName
    lateinit var info : TextView

    val parking = "https://api.kcg.gov.tw/api/service/get/897e552a-2887-4f6f-a6ee-709f7fbe0ee3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        findViews()
        parkingTask()
    }

    private fun parkingTask() = GlobalScope.launch(Dispatchers.Main) {
//        val parking = "https://api.kcg.gov.tw/api/service/get/897e552a-2887-4f6f-a6ee-709f7fbe0ee3"
        val url = URL(parking)
        var json: String
        withContext(Dispatchers.IO) {
            val jsonDownload = url.readText()
            json = jsonDownload
            Log.d(TAG, "parkingTask: $jsonDownload")

        }.run {
            Log.d(TAG, "parkingTask: $json")
            Toast.makeText(this@ParkingActivity, "Got it.", Toast.LENGTH_LONG).show()
            info.text = json
            AlertDialog.Builder(this@ParkingActivity)
                .setTitle("Alert")
                .setMessage("Got it")
                .setPositiveButton("ok") {dialog, which ->
                    parseGson(json)
                }
                .show()
        }
    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        Log.d(TAG, "parseGson: ${parking.data.size}")
        parking.data.forEach {
            Log.d(TAG, "parseGson: ${it.seq} ${it.行政區} ${it.臨時停車處所} ${it.可提供小型車停車位} ${it.地址}")
        }
    }

    private fun findViews() {
        info = findViewById(R.id.info)
    }
}

/*
{"isImage":false,"data":[
    {
        "seq":1,
        "行政區":"大社區",
        "臨時停車處所":"老人文康活動中心",
        "可提供小型車停車位":"10",
        "地址":"金龍路65號"
    }
  ]
}
 */

/*
class Parking(val data: List<ParkingLot>)

data class ParkingLot(
    val seq : String,
    val 行政區 : String,
    val 臨時停車處所 : String,
    val 可提供小型車停車位 : Int
)*/

data class Parking(
    val `data`: List<Data>,
    val isImage: Boolean
)

data class Data(
    val seq: Int,
    val 可提供小型車停車位: String,
    val 地址: String,
    val 臨時停車處所: String,
    val 行政區: String
)