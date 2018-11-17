package com.example.okhttpmultipart


class ActivityRequest
{
    private constructor()
    companion object {
        val REQUEST_WRITE = 1000
        val REQUEST_DETAIL = 2000
    }
}



class ExtraName
{
    private constructor()

    companion object {
        val NEW_BOARD = "NEW_BOARD"
        val DELETE_BOARD = "DELETE_BOARD"
        val BOARD = "BOARD"
    }
}


class BoardApi
{
    private constructor()

    companion object {
        val SERVER = "http://192.168.100.114:8080"

        val BOARD = "${SERVER}/board"

        val BOARD_SEARCH = "${SERVER}/board/search"

        val BOARD_THUMNAIL = "${SERVER}/board/thumbnail"

        val FILE = "${SERVER}/file"
    }
}