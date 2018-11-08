package com.example.android.board.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class BoardForm
{

    @NotNull
    @Length(min = 2, max = 30)
    private String writer;

    @NotNull
    @Length(min = 2, max = 150)
    private String subject;

    @NotNull
    @Length(min = 2, max = 2000)
    private String content;

    private MultipartFile[] files;

}
