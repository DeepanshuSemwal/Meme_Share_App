package com.example.memeshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.util.Queue
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    private val memeUrls: Queue<String> = LinkedList<String>()
    private val preloadCount: Int = 10
    private var currentUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
        val btnNext=findViewById<Button>(R.id.next)
        btnNext.setOnClickListener {
            next()
        }
        val btnShare:Button=findViewById(R.id.buttonshare)
        btnShare.setOnClickListener {
            share()
        }
    }
    
    private fun setMeme()
    {
        if (memeUrls.size > 0 && currentUrl != memeUrls.peek()){
            val imageview:ImageView = findViewById(R.id.imageView)
            currentUrl = memeUrls.peek()
            Glide.with(this).load(currentUrl).into(imageview)
        }
    }
    
    private fun loadMeme()
    {
        val progressbar:ProgressBar=findViewById(R.id.progress_bar)
        if (memeUrls.size < 1){
            progressbar.visibility=View.VISIBLE
        }
        
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                val newMemeUrl: String = response.getString("url")

                if (!memeUrls.contains(newMemeUrl))
                {
                    memeUrls.add(newMemeUrl)
                    progressbar.visibility=View.GONE
                }
                
                setMeme()

                if (memeUrls.size < preloadCount){
                    loadMeme()
                }
            },

            { _ ->
                val snackBar: Snackbar = Snackbar.make(this.window.decorView.rootView, "Unable to fetch meme", Snackbar.LENGTH_INDEFINITE);
                snackBar.setAction("Retry", View.OnClickListener {
                    loadMeme()
                })
                snackBar.show()
                progressbar.visibility=View.GONE
            })
        queue.add(jsonObjectRequest)
    }
        
    private fun share()
    {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, currentUrl)
        val chooser=Intent.createChooser(intent,"Share this meme using ...")
        startActivity(chooser)
    }

    private fun next()
    {
        memeUrls.poll()
        setMeme()
        loadMeme()
    }
}
