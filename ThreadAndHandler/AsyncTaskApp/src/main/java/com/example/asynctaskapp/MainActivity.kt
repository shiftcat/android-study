package com.example.asynctaskapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    AsyncTask
    - AsyncTask는 비동기 처리를 위해 제공되는 클래스이다.
    - 개발자가 발생 시키는 쓰래드와 핸들러의 조합으로 쓰래드 운영 중 화면 처리가 가능했던 구조를 클래스로 제공하는 것이다.

    메서드
    - onPreExecute: doInBackground 메서드가 호출되기 전에 호출되는 메서드.
       Main Thread가 처리한다.
    - doInBackground: 일반 쓰래드에서 처리한다.
    - onProgressUpdate: doInBackground 메서드에서 publishProgress 메서드를 호출하면 Main Thread가 처리하는 메서드.
                        doInBackground 메서드 내에서 화면 처리가 필요할 때 사용한다.
    - onPostExecute: doInBackground 메서드 수행 완료 후 호출.
                     Main Thread가 처리한다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var time = System.currentTimeMillis()
            textView1.text = "현재시간: ${time}"
        }

        var sync = MyAsyncTask()
        // vararg 가변형 매개변수
        sync.execute(10, 20)
    }


    // <T1, T2, T3>
    inner class MyAsyncTask: AsyncTask<Int, Long, String>() {

        // Main Thread에 의해 처음 한 번만 호출 됨.
        override fun onPreExecute() {
            super.onPreExecute()
            textView2.text = "AsyncTask start."
        }


        // 새로운 쓰래드에 의해 처리
        // 화면 관련 처리할 수 없음.
        override fun doInBackground(vararg params: Int?): String {
            var a1 = params[0]!!
            var a2 = params[1]!!

            for (idx in 0..9) {
                SystemClock.sleep(500)
                a1++
                a2++

                Log.d("AsyncTaskApp", "${idx}, ${a1}, ${a2}")

                var time = System.currentTimeMillis()
                publishProgress(time) // -> onProgressUpdate()
            }

            return "Execute complete!!" // -> onPostExecute()
        }


        override fun onProgressUpdate(vararg values: Long?) {
            super.onProgressUpdate(*values)
            textView2.text = "Async: ${values[0]}"
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            textView2.text = result
        }

    }
}
