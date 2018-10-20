package com.example.activitycontroller


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 *
 */
class ResultFragment : Fragment() {

    var button: Button? = null

    var textView1: TextView? = null

    var textView2: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_result, container, false)

        button = view.findViewById(R.id.button2)
        textView1 = view.findViewById(R.id.textView1)
        textView2 = view.findViewById(R.id.textView2)

        var mainActivity = activity as MainActivity

        textView1?.text = mainActivity.value1
        textView2?.text = mainActivity.value2

        button?.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }

        return view
    }


}
