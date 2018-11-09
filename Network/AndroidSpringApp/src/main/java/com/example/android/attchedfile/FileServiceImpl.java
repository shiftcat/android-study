package com.example.android.attchedfile;

import com.example.android.Constant;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.models.AttachedFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private FileRepository repository;


    public FileServiceImpl(FileRepository repository)
    {
        this.repository = repository;
    }


    private String rename(String name)
    {
        int lastidx = name.lastIndexOf(".");
        UUID uuid = UUID.randomUUID();
        String renamed = null;
        if(lastidx > -1) {
            String ext = name.substring(lastidx+1, name.length());
            renamed = uuid.toString() + "." + ext;
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


    private FileBytes fileModelToBytes(AttachedFile attachedFile)
    {
        FileBytes fileBytes = new FileBytes();
        fileBytes.setFileName(attachedFile.getOriginalName());
        fileBytes.setSize(attachedFile.getSize());
        fileBytes.setId(attachedFile.getId());
        return fileBytes;
    }


    private AttachedFile toAttachedFile(FileBytes fileBytes)
    {
        String originalName = fileBytes.getFileName();
        String changedName = rename(originalName);
        String subdirPath = getSubdirPath();

        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setOriginalName(originalName);
        attachedFile.setChangedName(changedName);
        attachedFile.setSize(fileBytes.getSize());
        attachedFile.setSubdirPath(subdirPath);
        attachedFile.setFileType(FileType.getFileType(originalName));
        return attachedFile;
    }


    @Transactional
    public AttachedFile save(FileBytes fileBytes) throws IOException {

        Optional<AttachedFile> res = repository.findTop1ByOriginalNameAndSize(fileBytes.getFileName(), fileBytes.getSize());
        if(res.isPresent()) {
            return res.get();
        }

        AttachedFile newAttachedFile = toAttachedFile(fileBytes);

        makeSubdir(Constant.UPLOAD_DIR + newAttachedFile.getSubdirPath());
        File dest = new File(Constant.UPLOAD_DIR + newAttachedFile.getSubdirPath() + File.separator + newAttachedFile.getChangedName());
        FileUtils.writeByteArrayToFile(dest, fileBytes.getBytes());
        log.debug("File write complete: {}", dest);

        AttachedFile resAttachedFile = repository.save(newAttachedFile);
        log.debug("File info save complete: {}", newAttachedFile);

        return resAttachedFile;
    }


    @Transactional
    public void delete(Long id) {
        Optional<AttachedFile> res = repository.findById(id);
        AttachedFile attachedFile = res.orElseThrow(() -> new RuntimeException("Not found."));
        File file = new File(Constant.UPLOAD_DIR + attachedFile.getSubdirPath() + File.separator + attachedFile.getChangedName());
        if(file.exists()) {
            log.debug("File delete complete: {}", file);
            file.delete();
        }
        repository.deleteById(id);
    }


    @Transactional
    public AttachedFile update(FileBytes fileBytes) throws IOException {
        Optional<AttachedFile> res = repository.findById(fileBytes.getId());
        AttachedFile existingFile = res.orElseThrow(() -> new RuntimeException("Not found."));
        File file = new File(Constant.UPLOAD_DIR + existingFile.getSubdirPath() + File.separator + existingFile.getChangedName());
        if(file.exists()) {
            log.debug("Existing file delete complete: {}", file);
            file.delete();
        }

        String originalName = fileBytes.getFileName();
        String changedName = rename(originalName);
        String subdirPath = getSubdirPath();

        makeSubdir(Constant.UPLOAD_DIR + subdirPath);
        File dest = new File(Constant.UPLOAD_DIR + subdirPath + File.separator + changedName);
        FileUtils.writeByteArrayToFile(dest, fileBytes.getBytes());
        log.debug("Update file write complete: {}", dest);

        /* update */
        existingFile.setChangedName(changedName);
        existingFile.setOriginalName(originalName);
        existingFile.setSubdirPath(subdirPath);
        existingFile.setFileType(FileType.getFileType(originalName));
        /* update */

        return existingFile;
    }


    @Override
    public FileBytes getFileBytes(Long id) throws IOException {
        Optional<AttachedFile> res = repository.findById(id);
        AttachedFile attachedFile = res.orElseThrow(() -> new RuntimeException("Not found."));

        File file = new File(Constant.UPLOAD_DIR + attachedFile.getSubdirPath() + File.separator + attachedFile.getChangedName());
        byte[] fileBytesArray = FileUtils.readFileToByteArray(file);

        FileBytes fileBytes = fileModelToBytes(attachedFile);
        fileBytes.setBytes(fileBytesArray);
        return fileBytes;
    }


    @Override
    public AttachedFile getAttachedFile(Long id) throws IOException {
        Optional<AttachedFile> res = repository.findById(id);
        return res.orElseThrow(() -> new RuntimeException("Not found."));
    }


    @Override
    public List<AttachedFile> search(FileSearch searchVO)
    {
        Pageable pageable = new PageRequest(searchVO.getOffset(), searchVO.getSize());

        List<AttachedFile> attachedFiles = null;
        if(StringUtils.isEmpty(searchVO.getFileName())) {
            attachedFiles = repository.findAllBy(pageable);
        }
        else {
            attachedFiles = repository.findByOriginalNameLike(searchVO.getFileName()+'%', pageable);
        }

        return attachedFiles;
    }


}
