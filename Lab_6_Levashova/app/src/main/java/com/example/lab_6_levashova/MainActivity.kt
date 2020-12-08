package com.example.lab_6_levashova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    private val theImageAPIService = NetworkModule.theImageAPIService

    private val mainImages = ArrayList<ImageResponse>()
    private val imagesCopy = ArrayList<ImageResponse>()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var imagesList: RecyclerView
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh)

        imagesList = findViewById(R.id.imageList)
        imagesList.setHasFixedSize(true)

        imagesCopy.addAll(mainImages)

        imagesAdapter = ImagesAdapter(this, imagesCopy)
        imagesList.adapter = imagesAdapter

        progressBar = findViewById(R.id.progress_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.images_menu, menu)
        val menuItem = menu?.findItem(R.id.search_item)

        if (menuItem != null){
            val searchView = menuItem.actionView as SearchView
            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Поиск"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {

                    if (p0!!.isNotEmpty()) {
                        Log.d("@@@@@@@@@@@@@@@@@@", "onQueryTextChange: " + 1)
                        Log.d("@@@@@@@@@@@@@@@@@@", "onQueryTextChange1: " + p0)

                        imagesCopy.clear()
                        val search = p0.toLowerCase(Locale.getDefault())
                        mainImages.forEach {

                            if (it.author.toLowerCase(Locale.getDefault()).contains(search)) {
                                imagesCopy.add(it)
                            }
                        }
                        imagesList.adapter?.notifyDataSetChanged()
                    } else {
                        imagesCopy.clear()
                        imagesCopy.addAll(mainImages)
                        imagesList.adapter?.notifyDataSetChanged()
                    }
//                    }
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.images_menu, menu)
//
////        val searchItem: MenuItem = findViewById(R.id.search_item)
////        val searchView: SearchView = searchItem?.actionView as SearchView
//
//        val menuItem = menu?.findItem(R.id.search_item)
//        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
//
//        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
//
//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false;
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                imagesAdapter.filter.filter(p0)
//                return false
//            }
//
//        })
//        return true
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.images_menu, menu)
//
//        val searchItem = menu.findItem(R.id.search_item)
//        val searchView = searchItem.actionView as SearchView
//        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                imagesAdapter.getFilter().filter(newText)
//                return false
//            }
//        })
//        return true
//    }

    override fun onResume() {
        super.onResume()

        repeat(10){
            progressBar.isVisible = true
            val imagesCall: Call<List<ImageResponse>> = theImageAPIService.getImages()
            imagesCall.enqueue(object :Callback<List<ImageResponse>>{
                override fun onResponse(
                    call: Call<List<ImageResponse>>,
                    response: Response<List<ImageResponse>>
                ) {
                    val images: List<ImageResponse> = response.body() ?: emptyList()
                    progressBar.isVisible = false
                    imagesAdapter.deleteImages()
                    imagesAdapter.addImages(images)
                    mainImages.clear()
                    mainImages.addAll(images)
                }

                override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {
                    progressBar.isVisible = false
                    Toast.makeText(this@MainActivity, "Ошибка загрузки изображения!", Toast.LENGTH_LONG).show()
                }
            })
        }

        swipeRefreshLayout.setOnRefreshListener {
            imagesAdapter.deleteImages()
            repeat(10){
                swipeRefreshLayout.isRefreshing = true
                val imagesCall: Call<List<ImageResponse>> = theImageAPIService.getImages()
                imagesCall.enqueue(object :Callback<List<ImageResponse>>{
                    override fun onResponse(
                        call: Call<List<ImageResponse>>,
                        response: Response<List<ImageResponse>>
                    ) {
                        val images: List<ImageResponse> = response.body() ?: emptyList()
                        swipeRefreshLayout.isRefreshing = false
                        imagesAdapter.addImages(images)
                    }

                    override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {
                        swipeRefreshLayout.isRefreshing = false
                        Toast.makeText(this@MainActivity, "Ошибка загрузки изображения!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}