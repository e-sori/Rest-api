package board.rest.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;

@Mapper
public interface BoardMapper {
	// 게시글 내역 조회
	List<BoardDto> selectBoardList() throws Exception;
	
	// 게시글 등록
	void insertBoard(BoardDto board) throws Exception;	
	
	// 데이터베이스에 파일 정보 저장
	void insertBoardFileList(List<BoardFileDto> fileList) throws Exception;
	
	// 하나의 게시글에 업로드된 모든 파일 정보 조회
	List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;
	
	// 하나의 게시글에서 다운 받고자하는 파일 정보 조회
	BoardFileDto selectBoardFileInfo(BoardFileDto file) throws Exception;
	
	// 조회수 증가
	void updateHitCount(int boardIdx) throws Exception;
	
	// 게시글 상세 조회
	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	// 게시글 수정
	void modifyBoard(BoardDto board) throws Exception;
	
	// 게시글 삭제
	void deleteBoard(int boardIdx) throws Exception;
}
