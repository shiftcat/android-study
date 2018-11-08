package com.example.android.attchedfile;

public class TestMain
{
    public static void main(String[] args) {
        String ext = "jpg";
        FileType type = FileType.getFileType(ext);
        System.out.println(type);
    }
}
