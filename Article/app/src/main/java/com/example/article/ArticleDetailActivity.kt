package com.example.article

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

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