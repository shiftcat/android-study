package com.example.android.thumbnail;

import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.models.ThumbnailImage;

import java.io.IOException;
import java.util.List;

public interface ThumbnailService
{

    public ThumbnailImage save(FileBytes fileBytes) throws IOException;

    public void delete(Long id);

    public ThumbnailImage update(FileBytes fileBytes) throws IOException;

    public FileBytes getFileBytes(Long id) throws IOException;

    public ThumbnailImage getThumbnailImage(Long id) throws IOException;

    public List<FileBytes> search(FileSearch searchVO);

}
