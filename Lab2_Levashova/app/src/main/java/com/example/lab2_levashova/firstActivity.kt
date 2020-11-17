package com.example.lab2_levashova

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class firstActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.each_item)
        val android = intent.extras?.get("Android") as Android

        findViewById<ImageView>(R.id.avatar).setImageResource(android.imageAndroid)
        findViewById<TextView>(R.id.title).setText(android.title)
        findViewById<TextView>(R.id.data).setText(android.data)
        findViewById<TextView>(R.id.textView3).setText(android.text)
        findViewById<Button>(R.id.click_btn).setOnClickListener{goToYouTube(android.url)}

//        val imageView: ImageView = findViewById(R.id.avatar)
//        imageView.setImageResource(android.imageAndroid)
//
//        val title: TextView = findViewById(R.id.title)
//        title.setText(android.title)
//
//        val date: TextView = findViewById(R.id.data)
//        date.setText(android.data)
//
//        val text: TextView = findViewById(R.id.textView3)
//        text.setText(android.text)
//
//        val button: Button = findViewById(R.id.click_btn)
//        button.setOnClickListener{goToYouTube(android.url)}

    }

    private fun goToYouTube(url: String) {
        val webView: WebView = findViewById(R.id.webview)
        webView.loadUrl(url)
    }
}