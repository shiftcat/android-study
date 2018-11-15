package com.example.android.board;


import com.example.android.ExceptionFunction;
import com.example.android.attchedfile.FileService;
import com.example.android.attchedfile.FileType;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.attchedfile.vo.FileResponse;
import com.example.android.board.vo.BoardRequest;
import com.example.android.board.vo.BoardResponse;
import com.example.android.board.vo.BoardSearch;
import com.example.android.models.AttachedFile;
import com.example.android.models.Board;
import com.example.android.models.ThumbnailImage;
import com.example.android.thumbnail.ThumbnailService;
import com.example.android.thumbnail.vo.ThumbnailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {


    private BoardRepository repository;

    private FileService fileService;

    private ThumbnailService thumbnailService;


    public BoardServiceImpl(BoardRepository repository, FileService fileService, ThumbnailService thumbnailService) {
        this.repository = repository;
        this.fileService = fileService;
        this.thumbnailService = thumbnailService;
    }


    private <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    private ThumbnailImage saveThumnail(List<FileBytes> fileBytes) throws IOException {
        Optional<FileBytes> res = fileBytes.stream()
                .filter(f -> {
                    FileType fileType = FileType.getFileType(f.getFileName());
                    return FileType.Image == fileType;
                })
                .findFirst();

        ThumbnailImage thumbnailImage = null;
        if (res.isPresent()) {
            // 썸네일 생성 및 저장.
            FileBytes fb = res.get();
            thumbnailImage = thumbnailService.save(fb);
        }

        return thumbnailImage;
    }


    private List<AttachedFile> saveAttachedFiles(List<FileBytes> fileBytes) {
        List<AttachedFile> attachedFiles =
                fileBytes.stream()
                        .map(
                                wrap(f -> {
                                    log.debug("FileBytes file: {}", f.getFileName());
                                    AttachedFile af = fileService.save(f);
                                    /* 썸네일 처리 시 원본 이미지 필요함. */
                                    f.setAttachedFile(af);
                                    return af;
                                })
                        )
                        .collect(Collectors.toList());

        return attachedFiles;
    }



    private ThumbnailResponse thumbnailToResponse(ThumbnailImage thumbnailImage)
    {
        if(thumbnailImage != null) {
            ThumbnailResponse thumbnailResponse = new ThumbnailResponse();
            thumbnailResponse.setId(thumbnailImage.getId());
            thumbnailResponse.setName(thumbnailImage.getOriginalName());
            thumbnailResponse.setSize(thumbnailImage.getSize());
            thumbnailResponse.setOriginId(thumbnailImage.getOriginalFile().getId());
            return thumbnailResponse;
        }
        return null;
    }



    private BoardResponse boardModelToResponse(Board board)
    {
        BoardResponse boardResponse = new BoardResponse();
        boardResponse.setId(board.getId());
        boardResponse.setWriter(board.getWirter());
        boardResponse.setSubject(board.getSubject());
        boardResponse.setContent(board.getContent());
        boardResponse.setFileCount(board.getFileCount());
        if(board.getThumbnailImage() != null) {
            boardResponse.setThumbnail(thumbnailToResponse(board.getThumbnailImage()));
        }

        return boardResponse;
    }



    @Transactional
    public BoardResponse save(BoardRequest boardRequest) throws IOException {
        List<FileBytes> fileBytes = boardRequest.getFiles();
        List<AttachedFile> attachedFiles = null;
        ThumbnailImage thumbnailImage = null;
        if (!CollectionUtils.isEmpty(fileBytes)) {
            attachedFiles = saveAttachedFiles(fileBytes);
            thumbnailImage = saveThumnail(fileBytes);
        } else {
            attachedFiles = new ArrayList<>();
        }

        Board newBoard = new Board();
        newBoard.setWirter(boardRequest.getWriter());
        newBoard.setSubject(boardRequest.getSubject());
        newBoard.setContent(boardRequest.getContent());
        newBoard.setAttachedFiles(attachedFiles);
        newBoard.setFileCount(attachedFiles.size());
        newBoard.setThumbnailImage(thumbnailImage);

        Board resBoard = repository.save(newBoard);
        BoardResponse response = boardModelToResponse(resBoard);
        response.setFiles(fileModelToResponse(resBoard.getAttachedFiles()));
        return response;
    }


    @Transactional
    public void delete(Long id) {
        Optional<Board> res = repository.findOne(id);
        res.orElseThrow(() -> new RuntimeException("Board content not found."));
        Board existingBoard = res.get();

        ThumbnailImage existingThumnail = existingBoard.getThumbnailImage();

        repository.delete(existingBoard);

        if (existingThumnail != null) {
            thumbnailService.delete(existingThumnail.getId());
        }
    }


    @Transactional
    public BoardResponse update(BoardRequest boardRequest) throws IOException {
        Optional<Board> res = repository.findOne(boardRequest.getId());
        res.orElseThrow(() -> new RuntimeException("Board content not found."));
        Board existingBoard = res.get();

        ThumbnailImage existingThumnail = existingBoard.getThumbnailImage();

        List<FileBytes> fileBytes = boardRequest.getFiles();
        List<AttachedFile> attachedFiles = null;
        ThumbnailImage thumbnailImage = null;
        if (!CollectionUtils.isEmpty(fileBytes)) {
            attachedFiles = saveAttachedFiles(fileBytes);
            thumbnailImage = saveThumnail(fileBytes);
        } else {
            attachedFiles = new ArrayList<>();
        }

        /* UPDATE */
        existingBoard.setWirter(boardRequest.getWriter());
        existingBoard.setSubject(boardRequest.getSubject());
        existingBoard.setContent(boardRequest.getContent());
        existingBoard.setAttachedFiles(attachedFiles);
        existingBoard.setFileCount(attachedFiles.size());
        existingBoard.setThumbnailImage(thumbnailImage);
        /* UPDATE */

        if (existingThumnail != null) {
            thumbnailService.delete(existingThumnail.getId());
        }
        BoardResponse response = boardModelToResponse(existingBoard);
        response.setFiles(fileModelToResponse(existingBoard.getAttachedFiles()));
        return response;
    }




    @Override
    public List<BoardResponse> search(BoardSearch searchVO) {
        Pageable pageable = new PageRequest(searchVO.getOffset(), searchVO.getSize());

        List<Board> boards = null;

        if(StringUtils.isEmpty(searchVO.getSubject())) {
            boards = repository.findAllBy(pageable);
        }
        else {
            boards = repository.findBySubjectLike(searchVO.getSubject() + '%', pageable);
        }

        return boards.stream()
                    .map(b -> boardModelToResponse(b))
                    .collect(Collectors.toList());
    }


    private List<FileResponse> fileModelToResponse(List<AttachedFile> attachedFiles) {
        List<FileResponse> result = null;

        if (!CollectionUtils.isEmpty(attachedFiles)) {
            result = attachedFiles.stream()
                    .map(af -> {
                        FileResponse fileResponse = new FileResponse();
                        fileResponse.setId(af.getId());
                        fileResponse.setSize(af.getSize());
                        fileResponse.setType(af.getFileType().toString());
                        fileResponse.setName(af.getOriginalName());
                        return fileResponse;
                    })
                    .collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }
        return result;
    }


    public BoardResponse findOne(Long id)
    {
        Optional<Board> res = repository.findOne(id);
        res.orElseThrow(() -> new RuntimeException("Board content not found."));
        Board board = res.get();
        BoardResponse response = boardModelToResponse(board);
        response.setFiles(fileModelToResponse(board.getAttachedFiles()));
        return response;
    }



}
