package com.example.contactapp

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Contact (
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    @Ignore
    var checked: Boolean = false
): RealmObject() {

}