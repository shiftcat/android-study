package com.example.contactapp

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class ContactDAO(realm: Realm) {

    val mRealm = realm

    private fun exec(argFun: () -> Any?): Any? {
//        if(!mRealm.isInTransaction) {
//        }
        mRealm.beginTransaction()
        var res: Any? = argFun()
        mRealm.commitTransaction()
        return res
    }


    fun nextId(): Long {
        val maxId = mRealm.where<Contact>().max("id")
        Log.d(javaClass.name, "Contact max id ${maxId}")
        if(maxId != null) {
            return maxId.toLong() + 1
        }
        return 0
    }

    fun insert(vo: ContactVO) {
        exec {
            val nextId = nextId()
            Log.d(javaClass.name, "Contact new id ${nextId}")
            var newItem = mRealm.createObject<Contact>(nextId)
            newItem.name = vo.name
            newItem.phone = vo.phone
            newItem.email = vo.email
            true
        }
    }


    fun delete(id: Long): Boolean {
        Log.d(javaClass.name, "Contact Delete id ${id}")
        return exec {
            val delItem = mRealm.where<Contact>().equalTo("id", id).findFirst()
            if(delItem != null) {
                delItem.deleteFromRealm()
                true
            }
            else {
                true
            }
        } as Boolean
    }


    fun deleteAll() {
        exec {
            mRealm.deleteAll()
            true
        }
    }


    fun update(vo: Contact): Boolean {
        Log.d(javaClass.name, "Contact Update id ${vo.id}")
        return exec {
            var updItem = mRealm.where<Contact>().equalTo("id", vo.id).findFirst()
            if (updItem !== null) {
                updItem.name = vo.name
                updItem.phone = vo.phone
                updItem.email = vo.email
                true
            }
            else {
                false
            }
        } as Boolean
    }


    fun findAll(): RealmResults<Contact> {
        return exec {
            mRealm.where<Contact>().findAll().sort("name", Sort.ASCENDING)
        } as RealmResults<Contact>
    }


    fun findOne(id: Long): Contact? {
        Log.d(javaClass.name, "Contact findOne id ${id}")
        return exec {
            mRealm.where<Contact>().equalTo("id", id).findFirst()
        } as Contact
    }



}