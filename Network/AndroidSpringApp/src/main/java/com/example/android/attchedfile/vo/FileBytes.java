package com.example.android.attchedfile.vo;

import com.example.android.models.AttachedFile;
import lombok.Data;

@Data
public class FileBytes
{

    private Long id;

    private String fileName;

    private Long size;

    private Integer ord;

    private byte[] bytes;

    private AttachedFile attachedFile;

}
