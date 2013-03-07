package com.naphaso.alice;

import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 6:01 PM
 */
public class XMPPMessageListenerImpl implements XMPPMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(XMPPMessageListenerImpl.class);

    @Autowired
    XMPPClient xmpp;

    private static final Pattern msgPattern = Pattern.compile("^(\\w+)\\s*(.*)$");

    private List<Command> commands = new ArrayList<>();

    public void registerCommand(Command command) {
        logger.debug("register command !!!!!!!!!!!");
        commands.add(command);
    }

    public void unregisterCommand(Command command) {
        logger.debug("unregister command !!!!!!!!!");
        commands.remove(command);
    }

    @Override
    public void onMessage(Message message) {
        logger.debug("message: {}", message.getBody());
        //msgPattern.matcher(msgPattern);

        for(Command command : commands) {
            if(command.process(message))
                break;
        }

        /*Message reply = new Message(message.getFrom());
        reply.setBody("reply: " + message.getBody());
        xmpp.sendPacket(reply);*/
    }
}
