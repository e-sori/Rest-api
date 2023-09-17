package board.rest.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BoardPagingDto {
	private List<BoardDto> boardList;
    private PagingVo paging;
}
