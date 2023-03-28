package com.dus.back.tel.msg;

import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.tel.TelDTO;
import org.springframework.http.ResponseEntity;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ExcelService {
    /**
     * 문자 대량 발송 기본 양식 다운로드 기능 제공
     * @param response
     * @return
     * @throws IOException
     */
    ResponseEntity<Resource> download(HttpServletResponse response) throws IOException;

    /**
     * 엑셀 파일에서 휴대폰 번호 리스트 추출
     * @param file
     * @return
     */
    List<TelDTO> getTelDTOList(MultipartFile file, String msg, String myPhoneNumber);

    /**
     * 번호를 01012345678 과 같이 숫자로만 이루어지게 변경
     * @param targetPhoneNumber
     * @return
     */
    String getRegularNumber(String targetPhoneNumber);


}
