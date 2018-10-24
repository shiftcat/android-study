# BroadcastReceiver
- 브로드캐스트 리시버를 흔히 "이벤트 모델로 수행되는 컴포넌트"라 부른다.
- 액티비티, 서비스, 브로드캐스트 리시버는 인텐트에 의해 실행된다.
- 인텐트가 발생하여 실행될 브로드캐스트 리시버가 없다고 하더라도 에러가 발생하지 않는다.
- 샐행될 브로드캐스트 리시버가 여러 개라면 모두 실행된다.
- 브로드캐스트 리시버는 특정한 목적의 업무를 담당하기 위해 정의된 컴포넌트가 아니다.
    * 액티비티는 화면 출력이 목적
    * 서비스는 백그라운드 업무 로직 구현이 목적
    * 콘텐트 프로바이더는 앱 간의 데이터 공유가 목적
- 브로드캐스트 리시버는 10초 이내에 업무 처리가 종료되어야 한다.
- 간단한 업무처리, 다른 액티비티나 서비스 실행하는 역할로만 사용.

## 브로드캐스트 리시버 작성

```kotlin
class MyReceiver : BroadcastReceiver() 
{
    override fun onReceive(context: Context, intent: Intent) 
    {
        val data1 = intent.getIntExtra("data1", 0)
        val data2 = intent.getStringExtra("data2")
        val msg = "data1: ${data1}, data2: ${data2}"
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
```

컴포넌트 클래스이므로 AndroidManifest.xml 파일에 \<rceiver> 태그로 등록 한다.

```xml
    <application>
        ...
        <receiver
                android:name=".MyReceiver"
                android:enabled="true"
                android:exported="true">
            <intent-filter>
                <action android:name="com.example.broadcast.TEST1" />
            </intent-filter>
        </receiver>
    </application>

```

인텐트 발생

1. 같은 앱에서 명시적 인텐트
```kotlin
var intent = Intent(this, MyReceiver::class.java)
sendBroadcast(intent)
```

2. 다른 앱에서 명시적 인텐트
- 다른 앱에서 해당 브로드캐스트 리시버의 패키지명과 클래스명을 알아야 실행할 수 있는 단점이 있다.
```kotlin
// Manifest에 정의된 패키지명과 리시버 클래스 명
var intent = Intent()
intent.setClassName("com.example.broadcastapp", "com.example.broadcastapp.MyReceiver")
intent.putExtra("data1", 100)
intent.putExtra("data2", "요청 데이터")
sendBroadcast(intent)
```

3. 다른 앱에서 암시적 인텐트
- 8.0(오레오) 이전 안드로이드에서 정상적으로 작동한다.
- 8.0(오레오) 버전 부터 암시적 인텐트에 의한 리시버의 백그라운드 실행을 제한하고 있다.
```kotlin
var intent = Intent("com.example.broadcast.TEST1")
intent.putExtra("data1", 100)
intent.putExtra("data2", "요청 데이터")
sendBroadcast(intent)
```

- 백그라운드 실행이 아니라면 다음과 같이 registerReceiver() 함수를 이용해 리시버를 등록하면 암시적 인텐트로 실행할 수 있다.
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addReceiver()
        ...
    }

    var myReceiver: MyReceiver? = null

    fun addReceiver() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        myReceiver = MyReceiver()
        var filter = IntentFilter("com.example.broadcast.TEST1")
        registerReceiver(myReceiver, filter)
    }


    override fun onDestroy() {
        super.onDestroy()
        if(myReceiver != null) {
            unregisterReceiver(myReceiver)
            myReceiver = null
        }
    }
}
```