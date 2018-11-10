package com.example.android.board;

import com.example.android.board.vo.BoardResponse;
import com.example.android.board.vo.BoardSearch;
import com.example.android.board.vo.BoardRequest;

import java.io.IOException;
import java.util.List;

public interface BoardService {


    public BoardResponse save(BoardRequest boardRequest) throws IOException;


    public BoardResponse update(BoardRequest boardRequest) throws IOException;


    public List<BoardResponse> search(BoardSearch searchVO);


    public BoardResponse findOne(Long id);


    public void delete(Long id);

}
