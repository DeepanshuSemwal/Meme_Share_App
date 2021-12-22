package com.example.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentimageurl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
        val btn_next=findViewById<Button>(R.id.next)
        btn_next.setOnClickListener {
            next()
        }
        val btn_share:Button=findViewById(R.id.buttonshare)
        btn_share.setOnClickListener {
            share()
        }
    }
    private fun loadmeme()
    {
        // Instantiate the RequestQueue

        val progressbar:ProgressBar=findViewById(R.id.progress_bar)
        progressbar.visibility=View.VISIBLE
        val imageview:ImageView=findViewById(R.id.imageView)
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                currentimageurl=response.getString("url")
                Glide.with(this).load(currentimageurl).into(imageview)
                progressbar.visibility=View.GONE

            },

            { error ->
                // TODO: Handle error
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            })
        queue.add(jsonObjectRequest)

    }
    private fun share()
    {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this meme : $currentimageurl")
        val chooser=Intent.createChooser(intent,"Share this meme suing ...")
        startActivity(chooser)
    }

    private fun next()
    {
        loadmeme()
    }
}
