package com.example.android.board.vo;

import com.example.android.attchedfile.vo.FileBytes;
import lombok.Data;

import java.util.List;

@Data
public class BoardRequest
{

    private Long id;

    private String writer;

    private String subject;

    private String content;

    private List<FileBytes> files;

}
