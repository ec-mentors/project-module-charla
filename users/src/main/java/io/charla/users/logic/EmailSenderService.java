package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Collections;


@Service
public class EmailSenderService {


    private final JavaMailSender mailSender;

    private final String emailNotSent;
    private String emailContent;
    private final String sender, senderName, subject;

    public EmailSenderService(JavaMailSender mailSender,
                              @Value("${email-not-sent}") String emailNotSent,
                              @Value("${sender}")  String sender,
                              @Value("${sender-name}") String senderName,
                              @Value("${subject}") String subject,
                              @Value("${email-content}") String emailContent) {
        this.mailSender = mailSender;
        this.emailNotSent = emailNotSent;
        this.sender = sender;
        this.senderName = senderName;
        this.subject = subject;
        this.emailContent = emailContent;
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


        emailContent = emailContent.replace("{urlWithCode}", urlWithCode);


        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(sender, senderName);
            helper.setTo(user.getEmail());

            helper.setSubject(subject);

            helper.setText(emailContent, true);

            mailSender.send(mimeMessage);


        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException(emailNotSent);
        }

    }

}

/*
Socket socket = new Socket();
socket.connect(new InetSocketAddress("google.com", 80));
System.out.println(socket.getLocalAddress());
socket.close();
 */

/*
public static  String getSimpleIp() throws SocketException, UnknownHostException{
        String ip= "";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        return   ip = socket.getLocalAddress().getHostAddress();
        }
    }
 */

/*
public static String getAnotherIp() {
    try {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();

                final String ip = addr.getHostAddress();
                if(Inet4Address.class == addr.getClass()) return ip;
            }
        }
    } catch (SocketException e) {
        throw new RuntimeException(e);
    }
    return null;
}

 */