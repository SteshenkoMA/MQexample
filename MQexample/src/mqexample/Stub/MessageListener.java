/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqexample.Stub;

/**
 *
 * @author steshenko3-ma
 */
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import com.ibm.mq.jms.MQQueueConnection;
import java.util.concurrent.TimeUnit;

public class MessageListener implements javax.jms.MessageListener {
    
    boolean a = true;
	
	MQQueueConnection conn;
	
	ScheduledExecutorService executor;
        String sendQueue=null;

	public MessageListener (MQQueueConnection conn, String sendQueue) {
		this.conn = conn;
		executor = new ScheduledThreadPoolExecutor(50);
                this.sendQueue = sendQueue;
	}
	
	public void onMessage(Message msg) {
           
            try {
                            
                        TextMessage tMsg = (TextMessage) msg;
                        
                        String tMsgStr= tMsg.getText();
                        
                        System.out.println("recieved: "+ tMsgStr);
                        
               
		Properties prop = null;
	//	String tMsgStr=null;
		try {
			tMsgStr = tMsg.getText();
			prop = Methods.getMsgInfo(tMsg);
                        
                   //     System.out.println(prop.toString());
                        

            	
		} catch (JMSException e) {
			e.printStackTrace();
		} 
                                     
                               
                Runnable runnableSend = new MessageSender(conn, prop, sendQueue);
		Thread t = new Thread (runnableSend);
		t.run();
                
            }
                catch (Exception e) {
			e.printStackTrace();
          
                }
            }
        

        }
