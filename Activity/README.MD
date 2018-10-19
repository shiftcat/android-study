# Activity

## 안드로이드의 4대 구성요소
- Activity
- Service
- Broadcase Receiver
- Content Provider

### Activity
 - UI를 구성하기 위한 컴포넌트
 - 액티비티(Activity)는 사용자 화면을 제공하는 컴포넌이다.

### Service
  - UI 없이 백그라운드에서 장시간 수행되는 컴포넌트
  - 서비스(Service)는 화면과 전혀 상관없이 사용자 눈에는 보이지 않지만, 백그라운드에서 장시간 무언가를 수행할 수 있는 컴포넌트이다.

### ContentProvider
 - 애플리케이션 간 데이터를 공유하기 위한 컴포넌트
 - 안드로이드 스마트폰에는 여러 앱이 있고, 그 앱들 간의 데이터를 공유하기 위한 목적.

### BroadcaseReceiver
 - 이벤트 모델로 수행되는 컴포넌트


### 모듈 수준의 그레이들
```gradle
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion 28
    defaultConfig {
        applicationId "com.example.user.androidlab"
        minSdkVersion 15
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
}
```

 - compileSdkVersion: 사용하는 컴파일러 버전
 - applicationId "com.example.user.androidlab": 앱의 식별자
 - minSdkVersion: 최소 지원 범위
 - targetSdkVersion: 사용하고 있는 SDK 버전
 - versionCode: 앱의 버전

### AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.example.user.androidlab">

    <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

</manifest>
```
 - package="com.example.user.androidlab": 앱의 식별자. 그레이들의 applicationId과 같은 값
 - android:icon="@mipmap/ic_launcher" android:label="@string/app_name": 스마튼폰에 앱이 설치되었을 때 표시할 아이콘과 이름
 - activity android:name=".MainActivity": 앱에 액티비티 컴포넌트를 등록하기 위한 태그. 만약 앱에 액티비티가 3개라면 이곳에 3개의 액티비티 태그가 정의되어야 한다.
 - 서비스, 프로바이더, 리시버 컴포넌트가 있다면 \<service>, \<provider>, \<receiver> 태그로 등록해 주어야 한다.
 - \<intent-filter>: 앱의 아이콘을 클릭했을 때 실행되는 액티비티를 설정한다.

## Intent
 - 안드로이드의 4대 구성 요소를 실행하기 위해서는 Intent라는 객체가 필요하다.
 - 안드로이드의 4대 구성 요소를 실행하기 위해서는 개발자가 직접 실행하는 것이 아니라 OS에게 실행을 요청하게 된다.
 - 실행 요청을 전달 받은 OS는 해당 실행 요소를 실행하게 되는데 실행 요청을 위한 정보를 담는 객체가 Intent이다.
 - Intent를 통해 객체를 전달 할 때는 Parcelable 인터페이스를 구현한 객체만 가능하다.
 - Parcelable 인터페이스는 전달 받은 쪽에서 객체를 복원할 때 필요한 정보를 가진 부분을 의미한다.


### 명시적, 암시적 인텐트
#### 명시적 인텐트
 - 실행하고자 하는 컴 컴포넌트의 클래스명을 인텐트에 담는 방법이다.
 - 주로 같은 앱의 컴포넌트를 실행할 때 이용하는 방법이다.
```kotlin
// SecondActivity 실행 요청
var intent = Intent(this, SecondActivity::class.java)
intent.putExtra("data1", "Value1")
intent.putExtra("data2", 100)
startActivity(intent) // 돌려받을 데이터가 없을 때
startActivityForResult(intent, 10) // 돌려받을 데이터가 있을 때

// SecondActivity 실행 후 돌려받을 데이터가 있을 경우 처리할 이벤트 함수
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
        //...
    }
}

// SecondActivity 측 데이터 수신
val data1 = intent.getStringExtra("data1")
val data2 = intent.getIntExtra("data2", 0)
textView2.text = "data1: ${data1} - data2: ${data2}"

// SecondActivity에서 이전 액티비티로 돌아갈 때 버튼 등의 리슨너에서 처리
// 이전 액티비티로 돌려 줄 데이터가 있다면...
intent.putExtra("result", "RESULT OK")
setResult(Activity.RESULT_OK, intent)
// finish() 메서드를 호출하여 현재 액티비티 종료 -> 이전 액티비티로 이동.
finish()
```

 #### 암시적 인텐트
 - 클래스명이 아닌 Intent Filter 정보를 활용
 - 주로 클래스명을 알 수 없는 외부 앱의 컴포넌트를 실행할 때 이용
```xml
<activity android:name=".DetailActivity">
    <intent-filter>
        <action android:name="com.example.ACTION_VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</activity>
```
```kotlin
var intent = Intent()
intent.setAction("com.example.ACTION_VIEW")
startActivity(intent)
```

## Activity 생명주기
![Activity LifeCycle](images/activity_lifecycle.jpg)
(출처) https://kairo96.gitbooks.io/android/content/ch2.4.1.html

 - 활성상태(activity running): 현재 액티비티가 화면을 점유하여 출력되고 있으며 사용자 이벤트 처리가 정상으로 처리는 상태
 - 일시 정지 상태(pause): 현재 액티비티가 일시적으로 사용이 불가능한 상태
 - 비활성 상태(stop): 현재 액티비티가 다른 액티비티로 인해 화면이 완벽하게 가려진 상태

#### 활성상태
 - onCreate() -> onStart() -> onResume() 함수가 호출되면서 활성 상태가 된다.
 - setContentView() 함수가 호출되는 시점이 화면 출력 순간이 아니라 onResume()함수까지 실행하고, setContentView()함수에서 출력한 내용이 화면에 나오는 구조이다.

#### 일시 정지 상태
 - 화면이 보이지만, 포커스를 잃은 상태
 - onPause() 함수가 자동으로 호출
 - onPause() 함수에 다음 내용 구현 권장
    - 애니메이션이다 CPU 소비를 야기할 수 있는 기타 지속적인 작업 정지
    - GPS와 같은 센서값 수신, 서버 네트워킹 등 액티비티가 일지 정지된 동안 불필요한 동작 정지

#### 비활성 상태
 - 다른 액티비로 인해 화면이 완전히 가려진 상태
 - onPause() -> onStop() 함수까지 호출 됨
 - 뒤로가기 버튼 등으로 가렸던 액티비티가 다시 활성 전환 onRestart() -> onStart() -> onResume() 함수가 차례로 호출 됨
 