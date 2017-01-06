package com.sohu.tv.ip;

import com.quiet.ip.IPUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Copyright tv.sohu.com
 * Author : jiawei
 * Date   : 2017/1/5
 * Desc   :
 */
public class IPSearchTest {
    private static final Logger logger = LogManager.getLogger("IPSearchTest");
    IPSearch ipSearch;
    @Before
    public void reload(){
        File file = new File(IPSearchTest.class.getClassLoader().getResource("ip.txt").getFile());
        ipSearch = IPSearch.get(file.getAbsolutePath());
        ipSearch.reload();
    }

    @Test
    public void reLoadTest(){
        StopWatch watch = new StopWatch();
        int count = 0;
        for(int i = 0 ; i < 3000; i++){
           String ip = IPUtil.randomPublicNetworkIPv4();
           if(i == 0 ){
               watch.start();
           }else{
               watch.resume();
           }
           IPRecord IPRecord = ipSearch.query(ip);
           watch.suspend();
           if(IPRecord != null){
               if( IPUtil.convert(ip) >= IPRecord.getStart() && IPUtil.convert(ip) <= IPRecord.getEnd()){
                   logger.info(ip);
                   logger.info(IPRecord +"");
               }else{
                   logger.error(ip);
                   logger.error(IPRecord +"");
                   throw new RuntimeException();
               }
           }else{
               count++;
           }
        }
        watch.stop();
        logger.info(watch.getTime());
        logger.info(count);
    }
    @Test
    public void ipTest(){
        String ip= "70.211.13.86";
        IPRecord IPRecord = ipSearch.query(ip);
        logger.info(IPRecord);
    }
    @Test
    public void ipv4Test(){
        IPUtil.checkIPv4("101.101.22.21");
        logger.info(IPUtil.isIPv4("1"));
    }

}
