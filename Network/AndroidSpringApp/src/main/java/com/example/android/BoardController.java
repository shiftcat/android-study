package com.example.android;

import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.board.*;
import com.example.android.board.vo.BoardForm;
import com.example.android.board.vo.BoardSearch;
import com.example.android.board.vo.BoardVO;
import com.example.android.thumbnail.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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


    private BoardVO formToVO(BoardForm boardForm)
    {
        MultipartFile[] files = boardForm.getFiles();

        List<FileBytes> fileBytes = null;
        if(files != null && files.length > 0) {
            fileBytes = Arrays.stream(files)
                    .map(
                            wrap(f -> {
                                return multipartFileToRequest(f);
                            })
                    )
                    .collect(Collectors.toList());
        }
        else {
            fileBytes = Arrays.asList();
        }

        BoardVO boardVO = new BoardVO();
        boardVO.setWriter(boardForm.getWriter());
        boardVO.setSubject(boardForm.getSubject());
        boardVO.setContent(boardForm.getContent());
        boardVO.setFiles(fileBytes);
        return boardVO;
    }


    @PostMapping("/board")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@ModelAttribute @Valid BoardForm boardForm) throws IOException {
        BoardVO boardVO = formToVO(boardForm);
        BoardVO savedBoard = boardService.save(boardVO);
        return ResponseEntity.ok(savedBoard);
    }


    @PutMapping("/board/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @ModelAttribute @Valid BoardForm boardForm) throws IOException {
        BoardVO boardVO = formToVO(boardForm);
        boardVO.setId(id);
        BoardVO savedBoard = boardService.update(boardVO);
        return ResponseEntity.ok(savedBoard);
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
        BoardVO boardVO = boardService.findOne(id);
        return ResponseEntity.ok(boardVO);
    }


    @GetMapping("/board/search")
    public ResponseEntity<?> search(@ModelAttribute BoardSearch searchVO)
    {
        List<BoardVO> boards = boardService.search(searchVO);
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
