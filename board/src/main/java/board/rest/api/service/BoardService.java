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
	
	// 게시글 내역 조회
	public List<BoardDto> selectBoardList() throws Exception{
		List<BoardDto> boardList = boardMapper.selectBoardList();
		
		return boardList;
	}
	
	// 게시글 등록
	public void insertBoard(BoardDto board) throws Exception{
		boardMapper.insertBoard(board);
	}
	
	// 게시글 상세 내용 조회
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{
		// 상세 내용 조회 -> 조회수 증가
		boardMapper.updateHitCount(boardIdx);
		
		// 상세 내용 가져오기
		BoardDto boardDetail = boardMapper.selectBoardDetail(boardIdx);
		
		return boardDetail;
	}
	
	// 게시글 수정
	public void modifyBoard(BoardDto board) throws Exception{
		boardMapper.modifyBoard(board);
	}
	
	// 게시글 삭제
	public void deleteBoard(int boardIdx) throws Exception{
		boardMapper.deleteBoard(boardIdx);
	}
	
}
