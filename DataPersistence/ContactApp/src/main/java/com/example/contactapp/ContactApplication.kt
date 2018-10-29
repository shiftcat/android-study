package com.example.contactapp

import android.app.Application
import io.realm.Realm

class ContactApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}