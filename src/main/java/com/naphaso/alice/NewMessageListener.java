package com.naphaso.alice;

import javax.mail.Message;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 1:43 PM
 */
public interface NewMessageListener {
    public void onNewMessage(Message message);
}