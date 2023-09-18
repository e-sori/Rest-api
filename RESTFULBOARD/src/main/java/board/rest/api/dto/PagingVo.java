package board.rest.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PagingVo {
	private int rowsCount;
	private int currentPage;
	private int totalPage;
	private int startPage;
	private int endPage;
	private int startIndex;
	private int rowPerPage = 10;
	public static final int PAGE_NUM = 10;
	
	/** 생성자 메서드*/
	public PagingVo (int currentPage, int rowsCount) throws Exception{
		this.currentPage = currentPage;
		
		// 몇 번째 행 부터 조회할 것인지 계산
		startIndex = (currentPage-1)*rowPerPage;
		
		// 전체 게시글 개수를 한 페이지에 보여질 행의 개수로 나누어서 페이징의 가장 마지막 숫자(총 페이징 수)를 계산한다.
		// double로 계산 후에 소숫점을 올림 처리하기 위해 Math.ceil 사용 후 int로 형변환
		totalPage = (int) Math.ceil((double)rowsCount / rowPerPage);

		// 페이징의 가장 처음 번호(처음에는 당연히 1 -> currentPage가 바뀔 때마다 바뀔 예정)
		startPage = 1;

        // 화면에 보이는 마지막 페이징 숫자(currentPage가 바뀔 때마다 바뀔 예정)
        // rowPerPage는 한 페이지에 보일 행의 개수 / endPage는 한 페이지에 보일 페이징의 마지막 번호
		endPage = (totalPage < PAGE_NUM)? totalPage : PAGE_NUM;
		
		// currentPage가 바뀔 때마다 startPge와 endPage가 바뀐다.
	    // 페이징이 처음에 1~10이기 때문에 currentPage가 6이되는 순간 startPager가 2로, endPage가 11로 바뀐다.
	    // totalPage에도 조건을 거는 이유는 만약 게시글이 적어서 totalPage가 10보다 적을 경우
	    // 페이징이 시작번호와 끝 번호가 바뀔 이유가 없기 때문
	    if(currentPage >= 7 && totalPage > 10) {
	    	startPage = currentPage - 5;
	    	endPage = currentPage + 4;
	        // 현재 페이지에 보이는 페이징 번호가 마지막 페이징 숫자보다 같거나 클 경우
	        // 더 이상 시작 번호와 끝 번호가 바뀌지 않도록 한다.
	        if(endPage >= totalPage) {
	        	startPage = totalPage - 9;
	        	endPage = totalPage;
	        }
	    }
	}
}
