package com.example.githubuserapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var rvUsers: RecyclerView
    private var list: ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        showAnimation()

        getJSON(this)?.let { readJSON(it) }

        showRecyclerView()
    }

    private fun showAnimation() {
        var isOpen = false

        val fabMain: FloatingActionButton = findViewById(R.id.fab_0)
        val fab1Goto: ExtendedFloatingActionButton = findViewById(R.id.fab_1)
        val fab2Info: ExtendedFloatingActionButton = findViewById(R.id.fab_2)
        val fab3Up: ExtendedFloatingActionButton = findViewById(R.id.fab_3)

        val fabClose: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.close)
        val fabOpen: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.open)
        val fabLeft: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.left_rotate)
        val fabRight: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.right_rotate)

        fabMain.setOnClickListener {
            if (isOpen) {
                fab1Goto.startAnimation(fabClose)
                fab2Info.startAnimation(fabClose)
                fab3Up.startAnimation(fabClose)
                fabMain.startAnimation(fabLeft)
                fab1Goto.isClickable = false
                fab2Info.isClickable = false
                fab3Up.isClickable = false
                isOpen = false
            } else {
                fab1Goto.startAnimation(fabOpen)
                fab2Info.startAnimation(fabOpen)
                fab3Up.startAnimation(fabOpen)
                fabMain.startAnimation(fabRight)
                fab1Goto.isClickable = true
                fab2Info.isClickable = true
                fab3Up.isClickable = true
                isOpen = true
            }
        }

        fab1Goto.setOnClickListener {
            fab1Goto.setIconTintResource(R.color.colorAccent)
            fab1Goto.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))

            val goTo = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/"))
            startActivity(goTo)
        }

        fab2Info.setOnClickListener {
            fab1Goto.startAnimation(fabClose)
            fab2Info.startAnimation(fabClose)
            fab3Up.startAnimation(fabClose)
            fabMain.startAnimation(fabLeft)
            fab1Goto.isClickable = false
            fab2Info.isClickable = false
            fab3Up.isClickable = false
            isOpen = false

            val info = Intent(this@MainActivity, InfoActivity::class.java)
            startActivity(info)
        }

        fab3Up.setOnClickListener {
            fab1Goto.startAnimation(fabClose)
            fab2Info.startAnimation(fabClose)
            fab3Up.startAnimation(fabClose)
            fabMain.startAnimation(fabLeft)
            fab1Goto.isClickable = false
            fab2Info.isClickable = false
            fab3Up.isClickable = false
            isOpen = false

            rvUsers.post {
                rvUsers.smoothScrollToPosition(0)
            }
        }
    }

    private fun getJSON(context: Context): String? {
        val jsonString: String

        try {
            jsonString = context.assets.open("githubuser.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun readJSON(response: String) {
        val objectJSON = JSONObject(response)
        val userArray = objectJSON.getJSONArray("users")

        for (i in 0 until userArray.length()) {
            val gson = Gson()
            val data = gson.fromJson(userArray.getJSONObject(i).toString(), User::class.java)

            list.add(data)
        }
    }

    private fun showRecyclerView() {
        rvUsers.layoutManager = LinearLayoutManager(this)
        val cardViewAdapter = Adapter(list)
        rvUsers.adapter = cardViewAdapter
    }
}