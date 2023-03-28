package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    private final ResourceLoader resourceLoader;

    public ExcelServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResponseEntity<Resource> download(HttpServletResponse response) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/excel/msg-contacts.xlsx");
        File file = resource.getFile();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getName() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                .body(resource);
    }

    @Override
    public List<TelDTO> getTelDTOList(MultipartFile file, String msg, String myPhoneNumber) {
        List<TelDTO> telDTOList = new ArrayList<>();

        try{
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            XSSFSheet sheet = workbook.getSheetAt(0);
            log.info("읽은 엑셀 파일 행 수: {}", String.valueOf(sheet.getPhysicalNumberOfRows()));
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                TelDTO telDTO = new TelDTO();
                Row row = sheet.getRow(i);

                if(row == null){
                    continue;
                }

                Cell cell = row.getCell(0);
                if(cell == null){
                    continue;
                }

                String targetPhoneNumber = cell.getStringCellValue();
                log.info("번호: {}", targetPhoneNumber);

                telDTO.setTargetPhoneNumber(getRegularNumber(targetPhoneNumber));
                telDTO.setMsg(msg);
                telDTO.setMyPhoneNumber(myPhoneNumber);

                telDTOList.add(telDTO);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        return telDTOList;

    }

    @Override
    public String getRegularNumber(String targetPhoneNumber) {
        return targetPhoneNumber.replace("[^0-9]", "");
    }

}
