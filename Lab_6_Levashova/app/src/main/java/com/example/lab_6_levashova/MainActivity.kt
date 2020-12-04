package com.example.lab_6_levashova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity()
{
    private val theImageAPIService = NetworkModule.theImageAPIService

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

        imagesAdapter = ImagesAdapter(this)
        imagesList.adapter = imagesAdapter

        progressBar = findViewById(R.id.progress_bar)
    }

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
                    imagesAdapter.addImages(images)
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