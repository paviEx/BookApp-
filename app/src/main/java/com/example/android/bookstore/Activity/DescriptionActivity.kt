package com.example.android.bookstore.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.bookstore.Dashboard_fragment
import com.example.android.bookstore.R
import com.example.android.bookstore.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView

    lateinit var txtBookAuthor: TextView

    lateinit var txtBookPrice: TextView


    lateinit var imgBookImage: ImageView

    lateinit var txtBookDesc: TextView

    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    var bookId: String?="100"

    lateinit var toolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)

        txtBookAuthor= findViewById(R.id. txtBookAuthor)

        txtBookPrice = findViewById(R.id.txtBookPrice)
        imgBookImage= findViewById(R.id. imgBookImage)
        txtBookDesc= findViewById(R.id.txtAboutTheBookStatic)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressLayout=findViewById(R.id.progressLayout)

        progressBar.visibility = View.VISIBLE

        progressLayout.visibility =View.VISIBLE

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"


        if(intent !=null){

            bookId= intent.getStringExtra( "book_id")

        } else {

            finish()

            Toast.makeText(this@DescriptionActivity, " 1st Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
        }

        if (bookId == "100") {

            finish()
            Toast.makeText(this@DescriptionActivity, " 2nd Some unexpected error occurred!", Toast.LENGTH_SHORT).show()

        }

        val queue = Volley.newRequestQueue ( this@DescriptionActivity)

        val url="http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()

        jsonParams.put( "book_id", bookId)

        val jsonRequest = object: JsonObjectRequest(Request.Method. POST, url, jsonParams, Response.Listener {

            try {
                val success = it.getBoolean("success")

                if (success) {

                    val bookJsonObject = it.getJSONObject("book_data")
                    progressLayout.visibility = View.GONE

                    val bookImageUrl = bookJsonObject.getString("image")
                    Picasso.get().load(bookJsonObject.getString("image"))
                        .error(R.drawable.default_image).into(imgBookImage)

                    txtBookName.text = bookJsonObject.getString("name")

                    txtBookAuthor.text = bookJsonObject.getString("author")

                    txtBookPrice.text = bookJsonObject.getString("price")

                    txtBookDesc.text = bookJsonObject.getString("description")
                    val bookEntity = BookEntity(

                        bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                        txtBookAuthor.text.toString(),
                        txtBookPrice.text.toString(),
                        txtBookDesc.text.toString(),
                        bookImageUrl
                    )
                    val checkFav =
                        Dashboard_fragment.DBAsyncTask(applicationContext, bookEntity, 1).execute()

                    val isFav = checkFav.get()

                    if (isFav) {

                        btnAddToFav.text = "Remove from Favourites"

                        val favColor =
                            ContextCompat.getColor(applicationContext, R.color.colorFavourite)

                        btnAddToFav.setBackgroundColor(favColor)

                    } else {

                        btnAddToFav.text = "Add to Favourites"
                        val noFavColor = ContextCompat.getColor(applicationContext, R.color.black)

                        btnAddToFav.setBackgroundColor(noFavColor)
                    }
                    btnAddToFav.setOnClickListener {

                        if (!Dashboard_fragment.DBAsyncTask(applicationContext, bookEntity, 1)
                                .execute().get()
                        ) {

                            val async =

                                Dashboard_fragment.DBAsyncTask(applicationContext, bookEntity, 2)
                                    .execute()

                            val result = async.get()

                            if (result) {

                                Toast.makeText(
                                    this@DescriptionActivity, "Book added to favourites",

                                    Toast.LENGTH_SHORT
                                ).show()

                                btnAddToFav.text = "Remove from favourites"

                                val favColor =
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorFavourite
                                    )

                                btnAddToFav.setBackgroundColor(favColor)

                            } else {
                                Toast.makeText(
                                    this@DescriptionActivity, "some error occured",

                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val async =
                                Dashboard_fragment.DBAsyncTask(applicationContext, bookEntity, 3)
                                    .execute()
                            val result=async.get()
                            if (result) {

                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Book removed from favourites",
                                    Toast.LENGTH_SHORT
                                ).show()

                                btnAddToFav.text = "Add to favourites"

                                val noFavColor =
                                    ContextCompat.getColor(applicationContext, R.color.black)
                                btnAddToFav.setBackgroundColor(noFavColor)

                            }else{
                                Toast.makeText(
                                    this@DescriptionActivity, "some error occured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    } else {

                    Toast.makeText(
                        this@DescriptionActivity,
                        " 3rd Some Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@DescriptionActivity,
                    "4th Some Error Occurred!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }, Response.ErrorListener{
            Toast.makeText(
                this@DescriptionActivity,
                "5th Some Error Occurred!",
                Toast.LENGTH_SHORT
            ).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "9bf534118365f1"
                return headers
            }

        }
        queue.add(jsonRequest)

    }

}