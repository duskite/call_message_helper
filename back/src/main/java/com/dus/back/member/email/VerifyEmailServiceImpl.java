package com.dus.back.member.email;

import com.sun.mail.util.MailConnectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class VerifyEmailServiceImpl implements VerifyEmailService{
    private final JavaMailSender javaMailSender;

    public VerifyEmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendTempPassword(String receiveEmail, String tempPassword) throws MailConnectException {

        log.info("메일 발송 요청 주소: {}", receiveEmail);

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(receiveEmail);
            simpleMailMessage.setText(tempPassword);
            simpleMailMessage.setSubject(subject);
            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            log.error("이메일 발송 오류: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public String makeTempPassword() {

        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetStringLength = 10;

        Random random = new Random();
        String tempPassword = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return tempPassword;
    }

}
