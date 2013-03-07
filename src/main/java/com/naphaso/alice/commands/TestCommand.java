package com.naphaso.alice.commands;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 1:14 AM
 */

public class TestCommand {}/*implements Command, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(TestCommand.class);

    private static final Pattern pattern = Pattern.compile("^test$");

    @Autowired
    private XMPPClient xmpp;
    @Autowired
    private XMPPMessageListener xmppMessageListener;


    @Override
    public boolean process(Message message) {
        if(pattern.matcher(message.getBody()).matches()) {
            Message reply = new Message(message.getFrom());
            reply.setBody("test!");
            xmpp.sendPacket(reply);
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        xmppMessageListener.registerCommand(this);
    }

    @Override
    public void destroy() throws Exception {
        xmppMessageListener.unregisterCommand(this);
    }
}*/
