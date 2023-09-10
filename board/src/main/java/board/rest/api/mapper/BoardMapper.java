package board.rest.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import board.rest.api.dto.BoardDto;

@Mapper
public interface BoardMapper {
	// 게시글 내역 조회
	List<BoardDto> selectBoardList() throws Exception;
	
	// 게시글 등록
	void insertBoard(BoardDto board) throws Exception;	
		
	// 조회수 증가
	void updateHitCount(int boardIdx) throws Exception;
	
	// 게시글 상세 조회
	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	// 게시글 수정
	void modifyBoard(BoardDto board) throws Exception;
	
	// 게시글 삭제
	void deleteBoard(int boardIdx) throws Exception;
}
