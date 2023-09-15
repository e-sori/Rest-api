package board.rest.api.service;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import board.rest.api.dto.BoardFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardFileService {
	// 프로퍼티 파일, 환경 변수, 시스템 속성 등의 외부 설정 값을 Spring 빈에 주입한다.
	// application.properties(프로퍼티 파일)에 작성해 둔 file.path를 사용하기 위해 썼다.
	// 즉, 프로퍼티 파일에 작성해둔 file.path라는 값을 필드의 filePath에 주입하겠다는 뜻
	@Value("${file.path}")
	private String filePath;
	 
	public List<BoardFileDto> uploadFile(int boardIdx, List<MultipartFile> files) throws Exception {		    	
		// 업로드한 파일이 없으면 null 반환
		// 만약 files.isEmpty() 하면 false 떠서 return이 안된다.
		// files는 데이터 타입이 List<MultipartFile> 이기 때문에 일단 List 자체에는 비어있는 MultipartFile이 존재하기 때문
		// 실제로 files.size()는 1이고 files.get(0).getSize()는 0이 나온다.
		// 참고로 size()는 List 같은 컬렉션에서 제공하는 메서드고
		// getSize()는 MultipartFile에서 제공하는 메서드라는 점에서 차이를 알 수 있다.
		// files.isEmpty()는 files가 String이거나 컬렉션 등 특정 타입에서만 사용 가능하다.
		// 다양한 타입에서 사용할 경우에는 ObjectUtils.isEmpty(files) 사용
		// 하지만, MultiparFile 자체에서 isEmpty()를 제공하기 때문에 files.isEmpty()를 사용
		if(files.get(0).isEmpty()) {
		    return null;
		}
		
		// 파일을 저장할 경로 지정하고 폴더 생성하기
		// 날짜를 20230911 형식으로 가져오도록 미리 형식 선언
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyMMdd");
		// 파일 저장 당시 날짜 가져오기
		ZonedDateTime currentDate = ZonedDateTime.now();
		// 파일 저장 경로 설정해주기 (날짜를 미리 선언해둔 형식으로 바꿔줘라)
		String path = filePath + currentDate.format(dateFormat);	    
		// File 객체는 파일 또는 디렉토리의 경로 정보를 저장하고 있다. 그래서 생성할 때 path 넣어준다.
		// File 객체가 제공하는 메서드 exists,isFile,isDirectory,mkdir,mkdirs...
		File file =new File(path);
		// 만약 지정된 경로에  해당 폴더가 존재하지 않을 경우 폴더를 만든다.
		// exists() : 파일 및 디렉토리 존재여부 확인 (파일 : isFile() / 디렉토리 : isDirectory())
		if(file.isDirectory() == false) {
		    // mkdir() : 경로의 가장 마지막 폴더만 생성 / mkdirs() : 없는 폴더 전부 생성
		    // 예를 들어 경로가 /a/b/c 인데 a라는 폴더만 있을 경우 mkdir()는 c 폴더만 / mkdirs()는 b,c 폴더 생성
		    file.mkdirs();
		}
		 
		List<BoardFileDto> fileList = new ArrayList<>();
		 
		for (MultipartFile oneFile : files) {
		    // 원래 파일 이름 추출 MultipartFile에서 제공하는 메서드 (확장자 포함)
		    String originalFileName = oneFile.getOriginalFilename();
		 
		    // 확장자 추출
		    // lastIndexOf() : 문자열의 끝에서 부터 해당 문자열을 찾아 인덱스를 반환 (확장자명은 .으로 구분되어 있기 때문)
		    // substring(숫자) : 해당 인덱스부터 끝까지 반환 
		    // 만약 파일 이름이 img.gif 였다면 substring(3) -> .gif 반환
		    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		 
		    // 새로운 파일 이름 (확장자 포함)
		    // 새로운 파일 이름은 유일성을 보장해야한다. (중복 x)
		    // UUID라는 객체는 따로 생성자 생성 없이 사용 가능
		    // UUID.randomUUID()는 유일성을 보장하는 랜덤 객체 반환
		    // 즉 UUID 객체를 반환하기 때문에 Strign으로 형변환
		    String uniqueObject =  UUID.randomUUID().toString();
		 
		    // 문자열로 변환한 UUID 객체와 확장자 결합
		    String newFileName = uniqueObject + fileExtension;
		 
		    BoardFileDto boardOneFile = new BoardFileDto();
		 
		    boardOneFile.setBoardIdx(boardIdx);
		    boardOneFile.setFileSize(oneFile.getSize());
		    boardOneFile.setOriginalFileName(originalFileName);
		    boardOneFile.setStoredFilePath(path + "/" + newFileName);
		 
		    fileList.add(boardOneFile);
		 
		    // File 객체에 파일 저장 (파일 확장자 포함한 파일경로)
		    file = new File(path + "/" + newFileName);
		    // onFile을 file 경로로 전송(저장)한다.
		    oneFile.transferTo(file);
		}
	return fileList;
	}
}