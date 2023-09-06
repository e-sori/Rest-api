package board.rest.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import board.rest.api.dto.BoardDto;

@Mapper
public interface BoardMapper {
		// 게시판 목록 조회
		List<BoardDto> selectBoardList() throws Exception;

}
