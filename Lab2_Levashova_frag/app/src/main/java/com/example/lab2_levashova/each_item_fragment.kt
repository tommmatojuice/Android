package com.example.lab2_levashova

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

class each_item_fragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_each_item_fragment, container, false)

        val position = arguments?.getString("POSITION")?.toInt()
        val android = DataStorage.getVersionsList()[position!!]

        view.findViewById<ImageView>(R.id.avatar).setImageResource(android.imageAndroid)
        view.findViewById<TextView>(R.id.title).setText(android.title)
        view.findViewById<TextView>(R.id.data).setText(android.data)
        view.findViewById<TextView>(R.id.textView3).setText(android.text)
        view.findViewById<Button>(R.id.click_btn).setOnClickListener{goToYouTube(android.url)}
        return view
    }

    private fun goToYouTube(url: String) {
        val webView = view?.findViewById<WebView>(R.id.webview)
        webView?.loadUrl(url)
    }

    fun newInstance(position: String): each_item_fragment{
        val fragment = each_item_fragment()
        val args: Bundle = Bundle()
        args.putString("POSITION", position)
        fragment.arguments = args
        return fragment
    }
}