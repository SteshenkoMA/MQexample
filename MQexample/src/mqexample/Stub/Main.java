/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqexample.Stub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author steshenko3-ma
 */
public class Main {
    
public static java.util.concurrent.ExecutorService executor = null;
static int threadCount = 1;

    public static void main(String[] args) {

        System.out.println("Start BS Stub...");

        String mq_host = "tv-frm-12r2-002";
        int mq_port = 1415;
        String mq_qm = "M99.ESB.GATEWAY.CLS7";
        String mq_channel = "ASFM.SVRCONN";
        String mq_queue = "MQ.TEST.IN";
        String sendQueue = "MQ.TEST.OUT";

        executor = Executors.newFixedThreadPool(threadCount);
        
		for (int i = 0; i < threadCount; i++)   {
			executor.submit(new Stub(mq_host, mq_port, mq_qm, mq_channel, mq_queue, sendQueue));
                 }            
    
    
        
    }
}
