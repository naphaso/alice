package com.naphaso.alice.commands;

import com.naphaso.alice.XMPPClient;
import com.naphaso.alice.persistence.Note;
import com.naphaso.alice.service.NoteService;
import org.jivesoftware.smack.packet.Message;
import com.naphaso.alice.annotation.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 1:36 AM
 */
@Component
public class NewCommand {
    private static final Logger logger = LoggerFactory.getLogger(NewCommand.class);

    @Autowired
    XMPPClient xmpp;

    @Autowired
    NoteService noteService;

    @Command(pattern = "^hello (.+)$")
    public void hello(Message message, Matcher matcher) {
        logger.info("hello called!");
        String name = matcher.group(1);
        Message reply = new Message(message.getFrom());
        reply.setBody("hi " + name);
        xmpp.sendPacket(reply);
    }

    @Command(pattern = "^add (.+?) (.+)$")
    public void add(Message message, Matcher matcher) {
        logger.info("add note called!");
        Note note = new Note(matcher.group(1), matcher.group(2));
        noteService.addNote(note);
        Message reply = new Message(message.getFrom());
        reply.setBody("ok!");
        xmpp.sendPacket(reply);
    }

    @Command(pattern = "^list$")
    public void list(Message message, Matcher matcher) {
        logger.info("list notes called!");

        List<Note> notes = noteService.listNote();

        Message reply = new Message(message.getFrom());
        reply.setBody("begin");
        xmpp.sendPacket(reply);
        for(Note note : notes) {
            reply = new Message(message.getFrom());
            reply.setBody("title:"+note.getTitle()+", content:"+note.getContent());
            xmpp.sendPacket(reply);
        }
        reply = new Message(message.getFrom());
        reply.setBody("end");
        xmpp.sendPacket(reply);
    }

    @Command(pattern = "^index$")
    public void index(Message message, Matcher matcher) {
        logger.info("reindex called!");
        noteService.index();
        Message reply = new Message(message.getFrom());
        reply.setBody("build index done!");
        xmpp.sendPacket(reply);
    }

    @Command(pattern = "^search (.+)$")
    public void search(Message message, Matcher matcher) {
        String query = matcher.group(1);
        List<Note> notes = noteService.search(query);
        if(notes.isEmpty()) {
            Message reply = new Message(message.getFrom());
            reply.setBody("not found");
            xmpp.sendPacket(reply);
        } else {
            for(Note note : notes) {
                Message reply = new Message(message.getFrom());
                reply.setBody(note.getTitle() + ": " + note.getContent());
                xmpp.sendPacket(reply);
            }
        }
    }
}
