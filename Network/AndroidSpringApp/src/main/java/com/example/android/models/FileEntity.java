package com.example.android.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass
public class FileEntity
{
    @Column(name = "ORIGIN_NM", nullable = false, length = 300)
    private String originalName;

    @Column(name = "CHGED_NM", nullable = false, length = 150)
    private String changedName;

    @Column(name = "SUBDIR", nullable = false, length = 150)
    private String subdirPath;

    @Column(name = "SIZE", nullable = false)
    private Long size;


}
