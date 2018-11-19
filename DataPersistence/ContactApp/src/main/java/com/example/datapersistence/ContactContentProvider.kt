package com.example.datapersistence

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.contactapp.ContactDAO
import com.example.contactapp.ContactVO
import io.realm.Realm

class ContactContentProvider : ContentProvider() {


    override fun onCreate(): Boolean {
        Realm.init(context)
        return true
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val contact = ContactVO()
        contact.name = values.getAsString("name")
        contact.phone = values.getAsString("phone")
        contact.email = values.getAsString("email")
        val realm = Realm.getDefaultInstance()
        val dao = ContactDAO(realm)
        try {
            dao.insert(contact)
        }
        finally {
            realm.close()
        }
        return uri
    }



    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val realm = Realm.getDefaultInstance()
        val dao = ContactDAO(realm)
        try {
            return dao.findAllForProvider()
        }
        finally {
            realm.close()
        }

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
