/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mqexample.Stub;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSession;
import javax.jms.JMSException;
import javax.jms.QueueSession;

/**
 *
 * @author steshenko3-ma
 */
public class Stub implements Runnable{

 public static java.util.concurrent.ExecutorService executor = null;
	
	static final long starttime = System.currentTimeMillis();
	
	MQQueueConnectionFactory factory;
	MQQueueConnection conn;

        String mq_queue = null;

        boolean stubIsWorking = false;
      
	MQQueueSession session;
	MQQueue queue;
	MQQueueReceiver receiver; 
        String sendQueue=null;
    
        public Stub (String mq_host, int mq_port, String mq_qm, String mq_channel, String mq_queue, String sendQueue){
                              
            	try {
                        this.mq_queue=mq_queue;
                        this.sendQueue=sendQueue;
			factory = new MQQueueConnectionFactory();
			factory.setHostName(mq_host);
			factory.setPort(mq_port);
			factory.setQueueManager(mq_qm);
			factory.setChannel(mq_channel);
                        factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
						
			conn = (MQQueueConnection) factory.createQueueConnection();
                        System.out.println("Start BS Stub connection...");
			conn.start();

	    } catch (JMSException e) {
		    e.printStackTrace();
		}
                
              
            
        }    
        		
    @Override
	public void run() {
          
		try {
			session = (MQQueueSession) conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			queue = (MQQueue) session.createQueue(mq_queue);
			receiver = (MQQueueReceiver) session.createReceiver(queue);
			receiver.setMessageListener(new MessageListener(conn, this.sendQueue));
                        System.out.println("111");
                        
		} catch (JMSException e) {
			e.printStackTrace();
		}
              
	}
        
}
