package com.naphaso.alice;

import com.jivesoftware.spark.util.DummySSLSocketFactory;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/4/13
 * Time: 5:03 PM
 */
public class XMPPClient implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(XMPPClient.class);

    @Value("#{appProperties['xmpp.host']}")
    private String host;
    @Value("#{appProperties['xmpp.user']}")
    private String user;
    @Value("#{appProperties['xmpp.password']}")
    private String password;
    @Value("#{appProperties['xmpp.serviceName']}")
    private String serviceName;

    public String getOwner() {
        return owner;
    }

    @Value("#{appProperties['xmpp.owner']}")
    private String owner;

    @Autowired
    private XMPPMessageListener messageListener;

    private ConnectionConfiguration configuration;
    private Connection connection;


    @Override
    public void afterPropertiesSet() throws Exception {
        configuration = new ConnectionConfiguration(host, 5223, serviceName);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        configuration.setSocketFactory(new DummySSLSocketFactory());
        connection = new XMPPConnection(configuration);
        try {
            connection.connect();
            connection.login(user, password);

            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);

            // send a message to somebody
            Message msg = new Message(owner, Message.Type.chat);
            msg.setBody("hello");
            connection.sendPacket(msg);


            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    if(packet instanceof Message)
                        messageListener.onMessage((Message)packet);
                }
            }, null);

            connection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connectionClosed() {
                    logger.info("XMPP connection closed");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    logger.info("XMPP connection closed on error", e);
                }

                @Override
                public void reconnectingIn(int i) {
                    logger.info("XMPP reconnect", i);
                }

                @Override
                public void reconnectionSuccessful() {
                    logger.info("XMPP reconnect successfull");
                }

                @Override
                public void reconnectionFailed(Exception e) {
                    logger.info("XMPP reconnect failed");
                }
            });
        } catch (XMPPException e) {
            logger.error("XMPP connect exception", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        connection.disconnect();
    }

    public void sendPacket(Packet packet) {
        connection.sendPacket(packet);
    }
}
