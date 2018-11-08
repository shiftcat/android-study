package com.example.android.attchedfile;

import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.models.AttachedFile;
import com.example.android.attchedfile.vo.FileSearch;

import java.io.IOException;
import java.util.List;

public interface FileService
{

    public AttachedFile save(FileBytes fileBytes) throws IOException;

    public void delete(Long id);

    public AttachedFile update(FileBytes fileBytes) throws IOException;

    public FileBytes getFileBytes(Long id) throws IOException;

    public AttachedFile getAttachedFile(Long id) throws IOException;

    public List<AttachedFile> search(FileSearch searchVO);

}
