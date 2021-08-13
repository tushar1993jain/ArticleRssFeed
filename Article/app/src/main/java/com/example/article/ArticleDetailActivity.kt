package com.example.article

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ArticleDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        val imageView = findViewById<ImageView>(R.id.iv_image)
        val desc = findViewById<TextView>(R.id.title)

        supportActionBar?.title = intent.getStringExtra("title")


        Picasso.with(this).load(Uri.parse(intent.getStringExtra("content"))).into(
            imageView
        )
        desc.text = intent.getStringExtra("description")
    }
}