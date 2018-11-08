package com.example.android.thumbnail;

import com.example.android.Constant;
import com.example.android.attchedfile.FileService;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.models.ThumbnailImage;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

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


    private String rename(String name)
    {
        int lastidx = name.lastIndexOf(".");
        UUID uuid = UUID.randomUUID();
        String renamed = null;
        if(lastidx > -1) {
            //String ext = name.substring(lastidx+1, name.length());
            //renamed = uuid.toString() + "." + ext;
            // Thumbnails 라이브러이에서 확장자가 png가 아니면 추가로 png 확장자가 붙음.
            renamed = uuid.toString() + ".png";
        }
        else {
            renamed = uuid.toString();
        }

        log.debug("File rename {} -> {}", name, renamed);
        return renamed;
    }



    private String getSubdirPath()
    {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return File.separator + year + File.separator + month + File.separator + day;
    }


    private void makeSubdir(String path)
    {
        File sub = new File(path);
        if(!sub.exists()) {
            sub.mkdirs();
            log.debug("Make subdir: {}", path);
        }
    }


    private FileBytes fileModelToBytes(ThumbnailImage thumbnailImage)
    {
        FileBytes fileBytes = new FileBytes();
        fileBytes.setFileName(thumbnailImage.getOriginalName());
        fileBytes.setSize(thumbnailImage.getSize());
        fileBytes.setId(thumbnailImage.getId());
        return fileBytes;
    }


    private ThumbnailImage toAttachedFile(FileBytes fileBytes)
    {
        String originalName = fileBytes.getFileName();
        String changedName = rename(originalName);
        String subdirPath = getSubdirPath();

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
        makeSubdir(Constant.THUMBNAIL_DIR + thumbnailImage.getSubdirPath());
        File dest =
                new File(Constant.THUMBNAIL_DIR + thumbnailImage.getSubdirPath() + File.separator
                        + thumbnailImage.getChangedName());

        Thumbnails.of(new ByteArrayInputStream(fileBytes.getBytes()))
                .size(190, 150).outputFormat("png").toFile(dest);

        log.debug("Thumbnail image write complete: {}", dest);

        return repository.save(thumbnailImage);
    }


    @Override
    public void delete(Long id) {
        Optional<ThumbnailImage> res = repository.findById(id);
        ThumbnailImage thumbnailImage = res.orElseThrow(() -> new RuntimeException("Not found."));
        File file = new File(Constant.THUMBNAIL_DIR + thumbnailImage.getSubdirPath() + File.separator + thumbnailImage.getChangedName());
        if(file.exists()) {
            log.debug("Thumbnail delete complete: {}", file);
            file.delete();
        }
        repository.deleteById(id);
    }


    @Override
    public ThumbnailImage update(FileBytes fileBytes) throws IOException {
        return null;
    }


    @Override
    public FileBytes getFileBytes(Long id) throws IOException {
        Optional<ThumbnailImage> res = repository.findById(id);
        ThumbnailImage img = res.orElseThrow(() -> new RuntimeException("Not found."));

        File file = new File(Constant.THUMBNAIL_DIR + img.getSubdirPath() + File.separator + img.getChangedName());
        byte[] fileBytesArray = FileUtils.readFileToByteArray(file);

        FileBytes fileBytes = fileModelToBytes(img);
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
