package board.rest.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;
import board.rest.api.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	private final BoardFileService boardFileService;
	
	// 게시글 내역 조회
	public List<BoardDto> selectBoardList() throws Exception{
		List<BoardDto> boardList = boardMapper.selectBoardList();
		
		return boardList;
	}
	
	// 게시글 등록
	public void insertBoard(BoardDto board, List<MultipartFile> files) throws Exception{
	    // form 데이터 등록
	    // dao에서 등록된 pk(boardIdx) 받아오도록 작성함 - useGeneratedKeys
	    boardMapper.insertBoard(board);
	 
	    // 파일을 서버 컴퓨터에 저장 후 파일 정보들 DTO에 받아주기
	    // useGeneratedKeys로 받아온 새로 생성된 boardIdx를 매개변수로 넣어준다.
	    List<BoardFileDto> fileList = boardFileService.uploadFile(board.getBoardIdx(), files);
	 
	    // 데이터베이스에 파일 정보 저장
	    if(fileList != null) {
	        boardMapper.insertBoardFileList(fileList);
	    }
	}
	
	// 게시글 상세 내용 조회
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{		
	    // 상세 내용 조회 -> 조회수 증가
	    boardMapper.updateHitCount(boardIdx);
	 
	    // 상세 내용 가져오기
	    BoardDto boardDetail = boardMapper.selectBoardDetail(boardIdx);
	 
	    // 파일들 정보 가져오기
	    List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
	    boardDetail.setFileList(fileList);
	 
	    return boardDetail;
	}
	
	// 하나의 게시글에서 다운 받고자하는 파일 정보 조회
	public BoardFileDto selectBoardFileInfo(BoardFileDto file) throws Exception{	
			BoardFileDto boardFile = boardMapper.selectBoardFileInfo(file);
		return boardFile;
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