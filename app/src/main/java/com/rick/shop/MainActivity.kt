package com.rick.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rick.shop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
//    var RC_SIGNUP = 0
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
//        RC_SIGNUP = result.resultCode
        if (result.resultCode == RESULT_OK) {
            Log.d("result", "RESULT_OK")
            if (!nick){
                nicknameSave()
            }
        }
    }
//    var signup = false
    val auth = FirebaseAuth.getInstance()
    var nick = false
    val functions = listOf<String>("Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "A News",
        "Movies",
        "C News",
        "D News",
        "E News",
        "F News",
        "G News",
        "Maps",
        "Music")

/*    lateinit var mainNickname: TextView
    lateinit var spinner: Spinner
    lateinit var recycler: RecyclerView*/

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
//        findViews()
        /*val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        /*if (!signup){
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncher.launch(intent)
        }*/
        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }
        //Spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.contentMain.spinner.adapter = adapter
//        spinner.adapter = adapter
        binding.contentMain.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d("MainActivity", "onItemSelected: ${colors[position]}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        //RecyclerView

        binding.contentMain.recycler.layoutManager = LinearLayoutManager(this)
        binding.contentMain.recycler.setHasFixedSize(true)
        binding.contentMain.recycler.adapter = FunctionAdapter()
    }

    inner class FunctionAdapter(): RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)
            }
        }

    }

    private fun functionClicked(holder: FunctionHolder, position: Int) {
        Log.d(TAG, "functionClicked: $position")
        when(position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            5 -> startActivity(Intent(this, MovieActivity::class.java))
        }
    }

    class FunctionHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.findViewById(R.id.name)
    }

    override fun onResume() {
        super.onResume()
//        mainNickname.text = getNickname()
        if (auth.currentUser != null) {
            FirebaseDatabase.getInstance("https://shop-69b3d-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users")
                .child(auth.currentUser!!.uid)
                .child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        binding.contentMain.mainNickname.text = snapshot.value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncher.launch(intent)
        }else {
            Log.d("MainActivity", "authChanged: ${auth.currentUser?.uid}")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
    fun nicknameSave() {
        nick = true
        val intent = Intent(this, NicknameActivity::class.java)
        resultLauncher.launch(intent)
    }

/*    fun findViews() {
        mainNickname = findViewById(R.id.main_nickname)
        spinner = findViewById(R.id.spinner)
        recycler = findViewById(R.id.recycler)
    }*/
}