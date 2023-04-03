package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/msg")
public class MsgController {
    private final MsgService msgService;
    private final ExcelService excelService;

    public MsgController(MsgService msgService, ExcelService excelService) {
        this.msgService = msgService;
        this.excelService = excelService;
    }

    @PostMapping("/sms")
    @ResponseBody
    public boolean sendSms(TelDTO telDTO, Authentication authentication) {

        if(!telDTO.getUserId().equals(authentication.getName())){
            return false;
        }

        return msgService.sendSms(telDTO);
    }

    @PostMapping("/mms")
    @ResponseBody
    public boolean sendMms(TelDTO telDTO, Authentication authentication) {

        if(!telDTO.getUserId().equals(authentication.getName())){
            return false;
        }

        return msgService.sendSms(telDTO);
    }

    @GetMapping("/excel-download")
    public ResponseEntity<Resource> downloadExcel(HttpServletResponse response) {

        try {
            return excelService.download(response);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/excel")
    @ResponseBody
    public boolean uploadExcel(@RequestParam("file") MultipartFile contactFile, String msg, String myPhoneNumber){

        if(contactFile.isEmpty()){
            log.error("엑셀 파일 업로드 실패: 파일이 비어있음");
            return false;
        }
        log.info("업로드 된 엑셀 파일 사이즈: {}", contactFile.getSize());

        String temp = "temp";
        List<TelDTO> telDTOList = excelService.loadTelDTOList(contactFile, temp, "01027405593");
        return msgService.sendManySms(telDTOList);

    }


}
