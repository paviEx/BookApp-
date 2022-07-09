package com.example.android.bookstore

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.bookstore.database.BookDatabase
import com.example.android.bookstore.database.BookEntity
import com.example.android.bookstore.model.Book
import com.example.android.bookstore.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class Dashboard_fragment : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var recyclerAdapter:DashboardRecyclerAdapter
     var bookInfoList= arrayListOf<Book>()
    var priceComparator=Comparator<Book>{book1,book2->
        if(book1.bookPrice.compareTo(book2.bookPrice,true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookPrice.compareTo(book2.bookPrice,true)
        }
    }

    /*  var bookInfoList = arrayListOf<Book>(
          Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", 77777,777),
          Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
          Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
          Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
          Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
          Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
          Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
          Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
          Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
          Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
      )*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this com.example.bookhub.com.example.bookhub.adapter.adapter.fragment
        val view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false)
        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerdashboard)

        layoutManager = LinearLayoutManager(activity)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility= View.VISIBLE



        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().CheckConnectivity(activity as Context)){
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    // Here we will handle the respose
                    val success = it.getBoolean("success")
                    try{
                        progressLayout.visibility= View.GONE
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager

                                recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )


                            }


                        } else {
                            Toast.makeText(activity as Context, "Some ERROR OCCURED", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }catch (e:JSONException){
                        Toast.makeText(activity as Context, "Some ERROR OCCURED", Toast.LENGTH_SHORT)
                            .show()

                    }



                }, Response.ErrorListener {
                    // here we handel the error
                    if(activity!=null) {
                        Toast.makeText(
                            activity as Context,
                            "Volly ERROR OCCURED",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }

                }

            queue.add(jsonObjectRequest)


        }else{
            val dialog =AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Open settings"){text,listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit") {text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view
    }

    class DBAsyncTask (val context: Context, val bookEntity: BookEntity, val mode: Int): AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 Check DB if the book is favourite or not

    Mode 2 -> Save the book into DB as favourite

    Mode 3 -> Remove the favourite book
    **/
        val db = Room.databaseBuilder (context, BookDatabase::class.java, "books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){
                1->{
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())

                    db.close()

                    return book != null

                }
                2->{
                    db.bookDao().insertBook (bookEntity)
                    db.close()
                    return true


                }
                3->{
                    db.bookDao ().deleteBook (bookEntity)
                    db.close()
                    return true

                }
            }

            return false
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item?.itemId
        if (id == R.id.action_sort) {
            Collections.sort (bookInfoList, priceComparator)
            bookInfoList. reverse()

    }
    recyclerAdapter.notifyDataSetChanged()
    return super.onOptionsItemSelected(item)
    }
}
