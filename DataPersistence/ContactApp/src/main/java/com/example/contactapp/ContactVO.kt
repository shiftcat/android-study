package com.example.contactapp

import android.os.Parcel
import android.os.Parcelable

class ContactVO(): Parcelable
{

    var id: Long = -1

    var name: String = ""

    var phone: String = ""

    var email: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        phone = parcel.readString()
        email = parcel.readString()
    }

    constructor(id: Long, name: String, phone: String, email: String): this() {
        this.id = id
        this.name = name
        this.phone = phone
        this.email = email
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactVO> {
        override fun createFromParcel(parcel: Parcel): ContactVO {
            return ContactVO(parcel)
        }

        override fun newArray(size: Int): Array<ContactVO?> {
            return arrayOfNulls(size)
        }
    }
}