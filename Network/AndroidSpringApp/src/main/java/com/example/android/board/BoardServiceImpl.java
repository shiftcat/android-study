package com.example.android.board;


import com.example.android.ExceptionFunction;
import com.example.android.attchedfile.FileType;
import com.example.android.attchedfile.vo.FileBytes;
import com.example.android.board.vo.BoardSearch;
import com.example.android.board.vo.BoardVO;
import com.example.android.models.Board;
import com.example.android.models.AttachedFile;
import com.example.android.attchedfile.FileService;
import com.example.android.models.ThumbnailImage;
import com.example.android.thumbnail.ThumbnailService;
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

    public static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
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


    @Transactional
    public BoardVO save(BoardVO boardVO) throws IOException {
        List<FileBytes> fileBytes = boardVO.getFiles();
        List<AttachedFile> attachedFiles = null;
        ThumbnailImage thumbnailImage = null;
        if (!CollectionUtils.isEmpty(fileBytes)) {
            attachedFiles = saveAttachedFiles(fileBytes);
            thumbnailImage = saveThumnail(fileBytes);
        } else {
            attachedFiles = new ArrayList<>();
        }

        Board board = new Board();
        board.setWirter(boardVO.getWriter());
        board.setSubject(boardVO.getSubject());
        board.setContent(boardVO.getContent());
        board.setAttachedFiles(attachedFiles);
        board.setFileCount(attachedFiles.size());
        board.setThumbnailImage(thumbnailImage);

        Board newBoard = repository.save(board);
        boardVO.setId(newBoard.getId());
        boardVO.setFiles(fileModelToVO(attachedFiles));
        boardVO.setThumbnail(thumbnailToVO(board.getThumbnailImage()));
        return boardVO;
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

//        if(!CollectionUtils.isEmpty(existingBoard.getAttachedFiles())) {
//            existingBoard.getAttachedFiles().stream()
//                    .forEach(af -> {
//                        fileService.delete(af.getId());
//                    });
//        }
    }


    @Transactional
    public BoardVO update(BoardVO boardVO) throws IOException {
        Optional<Board> res = repository.findOne(boardVO.getId());
        res.orElseThrow(() -> new RuntimeException("Board content not found."));
        Board existingBoard = res.get();

//        List<AttachedFile> existingFiles = existingBoard.getAttachedFiles();
//        if(!CollectionUtils.isEmpty(existingFiles)) {
//            existingFiles.stream().forEach(eaf -> {
//                fileService.delete(eaf.getId());
//            });
//        }

        ThumbnailImage existingThumnail = existingBoard.getThumbnailImage();

        List<FileBytes> fileBytes = boardVO.getFiles();
        List<AttachedFile> attachedFiles = null;
        ThumbnailImage thumbnailImage = null;
        if (!CollectionUtils.isEmpty(fileBytes)) {
            attachedFiles = saveAttachedFiles(fileBytes);
            thumbnailImage = saveThumnail(fileBytes);
        } else {
            attachedFiles = new ArrayList<>();
        }

        /* UPDATE */
        existingBoard.setWirter(boardVO.getWriter());
        existingBoard.setSubject(boardVO.getSubject());
        existingBoard.setContent(boardVO.getContent());
        existingBoard.setAttachedFiles(attachedFiles);
        existingBoard.setFileCount(attachedFiles.size());
        existingBoard.setThumbnailImage(thumbnailImage);
        /* UPDATE */

        if (existingThumnail != null) {
            thumbnailService.delete(existingThumnail.getId());
        }

        return boardModelToVO(existingBoard);
    }


    private List<FileBytes> fileModelToVO(List<AttachedFile> attachedFiles) {
        List<FileBytes> result = null;

        if (!CollectionUtils.isEmpty(attachedFiles)) {
            result = attachedFiles.stream()
                    .map(af -> {
                        FileBytes fileBytes = new FileBytes();
                        fileBytes.setId(af.getId());
                        fileBytes.setSize(af.getSize());
                        fileBytes.setFileName(af.getOriginalName());
                        return fileBytes;
                    })
                    .collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

    private FileBytes thumbnailToVO(ThumbnailImage thumbnailImage)
    {
        FileBytes fileBytes = new FileBytes();
        fileBytes.setId(thumbnailImage.getId());
        fileBytes.setFileName(thumbnailImage.getOriginalName());
        fileBytes.setSize(thumbnailImage.getSize());
        return fileBytes;
    }



    private BoardVO boardModelToVO(Board board)
    {
        List<AttachedFile> attachedFiles = board.getAttachedFiles();
        List<FileBytes> frs = fileModelToVO(attachedFiles);

        BoardVO boardVO = new BoardVO();
        boardVO.setId(board.getId());
        boardVO.setWriter(board.getWirter());
        boardVO.setSubject(board.getSubject());
        boardVO.setContent(board.getContent());
        boardVO.setFiles(frs);
        boardVO.setThumbnail(thumbnailToVO(board.getThumbnailImage()));
        return boardVO;
    }


    @Override
    public List<BoardVO> search(BoardSearch searchVO) {
        Pageable pageable = new PageRequest(searchVO.getOffset(), searchVO.getSize());

        List<Board> boards = null;

        if(StringUtils.isEmpty(searchVO.getSubject())) {
            boards = repository.findAllBy(pageable);
        }
        else {
            boards = repository.findBySubjectLike(searchVO.getSubject() + '%', pageable);
        }

        return boards.stream()
                .map(b -> {
                    return boardModelToVO(b);
                })
                .collect(Collectors.toList());
    }


    public BoardVO findOne(Long id)
    {
        Optional<Board> res = repository.findOne(id);
        res.orElseThrow(() -> new RuntimeException("Board content not found."));
        return boardModelToVO(res.get());
    }



}
