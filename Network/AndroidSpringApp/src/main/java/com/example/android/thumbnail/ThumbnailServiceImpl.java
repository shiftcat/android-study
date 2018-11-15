package com.example.android.thumbnail;

import com.example.android.Constant;
import com.example.android.attchedfile.FileService;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.models.ThumbnailImage;
import com.example.android.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThumbnailServiceImpl implements ThumbnailService
{

    private ThumbnailRepository repository;


    private FileService fileService;


    public ThumbnailServiceImpl(ThumbnailRepository repository, FileService fileService)
    {
        this.repository = repository;
        this.fileService = fileService;
    }


    private ThumbnailImage toAttachedFile(FileBytes fileBytes)
    {
        String originalName = fileBytes.getFileName();
        String changedName = FileUtils.rename(originalName);
        String subdirPath = FileUtils.getSubdirPath();

        ThumbnailImage thumbnailImage = new ThumbnailImage();
        thumbnailImage.setOriginalName(originalName);
        thumbnailImage.setChangedName(changedName);
        thumbnailImage.setSize(fileBytes.getSize());
        thumbnailImage.setSubdirPath(subdirPath);
        thumbnailImage.setOriginalFile(fileBytes.getAttachedFile());
        return thumbnailImage;
    }


    @Override
    public ThumbnailImage save(FileBytes fileBytes) throws IOException
    {
        ThumbnailImage thumbnailImage = toAttachedFile(fileBytes);

        ThumbnailImage resThumb = repository.save(thumbnailImage);

        FileUtils.thumbnailWrite(Constant.THUMBNAIL_DIR, fileBytes, thumbnailImage);
        log.debug("Thumbnail image write complete: {}", thumbnailImage.getOriginalFile());

        return resThumb;
    }


    @Override
    public void delete(Long id) {
        Optional<ThumbnailImage> res = repository.findById(id);
        ThumbnailImage thumbnailImage = res.orElseThrow(() -> new RuntimeException("Not found."));
        repository.deleteById(id);
        FileUtils.delete(Constant.THUMBNAIL_DIR, thumbnailImage);
    }


    @Override
    public ThumbnailImage update(FileBytes fileBytes) throws IOException {
        return null;
    }


    @Override
    public FileBytes getFileBytes(Long id) throws IOException {
        Optional<ThumbnailImage> res = repository.findById(id);
        ThumbnailImage img = res.orElseThrow(() -> new RuntimeException("Not found."));

        byte[] fileBytesArray = FileUtils.fileRead(Constant.THUMBNAIL_DIR, img);

        FileBytes fileBytes = FileUtils.toFileBytes(img, img.getId());
        fileBytes.setBytes(fileBytesArray);
        return fileBytes;
    }


    @Override
    public ThumbnailImage getThumbnailImage(Long id) throws IOException {
        Optional<ThumbnailImage> res = repository.findById(id);
        return res.orElseThrow(() -> new RuntimeException("Not found."));
    }


    @Override
    public List<FileBytes> search(FileSearch searchVO) {
        return null;
    }
}
