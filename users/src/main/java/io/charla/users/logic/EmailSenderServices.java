package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Collections;


@Service
public class EmailSenderServices {


    private final JavaMailSender mailSender;

    private final String emailNotSent;
    private String emailContent;
    private String newEmailContent;
    private final String sender, senderName, subject;

    public EmailSenderServices(JavaMailSender mailSender,
                               @Value("${email-not-sent}") String emailNotSent,
                               @Value("${sender}")  String sender,
                               @Value("${sender-name}") String senderName,
                               @Value("${subject}") String subject,
                               @Value("${email-content}") String emailContent,
                               @Value("${new-email-content}") String newEmailContent) {
        this.mailSender = mailSender;
        this.emailNotSent = emailNotSent;
        this.sender = sender;
        this.senderName = senderName;
        this.subject = subject;
        this.emailContent = emailContent;
        this.newEmailContent = newEmailContent;
    }


    public static String getIp()  {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
                    .flatMap(networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
                    .filter(address -> !address.isLoopbackAddress() && address instanceof Inet4Address)
                    .findFirst()
                    .map(InetAddress::getHostAddress)
                    .orElse(null);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }



    public void SendVerificationCode(User user) {


        //String urlWithCode=  "http://localhost:9001/users"+"/verify?code="+user.getVerificationCode();

        String urlWithCode = "http://" + getIp() + ":9001/users" + "/verify?code=" + user.getVerificationCode();

        String newEmailContentStr = emailContent.replace("{urlWithCode}", urlWithCode);


        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(sender, senderName);
            helper.setTo(user.getEmail());

            helper.setSubject(subject);

            helper.setText(newEmailContentStr, true);


            mailSender.send(mimeMessage);


        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException(emailNotSent);
        }

    }

    public void SendVerificationCodeNewEmail(User user) {


        //String urlWithCode=  "http://localhost:9001/users"+"/verify?code="+user.getVerificationCode();

        String urlWithCode = "http://" + getIp() + ":9001/users" + "/verify-new-email?code=" + user.getVerificationCode();

        String newEmailContentStr = newEmailContent.replace("{urlWithCode}", urlWithCode)
                .replace("{old_email}",user.getEmail())
                .replace("{new_email}",user.getTempEmail());


        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(sender, senderName);
            helper.setTo(user.getTempEmail());

            helper.setSubject(subject);

            helper.setText(newEmailContentStr, true);


            mailSender.send(mimeMessage);


        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException(emailNotSent);
        }

    }


}