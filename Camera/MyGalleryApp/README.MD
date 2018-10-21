# 전자액자(MyGallery)
안드로이드 생존코딩 챕터 9장

## 기능
 - 기기에 저장된 사진을 차례대로 부여준다.
 - 3초마다 자동으로 슬라이드한다.

## 라이브러리 설정
 - Anko: 인텐트, 다이얼로그, 로그 등을 구현하는 데 도움이 되는 라이브러리
 - Glide: 사진 로딩에 특화된 라이브러리로 메모리 절약과 자연스러운 사진 로딩에 사용.


 ### 모든 사진정보 가져오기
 ```kotlin
var cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, // 가져올 항목 배열
            null, // 조건
            null, // 조건
            MediaStore.Images.ImageColumns.DATE_TAKEN+" DESC") // 찍은 날짜 내림차순

 ```


 ### 매니페스트에 와부 저장소 읽기 권한 추가
 ```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.example.mygalleryapp">

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <application
    >
            ....
    </application>

</manifest>
 ```


 ### 권한요청
 ```kotlin
// 권한 부여 여부 확인
if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    != PackageManager.PERMISSION_GRANTED
) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    ) {
        alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다.", "권힌 필요 이유") {
            yesButton {
                // 권한 요청
                requestPermission()
            }
            noButton {

            }
        }.show()

    } else {
        // 권한 요청
        requestPermission()
    }

} else {
    getAllPhotos()
}

private fun requestPermission()
{
    // 권한 요청
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        REQUEST_READ_EXTERNAL_STORAGE
    )
}
 ```


 ### 이미지 로딩

 ```kotlin
 Glide.with(this).load(uri).into(imageView)
 ```