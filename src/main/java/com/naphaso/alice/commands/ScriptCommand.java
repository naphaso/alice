package com.naphaso.alice.commands;

import com.naphaso.alice.XMPPClient;
import com.naphaso.alice.annotation.Command;
import com.naphaso.alice.scripting.ScriptService;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/6/13
 * Time: 9:07 PM
 */
@Component
public class ScriptCommand {
    private static final Logger logger = LoggerFactory.getLogger(ScriptCommand.class);



    @Autowired
    private ScriptService scriptService;

    @Autowired
    private XMPPClient xmpp;

    @Command(pattern = "^!(.+)$")
    public void eval(Message message, Matcher matcher) {
        String code = matcher.group(1);
        logger.info("eval code: {}", code);

        String result = "default result";
        try {
            result = scriptService.eval(code);
        //} catch (MultipleCompilationErrorsException e) {
        //    result = e.getMessage();
        } catch(Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            result = writer.toString();
        }

        Message reply = new Message(message.getFrom());
        reply.setBody(result);
        xmpp.sendPacket(reply);
    }
}


