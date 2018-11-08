package com.example.android.attchedfile.vo;

import lombok.Data;

@Data
public class FileSearch
{
    private String fileName;

    private Integer size;

    private Integer offset;


    public Integer getOffset()
    {
        if (offset == null) {
            return 0;
        }
        return offset;
    }

    public Integer getSize()
    {
        if (size == null) {
            return 10;
        }
        return size;
    }
}
