package com.example.android.board;

import com.example.android.board.vo.BoardSearch;
import com.example.android.board.vo.BoardVO;

import java.io.IOException;
import java.util.List;

public interface BoardService {


    public BoardVO save(BoardVO boardVO) throws IOException;


    public BoardVO update(BoardVO boardVO) throws IOException;


    public List<BoardVO> search(BoardSearch searchVO);


    public BoardVO findOne(Long id);


    public void delete(Long id);

}
