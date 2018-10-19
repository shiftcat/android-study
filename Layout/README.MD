# Layout
 * LinearLayout
 * RelativeLayout
 * FrameLayout
 * TableLayout
 * GridLayout
 * ConstraintLayout

## LinearLayout
 - 뷰를 순서대로 가로나 세로 방향으로 나열한다.
 - orientation 속성을 이용하여 가로 또는 세로 방향을 지정한다.
 - 레이아웃을 중첩하여 사용할 수 있다.

__버튼 두 개를 세로로 나열__
```xml
<LinearLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
    <Button
            android:id="@+id/button1"
            android:text="Button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    <Button
            android:id="@+id/button2"
            android:text="Button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
</LinearLayout>
```