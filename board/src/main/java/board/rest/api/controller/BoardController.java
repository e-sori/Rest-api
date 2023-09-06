package board.rest.api.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import board.rest.api.dto.BoardDto;
import board.rest.api.service.BoardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	// 게시판 목록 조회 화면
	@GetMapping("/board")
	public String openBoardList(Model model) throws Exception{
		List<BoardDto> list = boardService.selectBoardList();
		
		model.addAttribute("list", list);
		
		return "/board/restBoardList";
		
	}
	

}
