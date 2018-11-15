package com.example.android.board.vo;

import com.example.android.attchedfile.vo.FileResponse;
import com.example.android.thumbnail.vo.ThumbnailResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BoardResponse
{
    private Long id;

    private String writer;

    private String subject;

    private String content;

    private Integer fileCount;

    private ThumbnailResponse thumbnail;


    private List<FileResponse> files;

}
