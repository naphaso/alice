package com.naphaso.alice;

import org.jivesoftware.smack.packet.Message;

import java.util.regex.Matcher;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 1:37 AM
 */
public interface Command {
    public boolean process(Message message);
}
