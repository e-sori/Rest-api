package board.rest.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;
import board.rest.api.dto.PagingVo;

@Mapper
public interface BoardMapper {
	/** 게시글 개수 조회 */
	int selectCount() throws Exception;
	
	/** 게시글 내역 조회 */
	List<BoardDto> selectBoardList(PagingVo paging) throws Exception;
	
	/** 게시글 등록 */
	void insertBoard(BoardDto board) throws Exception;	
	
	/** 조회수 증가 */
	void updateHitCount(int boardIdx) throws Exception;
	
	/** 게시글 상세 조회 */
	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	/** 게시글 수정 */
	void modifyBoard(BoardDto board) throws Exception;
	
	/** 게시글 삭제 */
	void deleteBoard(int boardIdx) throws Exception;
	
	/** 데이터베이스에 파일 정보 저장 */
	void insertFileList(List<BoardFileDto> fileList) throws Exception;
		
	/** 업로드된 모든 파일 정보 조회  by 게시판 번호 */
	List<BoardFileDto> selectFileList(int boardIdx) throws Exception;
	
	/** 다운 받고자하는 파일 정보 조회 by 게시판 번호, 첨부파일 번호 */
	BoardFileDto selectFileInfo(BoardFileDto file) throws Exception;
	
	/** 파일 데이터베이스에서 삭제 상태로 바꾸기 */
	void deleteFile(List<Integer> idsx) throws Exception;
}
