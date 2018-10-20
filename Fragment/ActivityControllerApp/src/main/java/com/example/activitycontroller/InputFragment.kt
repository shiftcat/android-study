package com.example.activitycontroller


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


/**
 * A simple [Fragment] subclass.
 *
 */
class InputFragment : Fragment() {

    var button: Button? = null

    var edit1: EditText? = null

    var edit2: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_input, container, false)

        button = view.findViewById(R.id.button)
        edit1 = view.findViewById(R.id.editText1)
        edit2 = view.findViewById(R.id.editText2)

        button?.setOnClickListener {
            var mainActivity = activity as MainActivity

            mainActivity.value1 = edit1?.text.toString()
            mainActivity.value2 = edit2?.text.toString()

            mainActivity.setFragment("result")
        }

        return view
    }


}
