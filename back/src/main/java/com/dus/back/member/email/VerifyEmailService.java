package com.dus.back.member.email;

import com.sun.mail.util.MailConnectException;

public interface VerifyEmailService {

    String senderName = "Call And Message Helper";
    String senderMail = "infocallandmsgHelper@gmail.com";
    String mailPassword = "";
    String subject = senderName + ", 임시 비밀번호";

    /**
     * 임시 비밀번호 이메일 발송
     * @param receiveEmail
     * @return
     */
    void sendTempPassword(String receiveEmail, String tempPassword) throws MailConnectException;

    /**
     * 0 ~ z 사이의 10자리 임시 비밀번호 생성
     * @return
     */
    String makeTempPassword();

}
