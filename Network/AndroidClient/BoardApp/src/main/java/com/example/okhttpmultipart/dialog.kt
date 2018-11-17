package com.example.okhttpmultipart

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

fun showDeleteDialog(context: Context, listener: DialogInterface.OnClickListener) {
    var builder =
        AlertDialog.Builder(context)
            .setTitle("삭제 확인")
            .setMessage("게시물을 삭제 하시겠습니까?")
            // builder.setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("확인", listener)
            .setNegativeButton("취소", listener)
    builder.show()
}


fun WaitDialog(context: Context): Dialog {
    val dialog = Dialog(context)
    val inflate = LayoutInflater.from(context).inflate(R.layout.progress_layout, null)
    dialog.setContentView(inflate)
    dialog.setCancelable(false)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return dialog
}

