package com.example.okhttpmultipart

import android.os.Parcel
import android.os.Parcelable

class Board(): Parcelable
{

    var id: Long = -1

    lateinit var writer: String

    lateinit var subject: String

    lateinit var content: String

    var thumb: Long = -1

    var thumbOrigin: Long = -1


    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        writer = parcel.readString()
        subject = parcel.readString()
        content = parcel.readString()
        thumb = parcel.readLong()
        thumbOrigin = parcel.readLong()
    }

    constructor(boardItem: BoardItemBase): this() {
        if(boardItem.getItemType() == BoardItemType.CONTENT_WITH_IMAGE) {
            val bi = boardItem as BoardItemWithImage
            id = bi.id
            writer = bi.writer
            subject = bi.subject
            content = bi.content
            thumb = bi.thumbId
            thumbOrigin = bi.thumbOriginId
        }
        else if (boardItem.getItemType() == BoardItemType.CONTENT_ONLY) {
            val bi = boardItem as BoardItemContent
            id = bi.id
            writer = bi.writer
            subject = bi.subject
            content = bi.content
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) {
            return false
        }
        else if((other is Board) == false) {
            return false
        }
        else {
            return id == (other as Board).id
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(writer)
        parcel.writeString(subject)
        parcel.writeString(content)
        parcel.writeLong(thumb)
        parcel.writeLong(thumbOrigin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Board> {
        override fun createFromParcel(parcel: Parcel): Board {
            return Board(parcel)
        }

        override fun newArray(size: Int): Array<Board?> {
            return arrayOfNulls(size)
        }
    }


}