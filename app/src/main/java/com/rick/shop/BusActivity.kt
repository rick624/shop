package com.rick.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rick.shop.databinding.ActivityBusBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

class BusActivity : AppCompatActivity() {
    private val TAG = BusActivity::class.java.simpleName
    private lateinit var binding: ActivityBusBinding
    var buses : List<BusItem>? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_bus)
        setContentView(binding.root)
        busTask()
    }
    
    private fun busTask() = GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.IO) {
//            val json = URL("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed").readText()
//            val buses = Gson().fromJson(json, Bus::class.java).datas
//            buses = Gson().fromJson(json, Bus::class.java).datas
            val busService = retrofit.create(BusService::class.java)
            buses = busService.listBus()
                .execute()
                .body()?.datas
            buses?.forEach {
                Log.d(TAG, "busTask: ${it.BusID} ${it.RouteID} ${it.Speed}")
            }
        }.run {
            binding.recycler.layoutManager = LinearLayoutManager(this@BusActivity)
            binding.recycler.setHasFixedSize(true)
            binding.recycler.adapter = BusAdapter()
        }
    }

    inner class BusAdapter() : RecyclerView.Adapter<BusHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            val size = buses?.size?: 0
            return size
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            val bus = buses?.get(position)
            holder.bindBus(bus!!)
        }
    }

    inner class BusHolder(view: View) : RecyclerView.ViewHolder(view) {
        var busId : TextView = view.findViewById(R.id.bus_bus_id)
        var routeId : TextView = view.findViewById(R.id.bus_route_id)
        var speed : TextView = view.findViewById(R.id.bus_speed)

        fun bindBus(bus: BusItem) {
            busId.text = bus.BusID
            routeId.text = bus.RouteID
            speed.text = bus.Speed
        }
    }


}

data class Bus(
    val datas: List<BusItem>
)

data class BusItem(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)

interface BusService {
    @GET("download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
//    fun listBus(): Call<List<BusItem>>
    fun listBus(): Call<Bus>
}