이 앱은 안드로이드 스튜디오의 위저드로 생성한 구글맵이다.

- res/values/google_maps_api.xml 파일을 생성한다.
- 구글 개발자 사이트에서 API key 발급 받아 key를 입력한다.
```xml
<resources>
    <string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">YOUR_API_KEY</string>
</resources>
```
