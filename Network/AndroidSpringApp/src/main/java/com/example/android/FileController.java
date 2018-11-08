package com.example.android;

import com.example.android.attchedfile.vo.FileSearch;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.FileService;
import com.example.android.models.AttachedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController
{

    @Autowired
    private FileService fileService;


    @PostMapping("/file")
    public ResponseEntity<?> multipart(@RequestPart(required = true) MultipartFile[] files) throws IOException
    {
        List<AttachedFile> results = new ArrayList<>();
        if(files != null && files.length > 0) {
            for(MultipartFile mf : files) {
                if(!mf.isEmpty()) {
                    FileBytes fileBytes = new FileBytes();
                    fileBytes.setBytes(mf.getBytes());
                    fileBytes.setFileName(mf.getOriginalFilename());
                    fileBytes.setSize(mf.getSize());
                    AttachedFile res = fileService.save(fileBytes);
                    results.add(res);
                }
            }
        }

        return ResponseEntity.ok(results);
    }



    @GetMapping("/file/{id}")
    public byte[] download(HttpServletResponse response, @PathVariable Long id) throws IOException
    {
        FileBytes fileBytes = fileService.getFileBytes(id);

        String fileName = java.net.URLEncoder.encode(fileBytes.getFileName(), "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        return fileBytes.getBytes();
    }


    @GetMapping("/file/search")
    public ResponseEntity<?> fileSearch(@ModelAttribute FileSearch searchVO)
    {
        List<AttachedFile> res = fileService.search(searchVO);
        return ResponseEntity.ok(res);
    }


}
