package mqexample.Stub;

import java.sql.Connection;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.QueueSession;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;
import javax.jms.DeliveryMode;
import javax.jms.TextMessage;

public class MessageSender implements Runnable {
	
	MQQueueConnection conn;
	String mq_queueSend = null;
	Properties prop;
	
	Connection oraconn;	
	
	public MessageSender(MQQueueConnection conn, Properties prop, String sendQueue) {
		this.conn = conn;
		this.prop = prop;
                this.mq_queueSend = sendQueue;
		
	}
	
	public void run() {
		
		if(prop.getProperty("OperationName").equals("UnknownOperation")){
                    return;
                }
		try {	
			
			MQQueueSession session = (MQQueueSession) conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			MQQueue queueSend = (MQQueue) session.createQueue(mq_queueSend);
			MQQueueSender sender = (MQQueueSender) session.createSender(queueSend);
			TextMessage sendMsg = session.createTextMessage();                    
                                    
                        sendMsg.setText(Methods.getBSResponse(prop));
                        
                                              
                        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sender.send(sendMsg);

                      System.out.println("response: " + sendMsg.getText());
                        
			sender.close();
			session.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
			
	}
}
