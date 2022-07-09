package com.example.android.bookstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView

import android.widget.LinearLayout

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.android.bookstore.R
import com.example.android.bookstore.database.BookEntity
import com.example.android.bookstore.model.Book
import com.squareup.picasso.Picasso


class FavouriteRecyclerAdapter(val context: Context,val bookList: List<BookEntity>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder> () {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.favbookName)

        val txtBookAuthor: TextView= view.findViewById(R.id.favauthor)

        val txtBookPrice: TextView =view.findViewById(R.id.favprice)

        val imgBookImage: ImageView = view.findViewById(R.id.favimage)

        val llContent: LinearLayout = view.findViewById(R.id.llFavcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
      val view=LayoutInflater.from(parent.context)
          .inflate(R.layout.recycler_favorite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }


    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
            val book=bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor. text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
         Picasso.get().load(book.bookImage) .error (R.drawable.default_image). into (holder.imgBookImage)
    }

    override fun getItemCount(): Int {
      return bookList.size
    }
}