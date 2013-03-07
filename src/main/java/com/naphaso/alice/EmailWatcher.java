package com.naphaso.alice;

import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.search.FlagTerm;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 1:13 PM
 */
public class EmailWatcher implements InitializingBean, DisposableBean, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(EmailWatcher.class);

    @Value("#{appProperties['email.address']}")
    private String address;
    @Value("#{appProperties['email.password']}")
    private String password;
    @Value("#{appProperties['email.host']}")
    private String host;

    private Thread watchThread;

    @Autowired
    private NewMessageListener newMessageListener;


    @Override
    public void afterPropertiesSet() throws Exception {
        watchThread = new Thread(this);
        watchThread.setName("emailWatch");
        watchThread.start();
    }

    @Override
    public void destroy() throws Exception {
        watchThread.interrupt();
    }


    @Override
    public void run() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(host, address, password);
            final IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
            folder.open(IMAPFolder.READ_WRITE);
            folder.addMessageCountListener(new MessageCountListener() {
                @Override
                public void messagesAdded(MessageCountEvent messageCountEvent) {
                    FlagTerm flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

                    try {
                        Message[] messages = folder.search(flagTerm);


                        for(Message message : messages) {
                            newMessageListener.onNewMessage(message);
                            /*
                            String subject = message.getSubject();
                            System.out.println("new message subject: " + subject);
                            message.setFlag(Flags.Flag.SEEN, true);
                            Object content = null;
                            try {
                                content = message.getContent();
                                String data = null;
                                if(content instanceof String) {
                                    data = (String) content;
                                    System.out.println("body = '"+data+"'");
                                } else if(content instanceof Multipart) {
                                    Multipart multipart = (Multipart) content;

                                    System.out.println("multipart message");
                                    for(int i = 0; i < multipart.getCount(); i++) {
                                        System.out.println("parse bodypart " + i);
                                        BodyPart bodyPart = multipart.getBodyPart(i);
                                        System.out.println("bodypart content-type: " + bodyPart.getContentType());

                                        Object part = bodyPart.getContent();
                                        if(part instanceof String) {
                                            String partstring = (String)part;
                                            System.out.println("part string = " + partstring);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            */
                        }
                    } catch (MessagingException e) {
                        logger.warn("messaging exception", e);
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent messageCountEvent) {

                }
            });

            try {
                while(true) {
                    folder.idle();
                }
            } finally {
                logger.info("email watcher stopped");
                store.close();
            }
        } catch (NoSuchProviderException e) {
            logger.error("not such email provider", e);
        } catch (MessagingException e) {
            logger.warn("messaging exception", e);
        }
    }
}
