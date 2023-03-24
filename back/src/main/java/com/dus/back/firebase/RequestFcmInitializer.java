package com.dus.back.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Component
public class RequestFcmInitializer {

    @PostConstruct
    public void initialize(){

        try{
            ClassPathResource resource = new ClassPathResource("private/firebase-admin.json");
            FileInputStream serviceAccount = new FileInputStream(resource.getFile());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (FileNotFoundException e){
            log.error("firebase admin api 관련 json 파일을 찾을 수 없음");
        }catch (IOException e){
            log.error("firebase admin api 관련 json 파일을 정상적으로 로드 하지 못함");
        }

    }

}
