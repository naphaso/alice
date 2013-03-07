package com.naphaso.alice;

import com.sun.mail.imap.IMAPFolder;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.search.FlagTerm;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");


        /*
        System.out.println("Hello World!");
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "alice@naphaso.com", "iehais7thaiZ");
            final IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
            folder.open(IMAPFolder.READ_WRITE);
            folder.addMessageCountListener(new MessageCountListener() {
                @Override
                public void messagesAdded(MessageCountEvent messageCountEvent) {
                    FlagTerm flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

                    try {
                        Message[] messages = folder.search(flagTerm);

                        for(Message message : messages) {
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

                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
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
                store.close();
            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        }*/
    }
}
