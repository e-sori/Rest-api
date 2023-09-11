package board.rest.api.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import board.rest.api.dto.BoardDto;
import board.rest.api.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;
	
	// 게시판 목록 조회 화면
	@GetMapping("")
	public String openBoardList(Model model) throws Exception{
		List<BoardDto> list = boardService.selectBoardList();
		
		model.addAttribute("list", list);
		
		return "/board/restBoardList";		
	}
	
	// 게시글 등록 화면
	@GetMapping("/write")
	public String openBoardWrite(Model model) throws Exception{
		return "/board/restBoardWrite";
	}
	
	// 게시글 등록 처리
	@PostMapping("/write")
	public String insertBoard(BoardDto board, @RequestParam("files") List<MultipartFile> files) throws Exception{
	    boardService.insertBoard(board, files);
	    
	    return "redirect:/board";
	}
	
	// 게시글 상세 조회 화면
	@GetMapping("/{boardIdx}")
	public String openBoardDetail(@PathVariable("boardIdx") int boardIdx, Model model) throws Exception{
		// @PathVariable : 요청(Request)을 받아와서 리소스에 사용 가능
		// ("매핑에 사용할 변수 이름") 변수와 묶어 줄 매개변수
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		
		model.addAttribute("board", board);
		
		return "/board/restBoardDetail";
	}
	
	// 게시글 수정 화면
	@GetMapping("/modify/{boardIdx}")
	public String openBoardModify(@PathVariable("boardIdx") int boardIdx, Model model) throws Exception{
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		model.addAttribute("board", board);
		
		return "/board/restBoardModify";
	}
	
	// 게시글 수정 처리
	@PutMapping("/modify/{boardIdx}")
	public String modifyBoard(@PathVariable("boardIdx") int boardIdx, BoardDto board) throws Exception{
		boardService.modifyBoard(board);
		
		return "redirect:/board/" + boardIdx;
	}
	
	// 게시글 삭제 처리
	@DeleteMapping("/delete/{boardIdx}")
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		
		return "redirect:/board";
	}
}
