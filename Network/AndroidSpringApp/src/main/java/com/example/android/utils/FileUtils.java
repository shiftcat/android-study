package com.example.android.utils;

import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.models.FileEntity;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

@Slf4j
public class FileUtils
{

    public static String getSubdirPath()
    {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return File.separator + year + File.separator + month + File.separator + day;
    }



    public static void makeDirs(String path)
    {
        File sub = new File(path);
        if(!sub.exists()) {
            sub.mkdirs();
            log.debug("Make subdir: {}", path);
        }
    }


    public static String rename(String name)
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


    public static FileBytes toFileBytes(FileEntity fileEntity, Long newId)
    {
        FileBytes fileBytes = new FileBytes();
        fileBytes.setFileName(fileEntity.getOriginalName());
        fileBytes.setSize(fileEntity.getSize());
        fileBytes.setId(newId);
        return fileBytes;
    }



    public static void delete(String baseDir, FileEntity fileEntity)
    {
        File file = new File(baseDir + fileEntity.getSubdirPath() + File.separator + fileEntity.getChangedName());
        if(file.exists()) {
            log.debug("File delete complete: {}", file);
            file.delete();
        }
    }


    public static void fileWrite(String baseDir, FileBytes fileBytes, FileEntity fileEntity) throws IOException
    {
        FileUtils.makeDirs(baseDir + fileEntity.getSubdirPath());
        File dest = new File(baseDir + fileEntity.getSubdirPath() + File.separator + fileEntity.getChangedName());
        writeByteArrayToFile(dest, fileBytes.getBytes());
    }


    public static void thumbnailWrite(String baseDir, FileBytes fileBytes, FileEntity fileEntity) throws IOException
    {
        FileUtils.makeDirs(baseDir + fileEntity.getSubdirPath());
        File dest =
                new File(baseDir + fileEntity.getSubdirPath() + File.separator
                        + fileEntity.getChangedName());

        Thumbnails.of(new ByteArrayInputStream(fileBytes.getBytes()))
                .size(190, 150).toFile(dest);
    }


    public static byte[] fileRead(String baseDir, FileEntity fileEntity) throws IOException
    {
        File file = new File(baseDir + fileEntity.getSubdirPath() + File.separator + fileEntity.getChangedName());
        return readFileToByteArray(file);
    }


    public static boolean exists(String baseDir, FileEntity fileEntity)
    {
        File file =
                new File(baseDir + fileEntity.getSubdirPath() + File.separator
                        + fileEntity.getChangedName());
        return file.exists();
    }

}
