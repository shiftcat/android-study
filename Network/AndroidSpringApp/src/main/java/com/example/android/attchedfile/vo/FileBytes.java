package com.example.android.attchedfile.vo;

import com.example.android.models.AttachedFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FileBytes
{

    private Long id;

    private String fileName;

    private Long size;


    @JsonIgnore
    private byte[] bytes;

    @JsonIgnore
    private AttachedFile attachedFile;

}
