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

class MainActivity1 : AppCompatActivity(), CellClickListener {

    val RSS_FEED_LINK = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,28";
    var adapter: MyItemRecyclerViewAdapter? = null
    var rssItems = ArrayList<Item>()
    var listV : RecyclerView?= null
    lateinit var imageView: ImageView
    lateinit var title: TextView
    lateinit var desc : TextView
    lateinit var details: TextView
    lateinit var creator: TextView
    lateinit var rateLayout: LinearLayout
    lateinit var progressBar: ProgressBar

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_article)
    setSupportActionBar(findViewById(R.id.my_toolbar))
    listV = findViewById(R.id.recycler_view)
    imageView = findViewById(R.id.iv_image)
    title = findViewById(R.id.title)
    desc = findViewById(R.id.description)
    details = findViewById(R.id.details)
    creator = findViewById(R.id.creator)
    progressBar = findViewById(R.id.progressBar)
    rateLayout = findViewById(R.id.rateLayout)

    adapter = MyItemRecyclerViewAdapter(this, rssItems, this)
    listV?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    listV?.adapter = adapter
    fetchDataFromRss()



}

    fun fetchDataFromRss(){
        progressBar.visibility = View.VISIBLE
        val url = URL(RSS_FEED_LINK)
        RssFeedFetcher(this).execute(url)
    }
    fun updateRV(rssItemsL: List<Item>) {
        rateLayout.visibility = View.VISIBLE
        if (rssItemsL != null && !rssItemsL.isEmpty()) {
            rssItems.addAll(rssItemsL)
            updateFirstArticle(rssItemsL[0])
            rssItems.removeAt(0)
            adapter?.notifyDataSetChanged()
        }
        progressBar.visibility = View.GONE

    }

    fun updateFirstArticle(rssItem: Item){
        val transformation: Transformation = RoundedTransformation(15, 10)
        Picasso.with(this).load(Uri.parse(rssItem.content)).transform(transformation).into(
            imageView
        )
        title.text = rssItem.title
        desc.text = rssItem.description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            details.text =  Html.fromHtml(rssItem.encoded, Html.FROM_HTML_MODE_LEGACY);
        } else {
            details.text = Html.fromHtml(rssItem.encoded)
        }
        creator.text = "By "+ rssItem.creator

    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_refresh -> {
            // User chose the "Settings" item, show the app settings UI...
            rssItems = ArrayList()
            fetchDataFromRss()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onCellClickListener(position: Int) {

        val intent1 = Intent(this, ArticleDetailActivity::class.java)
        intent1.putExtra("title", rssItems[position].title)
        intent1.putExtra("content", rssItems[position].content)
        intent1.putExtra("description", rssItems[position].description)
        startActivity(intent1)
    }

}

class RssFeedFetcher(val context: MainActivity1) : AsyncTask<URL, Void, List<Item>>() {
    val reference = WeakReference(context)
    private var stream: InputStream? = null;
    override fun doInBackground(vararg params: URL?): List<Item>? {
        val connect = params[0]?.openConnection() as HttpURLConnection
        connect.readTimeout = 8000
        connect.connectTimeout = 8000
        connect.requestMethod = "GET"
        connect.connect();
        val responseCode: Int = connect.responseCode;
        var rssItems: List<Item>? = null
        if (responseCode == 200) {
            stream = connect.inputStream;
            try {
                val parser = RssParser()
                rssItems = parser.parse(stream!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return rssItems
    }
    override fun onPostExecute(result: List<Item>?) {
        super.onPostExecute(result)
        if (result != null && !result.isEmpty()) {
            reference.get()?.updateRV(result)
        }
    }
}

/*Transformation for rounded corners
* @radius : corner radius in int
* @margin: margin in int*/
class RoundedTransformation(
    private val radius: Int, // dp
    private var margin: Int
) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(
            RectF(
                margin.toFloat(),
                margin.toFloat(),
                (source.width - margin).toFloat(),
                (source.height - margin).toFloat()
            ), radius.toFloat(), radius.toFloat(), paint
        )
        if (source != output) {
            source.recycle()
        }
        return output
    }

    override fun key(): String {
        return "rounded"
    }
}


interface CellClickListener {
    fun onCellClickListener(position: Int)
}