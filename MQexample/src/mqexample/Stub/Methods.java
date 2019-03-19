/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqexample.Stub;

import com.ibm.msg.client.wmq.WMQConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.Message;

/**
 *
 * @author SBT-Danilchuk-YAV
 */
public class Methods {

    public static String getBSResponse(Properties prop) {
        if (prop.getProperty("OperationName").equals("SomeOperationRequest")) {
            return getSomeOperationRequestResponse(prop);
        }

        return null;
    }
  
    public static String generateDate(String DateFormat) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DateFormat);

        return df.format(calendar.getTime());

    }

    public static Properties getMsgInfo(TextMessage tMsg) {
        String tMsgStr = null;
        Properties prop = new Properties();
        String corrId = null;
        try {
            tMsgStr = tMsg.getText();
        } catch (JMSException ex) {
            Logger.getLogger(Methods.class.getName()).log(Level.SEVERE, null, ex);
        }

        String rqUid = getFirstAttrValue(tMsgStr, "RequestID");
        if (rqUid == null) {
            try {
                rqUid = tMsg.getStringProperty("RequestID");
                
                System.out.println("ID: " + rqUid);
            } catch (JMSException ex) {
                Logger.getLogger(Methods.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (rqUid != null) {
            prop.setProperty("RequestID", rqUid);
        }

        return getOperationName(prop, tMsgStr);
    }

    private static Properties getOperationName(Properties prop, String tMsgStr) {
        if (tMsgStr.indexOf("SomeOperationRequest") > -1) {
            prop.setProperty("OperationName", "SomeOperationRequest");
            prop.setProperty("OperationNameResponse", "SomeOperationRequestResponse");
            return prop;
        }

        prop.setProperty("OperationName", "UnknownOperation1");
        return prop;
    }

    public static String getFirstAttrValue(String request, String nodeName) {

        String openTag = "<" + nodeName + ">";
        String closeTag = "</" + nodeName + ">";

        if (!(request.contains(openTag) && request.contains(closeTag))) {
            return null;
        }

        int t1 = request.indexOf(openTag) + openTag.length();
        int t2 = request.indexOf(closeTag, t1);

        if (t2 - t1 <= 0) {
            return null;
        }

        String attrValue = request.substring(t1, t2);
        return attrValue;
    }

    public static String getSomeOperationRequestResponse(Properties prop) {
        String rpTm = generateDate("yyyy-MM-dd'T'HH:mm:ss.SSS") + "+04:00";
        String response                          
              = "<SomeOperationResponse>"
               +    "<RequestID>"+prop.getProperty("RequestID")+"</RequestID>"
               +    "<ResponseTm>"+rpTm+"</ResponseTm>"
               +    "<Data>"
               +        "<Something>456</Something>"
               +    "</Data>"
               + "</SomeOperationResponse>";

        return response;
    }
  
}
