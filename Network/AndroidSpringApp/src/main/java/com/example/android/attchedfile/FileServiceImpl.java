package com.example.android.attchedfile;

import com.example.android.Constant;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.models.AttachedFile;
import com.example.android.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private FileRepository repository;


    public FileServiceImpl(FileRepository repository)
    {
        this.repository = repository;
    }


    private AttachedFile toAttachedFile(FileBytes fileBytes)
    {
        String originalName = fileBytes.getFileName();
        String changedName = FileUtils.rename(originalName);
        String subdirPath = FileUtils.getSubdirPath();

        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setOriginalName(originalName);
        attachedFile.setChangedName(changedName);
        attachedFile.setSize(fileBytes.getSize());
        attachedFile.setSubdirPath(subdirPath);
        attachedFile.setOrd(fileBytes.getOrd());
        attachedFile.setFileType(FileType.getFileType(originalName));
        return attachedFile;
    }


    @Transactional
    public AttachedFile save(FileBytes fileBytes) throws IOException {

        Optional<AttachedFile> res = repository.findTop1ByOriginalNameAndSize(fileBytes.getFileName(), fileBytes.getSize());
        if(res.isPresent()) {
            if(FileUtils.exists(Constant.UPLOAD_DIR, res.get())) {
                return res.get();
            }
        }

        AttachedFile newAttachedFile = toAttachedFile(fileBytes);

        AttachedFile resAttachedFile = repository.save(newAttachedFile);
        log.debug("File info save complete: {}", newAttachedFile);

        FileUtils.fileWrite(Constant.UPLOAD_DIR, fileBytes, newAttachedFile);
        log.debug("File write complete: {}", newAttachedFile.getOriginalName());

        return resAttachedFile;
    }


    @Transactional
    public void delete(Long id) {
        Optional<AttachedFile> res = repository.findById(id);
        AttachedFile attachedFile = res.orElseThrow(() -> new RuntimeException("Not found."));
        repository.deleteById(id);
        FileUtils.delete(Constant.UPLOAD_DIR, attachedFile);
    }


    @Transactional
    public AttachedFile update(FileBytes fileBytes) throws IOException {
        Optional<AttachedFile> res = repository.findById(fileBytes.getId());
        AttachedFile existingFile = res.orElseThrow(() -> new RuntimeException("Not found."));
        FileUtils.delete(Constant.UPLOAD_DIR, existingFile);

        String originalName = fileBytes.getFileName();
        String changedName = FileUtils.rename(originalName);
        String subdirPath = FileUtils.getSubdirPath();

        /* update */
        existingFile.setChangedName(changedName);
        existingFile.setOriginalName(originalName);
        existingFile.setSubdirPath(subdirPath);
        existingFile.setFileType(FileType.getFileType(originalName));
        /* update */

        FileUtils.fileWrite(Constant.UPLOAD_DIR, fileBytes, existingFile);

        return existingFile;
    }


    @Override
    public FileBytes getFileBytes(Long id) throws IOException {
        Optional<AttachedFile> res = repository.findById(id);
        AttachedFile attachedFile = res.orElseThrow(() -> new RuntimeException("Not found."));

        byte[] fileBytesArray = FileUtils.fileRead(Constant.UPLOAD_DIR, attachedFile);

        FileBytes fileBytes = FileUtils.toFileBytes(attachedFile, attachedFile.getId());
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
