package com.example.android.board.vo;

import com.example.android.attchedfile.vo.FileResponse;
import com.example.android.thumbnail.vo.ThumbnailResponse;
import lombok.Data;

import java.util.List;

@Data
public class BoardResponse
{

    private Long id;

    private String writer;

    private String subject;

    private String content;

    private List<FileResponse> files;

    private ThumbnailResponse thumbnail;

}
