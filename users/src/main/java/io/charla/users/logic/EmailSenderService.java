package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;

import static io.charla.users.logic.IPAddressExample.getIp;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;


    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void SendVerificationCode(User user) {



        String Sender = "charla23@gmx.at";

        //String urlWithCode=  "http://localhost:9001/users"+"/verify?code="+user.getVerificationCode();

        String urlWithCode=  "http://" + getIp()  +":9001/users"+"/verify?code="+user.getVerificationCode();

        String SenderName = "Charla group";
        String Subject = "Signup verification";


        String emailContent =
                "<style>\n" +
                        "    body{font-family:Arial,sans-serif;background-color:#f2f2f2;padding:20px}\n" +
                        "    .container{position:relative;max-width:400px;margin:0 auto;background-color:#fff;padding:20px;border-radius:5px;box-shadow:0 2px 4px rgba(0,0,0,.1)}\n" +
                        "    a{color:#fff;text-decoration:none;background-color:#007bff;padding:7px 15px;border-radius:5px}\n" +
                        "    a:hover{background-color:#7b9fc6}\n" +
                        "</style>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <p>Dear <strong>HumanTalk</strong> subscriber,</p>\n" +
                        "        <p>Please click the button below to verify your account and complete your registration:</p>\n" +
                        "        <h5><a href= \"" + urlWithCode +"\"  target=\"_self\">VERIFY</a></h5>\n" +
                        "        <p>Thank you,</p>\n" +
                        "        <p>Charla Team</p>\n" +
                        "    </div>\n" +
                        "</body>";



        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(Sender, SenderName);
            helper.setTo(user.getEmail());

            helper.setSubject(Subject);

            helper.setText(emailContent,true);

            mailSender.send(mimeMessage);


        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("failed to send email");
        }

    }

}


 class IPAddressExample {
    public static String getIp() {
        try {
            NetworkInterface wifiInterface = NetworkInterface.getByName("wlan0");
            return Collections.list(wifiInterface.getInetAddresses())
                    .stream()
                    .filter(address -> !address.isLoopbackAddress())
                    .findFirst()
                    .map(InetAddress::getHostAddress)
                    .orElse(null);

        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }
}
