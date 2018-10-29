package com.example.contactapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class MyAdaptor(realmResult: OrderedRealmCollection<Contact>) : RealmBaseAdapter<Contact>(realmResult) {


    private lateinit var context: Context

    private lateinit var layoutInflater: LayoutInflater

    private lateinit var isCheckedConfirm: Array<Boolean>


    constructor(context: Context, realmResult: OrderedRealmCollection<Contact>) : this(realmResult) {
        this.context = context
        this.layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.isCheckedConfirm = Array(realmResult.size, {false})
    }

    private inner class ViewHolder(view: View) {
        val txtName: TextView
        val txtPhone: TextView
        val txtEmail: TextView
        val checkBox: CheckBox

        init {
            this.txtName = view.findViewById(R.id.txtName)
            this.txtPhone = view.findViewById(R.id.txtPhoneNumber)
            this.txtEmail = view.findViewById(R.id.txtEmail)
            this.checkBox = view.findViewById(R.id.checkBox)
        }

    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.d(javaClass.name, "isCheckedConfirm size ${isCheckedConfirm.size}")
        Log.d(javaClass.name, "adapterData size ${adapterData?.size ?: 0}")
        val itemView: View =
            if (convertView == null) {
                layoutInflater.inflate(R.layout.item_layout, null)
            } else {
                convertView
            }

        var viewHolder: ViewHolder? = null

        if(convertView == null) {
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        }
        else {
            viewHolder = itemView.tag as ViewHolder
        }

        var txtName: TextView? = viewHolder.txtName
        var txtPhone: TextView? = viewHolder.txtPhone
        var txtEmail: TextView? = viewHolder.txtEmail
        var checkBox: CheckBox? = viewHolder.checkBox

        if(adapterData != null) {
            var item = adapterData!![position]
            txtName?.text = item.name
            txtPhone?.text = item.phone
            txtEmail?.text = item.email
            checkBox?.isChecked = isCheckedConfirm[position]

            checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isCheckedConfirm.size > position) {
                    isCheckedConfirm[position] = isChecked
                }
            }
        }

        return itemView
    }


    fun resetCheckStats(checked: Boolean) {
        this.isCheckedConfirm = Array(adapterData?.size ?: 0, {false})
        isCheckedConfirm.forEachIndexed { index, b ->
            isCheckedConfirm[index] = checked
        }
    }


    override fun getItem(position: Int): Contact? {
        if( adapterData != null) {
            return adapterData!![position]
        }
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun checkedItemPositions(): List<Int> {
        val res = ArrayList<Int>()
        isCheckedConfirm.forEachIndexed { index, b ->
            if (b) {
                res.add(index)
            }
        }
        return res
    }

}

