package board.rest.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDto {
	/* 컬럼 중에 deleted_yn은 dto에 저장하지 않는다.
	 * dto는 자바와 db 사이에 데이터를 전달할 때 dto에 담아서 주고 받는데 deleted_yn은 자바와 db 사이에서 주고 받을 이유가
	 * 없기 때문이다. 조건에 따라 그냥 쿼리 상에서 Y와 N으로 수정하거나 조회해주면 된다.
	 */	
	private int boardIdx;
	private String title;
	private String contents;
	private int hitCnt;
	private String creatorId;
	private LocalDateTime createdDatetime;
	private String updaterId;
	private LocalDateTime updatedDtetime;
	
	private List<BoardFileDto> fileList;
	private int isFile;
}
