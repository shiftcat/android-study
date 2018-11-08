package com.example.android.board.vo;

import lombok.Data;

@Data
public class BoardSearch
{

    private String subject;

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
