package board.rest.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;
import board.rest.api.dto.BoardPagingDto;
import board.rest.api.dto.PagingVo;
import board.rest.api.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
	private final BoardMapper boardMapper;
	private final BoardFileService boardFileService;
	
	/** 페이징 */
	public PagingVo selectPaging(int currentPage) throws Exception{
		 int rowsCount = boardMapper.selectCount();	
		 PagingVo paging = new PagingVo(currentPage, rowsCount);
		 
		 return paging;		
	}
	
	/** 게시글 목록 조회 */
	public BoardPagingDto selectBoardList(int currentPage) throws Exception{
		PagingVo paging = selectPaging(currentPage);
		List<BoardDto> boardList = boardMapper.selectBoardList(paging);		
		BoardPagingDto boardPaging = new BoardPagingDto(boardList, paging);		
		
		for(BoardDto board : boardList) {
			int boardIdx = board.getBoardIdx();
			List<BoardFileDto> fileList = boardMapper.selectFileList(boardIdx);
			board.setFileList(fileList);
		}						
		
		return boardPaging;
	}
	
	/** 게시글 등록 */
	public void insertBoard(BoardDto board, List<MultipartFile> files) throws Exception{
	    // form 데이터 등록
	    // dao에서 등록된 pk(boardIdx) 받아오도록 작성함 - useGeneratedKeys
	    if(board.getTitle().trim().isEmpty()) {board.setTitle("제목없음");}
	    boardMapper.insertBoard(board);
	 
	    // 첨부파일 등록
	    boardFileService.insertFile(board, files);
	}
	
	/** 게시글 상세 내용 조회 */
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{		
	    // 상세 내용 조회 -> 조회수 증가
	    boardMapper.updateHitCount(boardIdx);
	 
	    // 상세 내용 가져오기
	    BoardDto boardDetail = boardMapper.selectBoardDetail(boardIdx);
	 
	    // 파일들 정보 가져오기
	    List<BoardFileDto> fileList = boardMapper.selectFileList(boardIdx);
	    boardDetail.setFileList(fileList);
	 
	    return boardDetail;
	}
	
	/** 게시글 수정 */
	public void modifyBoard(BoardDto board, List<MultipartFile> files) throws Exception{
		// 게시글 수정
		boardMapper.modifyBoard(board);
		
		// 첨부파일 등록
	    boardFileService.insertFile(board, files);
	    
	    // 첨부파일 삭제
	    boardFileService.deleteFile(board);	    
	}
	
	/** 게시글 삭제 */
	public void deleteBoard(BoardDto board) throws Exception{
		// 게시글 삭제
		int boardIdx = board.getBoardIdx();
		boardMapper.deleteBoard(boardIdx);  
		
		// 첨부파일 삭제
	    boardFileService.deleteFile(board);	    
	}
	
}
