package com.example.lab2_levashova

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

//class each_item_fragment : Fragment()
class EachItemFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_each_item_fragment, container, false)
        val android = arguments?.get("ANDROID") as Android

        view.findViewById<ImageView>(R.id.avatar).setImageResource(android.imageAndroid)
        view.findViewById<TextView>(R.id.title).text = android.title
        view.findViewById<TextView>(R.id.data).text = android.data
        view.findViewById<TextView>(R.id.textView3).text = android.text
        view.findViewById<Button>(R.id.click_btn).setOnClickListener{goToYouTube(android.url)}
        return view
    }

    private fun goToYouTube(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun newInstance(android: Android?): EachItemFragment
    {
        val fragment = EachItemFragment()
        val args: Bundle = Bundle()
        args.putParcelable("ANDROID", android)
        fragment.arguments = args
        return fragment
    }
}

/*class FragmentAdapter(
        fragmentManager: FragmentManager,
        private val arrayList: List<Android>
) : FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment? {
        val versions = when {
            arrayList.isNotEmpty() && position <= count - 1 -> arrayList[position]
            else -> null
        }
        return versions?.run { DetailsFragment.newInstance(this) }
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}
*/