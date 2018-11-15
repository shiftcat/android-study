package com.example.android;

import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.board.*;
import com.example.android.board.vo.BoardForm;
import com.example.android.board.vo.BoardResponse;
import com.example.android.board.vo.BoardSearch;
import com.example.android.board.vo.BoardRequest;
import com.example.android.thumbnail.ThumbnailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;


    @Autowired
    private ThumbnailService thumbnailService;



    public static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    private FileBytes multipartFileToRequest(MultipartFile multipartFile) throws IOException {
        FileBytes fileBytes = new FileBytes();
        fileBytes.setFileName(multipartFile.getOriginalFilename());
        fileBytes.setSize(multipartFile.getSize());
        fileBytes.setBytes(multipartFile.getBytes());
        return fileBytes;
    }


    private BoardRequest formToRequest(BoardForm boardForm)
    {
        MultipartFile[] files = boardForm.getFiles();

        List<FileBytes> fileBytes = null;
        if(files != null && files.length > 0) {
            final int[] idx = {0};
            fileBytes = Arrays.stream(files)
                    .map(
                            wrap(f -> {
                                FileBytes fb = multipartFileToRequest(f);
                                fb.setOrd(idx[0]++);
                                return fb;
                            })
                    )
                    .collect(Collectors.toList());
        }
        else {
            fileBytes = Arrays.asList();
        }

        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setWriter(boardForm.getWriter());
        boardRequest.setSubject(boardForm.getSubject());
        boardRequest.setContent(boardForm.getContent());
        boardRequest.setFiles(fileBytes);
        return boardRequest;
    }


    @PostMapping("/board")
    public ResponseEntity<?> save(@ModelAttribute @Valid BoardForm boardForm) throws IOException {
        BoardRequest newBoard = formToRequest(boardForm);
        BoardResponse resBoard = boardService.save(newBoard);
        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri().path("/{id}")
                    .buildAndExpand(resBoard.getId())
                    .toUri();
        log.debug("Create new uri: {}", uri);
        return ResponseEntity.created(uri).body(resBoard);
    }


    @PutMapping("/board/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @ModelAttribute @Valid BoardForm boardForm) throws IOException {
        BoardRequest boardRequest = formToRequest(boardForm);
        boardRequest.setId(id);
        BoardResponse resBoard = boardService.update(boardRequest);
        return ResponseEntity.ok(resBoard);
    }



    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {
        boardService.delete(id);
    }



    @GetMapping("/board/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id)
    {
        BoardResponse boardRequest = boardService.findOne(id);
        return ResponseEntity.ok(boardRequest);
    }


    @GetMapping("/board/search")
    public ResponseEntity<?> search(@ModelAttribute BoardSearch searchVO)
    {
        List<BoardResponse> boards = boardService.search(searchVO);
        return ResponseEntity.ok(boards);
    }


    @GetMapping("/board/thumbnail/{id}")
    public byte[] thumbnail(HttpServletResponse response, @PathVariable Long id) throws IOException
    {
        FileBytes fileBytes = thumbnailService.getFileBytes(id);

        String fileName = java.net.URLEncoder.encode(fileBytes.getFileName(), "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        return fileBytes.getBytes();
    }


}
