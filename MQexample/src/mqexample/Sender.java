/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqexample;

/**
 *
 * @author steshenko3-ma
 */
import java.sql.Timestamp;
import java.util.Random;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.jms.JMSException;
import javax.jms.Session;

import com.ibm.jms.JMSMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;

public class Sender {

    MQQueueConnectionFactory cf = null;
    MQQueueConnection connection = null;
    MQQueueSession session = null;
    MQQueue queue = null;
    MQQueueSender sender = null;

    Random rnd = new Random();

    public void initConnection(String host, int port, String channel, String manager, String queueName) throws Throwable {

        try {

            cf = new MQQueueConnectionFactory();

            // Config
            cf.setHostName(host);
            cf.setPort(port);
            cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
            cf.setQueueManager(manager);
            cf.setChannel(channel);

            connection = (MQQueueConnection) cf.createQueueConnection();
            session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = (MQQueue) session.createQueue("queue:///" + queueName);

            sender = (MQQueueSender) session.createSender(queue);

            connection.start();

            System.out.println("connected");
        } catch (JMSException ex) {

        } catch (Exception ex) {

        }

    }

    public void sendMessage() throws Throwable {

        try {

            String rqUID = generateRqUid(32);                  
            JMSTextMessage message = (JMSTextMessage) session.createTextMessage(buildMsg(rqUID));
            sender.setDeliveryMode(1);
            sender.send(message);

        } catch (JMSException ex) {

        } catch (Exception ex) {

        }

    }

    /**
     * Метод возвращает rqUID в формате TimeStamp + opName +Properties + AC ID +
     * AC Name+ salt 0-13	13-15 15-17	17-19	19-N N	* /
     */
    public String generateRqUid(int length) {

        String rqUID = "";
        //build timestamp
        Timestamp ts = new Timestamp((new Date()).getTime());

        //build rqUID w/o salt	     	
        rqUID = ts.getTime() + "Client";

        //build salt
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (length - rqUID.length()); i++) {
            sb.append(rnd.nextInt(10));
        }

        //add salt
        rqUID += sb.toString();

        return rqUID;
    }

    /**
     * Метод служит для генерации date в заданном формате /
     */
    public String generateDate(String DateFormat) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DateFormat);

        return df.format(calendar.getTime());

    }

    public String buildMsg(String rqUID) {

        String msg = "";
        String rqTm = generateDate("yyyy-MM-dd'T'HH:mm:ss'.000+03:00'");
        String rqDate = generateDate("yyyy-MM-dd");
        String rqDate1 = generateDate("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        String agent = "{AC}"; //SBOL – СБОЛ Россия; RSTYLE – СБОЛ Москва;SVFE – SmartVista;	WAY4 – WAY4;ESB – КСШ;
        String property = "{propertiesValue}"; // 00- WAY4, 01 -SVFE; 

        // lr.message(rqUID);  - для debug       	
        //формируем msg
        msg
                = "<SomeOperationRequest>"
                +    "<RequestID>"+rqUID+"</RequestID>>"
                +    "<RequestTm>"+rqTm+"</RequestTm>>"
                +    "<Data>"
                +       "<Something>123</Something>"
                +    "</Data>"
                + "</SomeOperationRequest>";
        return msg;
    }

}
