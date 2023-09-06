package board.rest.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import board.rest.api.dto.BoardDto;
import board.rest.api.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	
	// 게시글 등록
	public void insertBoard(BoardDto board) throws Exception{
		boardMapper.insertBoard(board);
	}
	
	// 게시글 내역 조회
	public List<BoardDto> selectBoardList() throws Exception{
		return boardMapper.selectBoardList();
	}
}
