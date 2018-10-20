package com.example.listfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 *
 */
class MyListFragment : ListFragment() {

    var textView: TextView? = null

    var list = Array(16, {"데이터 ${it}"})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_list, container, false)

        textView = view.findViewById<TextView>(R.id.textView)

        var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list)
        listAdapter = adapter

        return view
    }


    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        var str = list[position]
        textView?.text = str

    }


}
