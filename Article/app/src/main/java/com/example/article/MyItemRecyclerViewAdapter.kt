package com.example.article

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*

class MyItemRecyclerViewAdapter(
    private val context: Context,
    private val dataSet: ArrayList<Item>,
    private val cellClick: CellClickListener
) :
        RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val image: ImageView
        val card: LinearLayout

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
            image = view.findViewById(R.id.iv_image)
            card = view.findViewById(R.id.card)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_text, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Picasso.with(context).load(Uri.parse(dataSet.get(position).content)).into(viewHolder.image);
        viewHolder.textView.text = dataSet.get(position).title
        viewHolder.card.setOnClickListener {
            cellClick.onCellClickListener(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}