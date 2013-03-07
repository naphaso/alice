import com.naphaso.alice.XMPPClient
import org.jivesoftware.smack.packet.Message
import org.springframework.context.ApplicationContext

class ScriptBase {
    def hello(arg) {
        return "Hello ${arg}!"
    }
}

ApplicationContext context = (ApplicationContext)_context;
XMPPClient xmpp = (XMPPClient)context.getBean("xmppClient")
String owner = xmpp.getOwner()

def send(msg) {
    Message message = new Message(owner)
    message.setBody(msg)
    xmpp.sendPacket(message)
}
