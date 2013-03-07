package com.naphaso.alice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 4:40 PM
 */
public class EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Value("#{appProperties['email.owner']}")
    private String owner;
    @Value("#{appProperties['email.address']}")
    private String address;
    @Value("#{appProperties['email.password']}")
    private String password;

    private class SendEmailTask implements Runnable {
        private String to;
        private String subject;
        private String body;
        public SendEmailTask(String to, String subject, String body) {
            this.to = to;
            this.subject = subject;
            this.body = body;
        }

        @Override
        public void run() {
            logger.debug("send email task started...");
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(address, password);
                        }
                    });
            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(address));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(owner));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);

                logger.debug("email sended successfully!");
            } catch (MessagingException e) {
                logger.warn("error sending email");
            }
        }
    }

    public void send(String to, String subject, String body) {
        threadPoolTaskExecutor.execute(new SendEmailTask(to, subject, body));
    }

    public void sendToOwner(String subject, String body) {
        send(owner, subject, body);
    }

}
