package com.example.android.attchedfile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum FileType
{

    Image("IMG") {
        public List<String> getExtensions()
        {
            return Arrays.asList("jpg", "jpeg", "gif", "png");
        }
    }
    , Word("WORD") {
        public List<String> getExtensions()
        {
            return Arrays.asList("doc", "docx");
        }
    }
    , Excel("EXCEL") {
        public List<String> getExtensions()
        {
            return Arrays.asList("xls", "xlsx");
        }
    }
    , ProwerPoint("PPT") {
        public List<String> getExtensions()
        {
            return Arrays.asList("ppt", "pptx");
        }
    }
    , Other("Other") {
        public List<String> getExtensions()
        {
            return Arrays.asList();
        }
    }
    ;


    private final String type;

    FileType(String type)
    {
        this.type = type;
    }



    public abstract List<String> getExtensions();


    private boolean equalsExtesion(String str1, String str2)
    {
        return str1.toUpperCase().equals(str2.toUpperCase());
    }


    private boolean typecheck(List<String> exts, String ext)
    {
        long cnt = exts.stream().filter(t -> this.equalsExtesion(t, ext)).count();
        return cnt > 0;
    }


    private String getExtension(String fileName)
    {
        int lastidx = fileName.lastIndexOf(".");
        String ext = null;
        if(lastidx > -1) {
            ext = fileName.substring(lastidx+1, fileName.length());
        }
        else {
            ext = "";
        }
        return ext;
    }


    public static FileType getFileType(String fileName)
    {
        Optional<FileType> res =
                Arrays.stream(FileType.values())
                        .filter(t -> t.typecheck(t.getExtensions(), t.getExtension(fileName)))
                        .findFirst();
        FileType type = res.orElse(Other);
        return type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
