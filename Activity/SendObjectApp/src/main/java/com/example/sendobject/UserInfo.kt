package com.example.sendobject

import android.os.Parcel
import android.os.Parcelable

class UserInfo() : Parcelable
{
    var name: String? = null
    var email: String? = null
    var age: Int = 0


    constructor(name: String, email: String, age: Int): this() {
        this.name = name
        this.email = email
        this.age = age
    }


    // alt + enter 이하 IDE에 의해 자동 생성

    constructor(parcel: Parcel) : this() {
        // writeToParcel 의 순서와 동일하게 read 해야 한다.
        name = parcel.readString()
        email = parcel.readString()
        age = parcel.readValue(Int::class.java.classLoader) as Int
    }

    // 전송 측에서 Parcel에 전송할 정보를 저장한다.
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeValue(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    /*
    1. 코들린에서 object 예약어는 익명 클래스이다.
        var anonymity = object { }
        // SuperClass를 상속한 익명 클래스
        var anonymity = object: SuperClass { }
    2. 이름을 부여할 수 있다.
        object MyClass { }
    3. object 클래스는 선언과 동시에 객체가 생성된다.
        object MyClass { }
        var my = MyClass() <- 에러
        MyClass.myFun() <- 정상(마치 자바의 static 클래스처럼 사용)
    4. 클래스 내부에 선언된 object 클래스를 정의한 경우 자바의 static 클래스 처럼
        OuterClass.ObjectClass.myFun()
       위 같이 사용해야 한다.
       companion 예약어를 사용하면
       OuterClass.myFun()
       처러 사용할 수 있다.
     */

    // 반드시 CREATOR 이어야 함.
    companion object CREATOR : Parcelable.Creator<UserInfo> {
        override fun createFromParcel(parcel: Parcel): UserInfo {
            return UserInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserInfo?> {
            return arrayOfNulls(size)
        }
    }


}