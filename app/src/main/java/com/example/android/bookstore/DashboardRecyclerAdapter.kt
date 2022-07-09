package com.example.android.bookstore

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bookstore.model.Book
import com.squareup.picasso.Picasso
import androidx.fragment.app.Fragment
import com.example.android.bookstore.Activity.DescriptionActivity

class DashboardRecyclerAdapter(val context: Context, val itemList:ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val Book = itemList[position]

        holder.txtBookName.text = Book.bookName
        holder.txtBookAuthor.text = Book.bookAuthor
        holder.txtBookPrice.text = Book.bookPrice
        //holder.imgBookImage.setImageResource(Book.bookImage)
        Picasso.get().load(Book.bookImage).error(R.drawable.default_image).into(holder.imgBookImage)
        holder.llcontent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", Book.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtBookName: TextView = view.findViewById(R.id.bookName)

        val txtBookAuthor: TextView = view.findViewById(R.id.author)

        val txtBookPrice: TextView = view.findViewById(R.id.price)

        val imgBookImage: ImageView = view.findViewById(R.id.firstimage)

        val llcontent: RelativeLayout = view.findViewById(R.id.llcontent)

    }
}