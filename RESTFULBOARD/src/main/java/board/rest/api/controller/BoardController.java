package board.rest.api.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import board.rest.api.dto.BoardDto;
import board.rest.api.dto.BoardFileDto;
import board.rest.api.dto.BoardPagingDto;
import board.rest.api.service.BoardFileService;
import board.rest.api.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
	
	private final BoardService boardService;
	private final BoardFileService boardFileService;
	
	/** 게시판 목록 조회 화면 */
	@GetMapping("/board")
	public String openBoardList(@RequestParam(value="currentPage", required = false ,defaultValue = "1") int currentPage, 
														Model model) throws Exception{
		BoardPagingDto boardPaging = boardService.selectBoardList(currentPage);
		
		model.addAttribute("title","게시판 목록");
		model.addAttribute("boardPaging", boardPaging);
		
		return "/board/restBoardList";		
	}
	
	/** 게시글 등록 화면 */
	@GetMapping("/board/view")
	public String openBoardWrite(Model model) throws Exception{
		
		model.addAttribute("title","게시글 등록");
		
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
		
		model.addAttribute("title","게시글 조회");
		model.addAttribute("board", board);
		
		return "/board/restBoardDetail";
	}	
	
	/** 게시글 수정 화면 */
	@GetMapping("/board/view/{boardIdx}")
	public String openBoardModify(@PathVariable("boardIdx") int boardIdx, Model model) throws Exception{
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		
		model.addAttribute("title","게시글 수정");
		model.addAttribute("board", board);
		
		return "/board/restBoardModify";
	}
	
	/** 게시글 수정 처리 */
	@PutMapping("/board/{boardIdx}")
	public String modifyBoard(@PathVariable("boardIdx") int boardIdx, 
													@ModelAttribute BoardDto board,
													@RequestParam("files") List<MultipartFile> files) throws Exception{
		boardService.modifyBoard(board, files);
		
		return "redirect:/board/" + boardIdx;
	}

	/** 게시글 삭제 처리 */
	@DeleteMapping("/board/{boardIdx}")
	public String deleteBoard(@ModelAttribute BoardDto board) throws Exception{
		boardService.deleteBoard(board);
		
		return "redirect:/board";
	}
	
	/** 첨부파일 다운로드 
	 * ResponseEntity : HTTP 응답의 상태 코드, 헤더, 본문 
	 * 									RESTful 웹 서비스에서 응답을 보다 세밀하게 제어하기 위해 사용
	 * Resource : UrlResource의 내용을 body로 보내주기 위해서 사용
	 */
	@GetMapping("/board/file")
	public ResponseEntity<Resource> downloadFile(BoardFileDto file) throws Exception{
		BoardFileDto boardFile = boardFileService.selectFileInfo(file);
		
		//boardFile은 따로 만든 dto 클래스이기 때문에 MultipartFile처럼 isEmpty 메서드가 없으니까 ObjectUitls.isEmpty메서드 사용
		if(ObjectUtils.isEmpty(boardFile)) {
			return null;
		}
		
		String fileName = boardFile.getOriginalFileName();
		
		// 파일의 경로를 바이트배열로 읽어온다.
		UrlResource resource = new UrlResource("file:" + boardFile.getStoredFilePath());
		
		// 파일 이름 URL 주소로 사용하기 전에 인코딩하기
		String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
		
		// Content-Disposition: HTTP 응답의 처리 방식, 브라우저가 반환된 내용을 화면에 표시할 것인지 파일로 다운로드할 것인지 결정한다.
        // attachment : 반환된 내용(인코딩된 파일 이름)을 파일로 다운로드 해라
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        // ResponseEntity.ok() : ResponseEntity의 메서드 상태 코드 200(OK)를 가진 ResponseEntity 객체 생성
        // header() : HTTP 응답의 헤더
        // HttpHeaders.CONTENT_DISPOSITION : HTTP 헤더 중 Content-Disposition이라는 헤더의 이름
        // 헤더에 contentDisposition이라는 변수를 넣어준다. (다운로드할 것을 명시하는 attachment와 파일이름)
        // body(resource) : HTTP 응답의 본문 / 위에서 생성한 UrlResource 객체 - 실제 파일의 내용
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
	}
}
