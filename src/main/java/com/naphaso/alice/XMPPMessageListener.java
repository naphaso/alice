package com.naphaso.alice;

import org.jivesoftware.smack.packet.Message;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 6:01 PM
 */
public interface XMPPMessageListener {
    public void onMessage(Message message);
    public void registerCommand(Command command);
    public void unregisterCommand(Command command);
}
