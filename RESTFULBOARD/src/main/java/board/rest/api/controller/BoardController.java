package board.rest.api.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;
import board.rest.api.service.BoardFileService;
import board.rest.api.service.BoardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	private final BoardFileService boardFileService;
	
	/** 게시판 목록 조회 화면 */
	@GetMapping("/board")
	public String openBoardList(Model model) throws Exception{
		List<BoardDto> list = boardService.selectBoardList();
		
		model.addAttribute("list", list);
		
		return "/board/restBoardList";		
	}
	
	/** 게시글 등록 화면 */
	@GetMapping("/board/view")
	public String openBoardWrite(Model model) throws Exception{
		return "/board/restBoardWrite";
	}
	
	/** 게시글 등록 처리(+ 첨부파일 업로드) */
	@PostMapping("/board")
	public String insertBoard(BoardDto board, @RequestParam("files") List<MultipartFile> files) throws Exception{
	    boardService.insertBoard(board, files);
	    
	    return "redirect:/board";
	}
	
	/** 게시글 상세 조회 화면(+ 첨부파일 조회) */
	@GetMapping("/board/{boardIdx}")
	public String openBoardDetail(@PathVariable("boardIdx") int boardIdx, Model model) throws Exception{
		// @PathVariable : 요청(Request)을 받아와서 리소스에 사용 가능
		// ("매핑에 사용할 변수 이름") 변수와 묶어 줄 매개변수
		BoardDto board = boardService.selectBoardDetail(boardIdx);		
		
		model.addAttribute("board", board);
		
		return "/board/restBoardDetail";
	}	
	
	/** 게시글 수정 화면 */
	@GetMapping("/board/view/{boardIdx}")
	public String openBoardModify(@PathVariable("boardIdx") int boardIdx, Model model) throws Exception{
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		model.addAttribute("board", board);
		
		return "/board/restBoardModify";
	}
	
	/** 게시글 수정 처리 */
	@PutMapping("/board/{boardIdx}")
	public String modifyBoard(@PathVariable("boardIdx") int boardIdx, BoardDto board) throws Exception{
		boardService.modifyBoard(board);
		
		return "redirect:/board/" + boardIdx;
	}
	
	/** 게시글 삭제 처리 */
	@DeleteMapping("/board/{boardIdx}")
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		
		return "redirect:/board";
	}
	
	/** 첨부파일 다운로드 */
	@GetMapping("/board/file")
	public void downloadBoardFile(BoardFileDto file, HttpServletResponse response) throws Exception{
		BoardFileDto boardFile = boardFileService.selectBoardFileInfo(file);
		
		//boardFile은 따로 만든 dto 클래스이기 때문에 MultipartFile처럼 isEmpty 메서드가 없으니까 ObjectUitls.isEmpty메서드 사용
		if(!ObjectUtils.isEmpty(boardFile)) {
			String fileName = boardFile.getOriginalFileName();
			int fileSize = (int)boardFile.getFileSize();
			
			// 파일의 경로를 바이트배열로 읽어온다.
			UrlResource resource = new UrlResource("file:" + boardFile.getStoredFilePath());
			
			// 파일 이름 URL 주소로 사용하기 전에 인코딩하기
			String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
			
			// Content-Disposition: HTTP 응답의 처리 방식, 브라우저가 반환된 내용을 화면에 표시할 것인지 파일로 다운로드할 것인지 결정한다.
	        // attachment : 반환된 내용을 파일로 다운로드 해라
	        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
	        response.setHeader("Content-Disposition", contentDisposition);
	        
	        // ContentType : 응답의 본문(body)의 미디어 타입 (데이터의 종류, 포맷에 대한 정보 제공)
	        // application/octet-stream : 바이너리 데이터
	        response.setContentType("application/octet-stream");
	        
	        // 파일의 크기 설정(바이트 단위)
	        response.setContentLengthLong(fileSize);
	        
	        // 파일의 바이트 수 가져오기 위해서 인풋 스트림 생성
	        InputStream inputStream = resource.getInputStream();
	        
	        // 다운로드 파일 데이터를 클라이언트에 전달하기 위해 response에 출력할 출력 스트림을 생성	        I
	        OutputStream outStream = response.getOutputStream();	       

	        // 파일을 바이트 배열로 가져오기
	        byte[] byteFile = resource.getContentAsByteArray();

	        int byteLength;
	        // inputStream.read(byteFile) : 바이트 배열의 길이
	        // 바이트 배열을 루프를 돌면서 하나의 바이트씩 읽는다. 더 이상 읽을 바이트가 없으면 -1 리턴
			while ((byteLength = inputStream.read(byteFile)) > 0) {
				// write(바이트배열, 바이트배열에서 데이터의 시작 위치, 바이트배열의 테이터 길이]
				// 바이트 배열에서 시작 위치 부터 데이터 길이까지 데이터를 출력 스트림으로 보관
				outStream.write(byteFile, 0, byteLength);
			}			
			outStream.flush();
			inputStream.close();   
			outStream.close();  
		}
	}
}
