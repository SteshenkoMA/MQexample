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
public class Main {

    public static void main(String[] args) throws Throwable {

        String mq_host = "host";
        int mq_port = 555;
        String mq_qm = "";
        String mq_channel = "channel";
        String mq_queue = "queue";

        Sender msgSender = new Sender();
        msgSender.initConnection(mq_host, mq_port, mq_channel, mq_qm, mq_queue);
        msgSender.sendMessage();

    }

}
