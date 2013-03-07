package com.naphaso.alice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 1:44 PM
 */
public class NewMessageListenerImpl implements NewMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(NewMessageListenerImpl.class);

    @Value("#{appProperties['email.owner']}")
    private String owner;
    @Autowired
    private EmailSender emailSender;


    @Override
    public void onNewMessage(Message message) {
        try {
            logger.debug("new email message processing...");
            String subject = message.getSubject();
            String from = message.getFrom()[0].toString();

            if(from.indexOf(owner) == -1) {
                logger.warn("message from unknown: {}", from);
                return;
            }

            String data = null;
            Object content = message.getContent();
            if(content instanceof String) {
                data = (String) content;
            } else if(content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for(int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);
                    if(part.getContentType().toLowerCase().indexOf("text/plain") != -1 && part.getContent() instanceof String)
                        data = (String) part.getContent();
                }
            }

            if(data == null) {
                emailSender.sendToOwner("unknown content", "я не знаю как читать это сообщение ;'(");
                return;
            }

            emailSender.sendToOwner("ответ на сообщение", data + "\n\nololo:DDDDDDDDDDDDD");

        } catch (MessagingException e) {
            logger.warn("exception on message processing", e);
        } catch (IOException e) {
            logger.warn("exception on message processing", e);
        }

    }
}
