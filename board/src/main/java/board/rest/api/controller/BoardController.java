package board.rest.api.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.rest.api.dto.BoardDto;
import board.rest.api.service.BoardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;
	
	// 게시글 등록 처리
	@PostMapping("/write")
	public String insertBoard(BoardDto board) throws Exception{
		boardService.insertBoard(board);
		
		return "redirect:/board";
	}
	
	// 게시글 등록 화면
	@GetMapping("/write")
	public String openBoardWrite(Model model) throws Exception{
		return "/board/restBoardWrite";
	}
	
	// 게시판 목록 조회 화면
	@GetMapping("")
	public String openBoardList(Model model) throws Exception{
		List<BoardDto> list = boardService.selectBoardList();
		
		model.addAttribute("list", list);
		
		return "/board/restBoardList";		
	}
}
