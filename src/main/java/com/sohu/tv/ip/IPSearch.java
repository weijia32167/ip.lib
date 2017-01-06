package com.sohu.tv.ip;
import com.quiet.ip.IPUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright tv.sohu.com
 * Author : jiawei
 * Date   : 2017/1/5
 * Desc   :
 */
public final class IPSearch {
    private static final Logger logger = LogManager.getLogger("IPSearch");
    public static final IPSearch IPSEARCH = new IPSearch();
    private static Pattern IP_RECORD_PATTREN = Pattern.compile("([^\\s]*)\\s{1,}([^\\s]*)\\s{1,}([^\\s]*)\\s{1,}([^\\s]*)");
    private static final IPRecord DEFAULT_IP_RECORD = new IPRecord("255.255.255.255","255.255.255.255",34,10);

    private String file;

    private IPRangeMap<IPRange,IPRecord> ipRangeMap;

    private IPSearch() {

    }

    public static final IPSearch get(String filePath) {
        IPSEARCH.file = filePath;
        return IPSEARCH;
    }

    public IPRecord query(String ip){
        if(ipRangeMap ==null){
            return null;
        }else{
            IPRecord IPRecord = ipRangeMap.get(IPUtil.convert(ip));
         /*   if(IPRecord == null){
                IPRecord = DEFAULT_IP_RECORD;
            }*/
            return IPRecord;
        }
    }


    public synchronized void reload(){
        IPRangeMap<IPRange,IPRecord> ipRangeMap = new IPRangeMap<>();
        int recordsCount = 0;
        int count = 0;
        try {
            List<String> records = FileUtils.readLines(new File(file),"utf-8");
            recordsCount = records.size();
            Matcher matcher = null;
            String record;
            for(int i = 0 ; i <recordsCount ;i++){
                record = records.get(i);
                matcher = IP_RECORD_PATTREN.matcher(record);
                if(matcher.matches()){
                    String startIP = matcher.group(1);
                    String endIP = matcher.group(2);
                    String areaId = matcher.group(3);
                    String netTypeId = matcher.group(4);
                    if(IPUtil.isIPv4(startIP) && IPUtil.isIPv4(endIP)){
                        long start = IPUtil.convert(startIP);
                        long end = IPUtil.convert(endIP);
                        try{
                            ipRangeMap.put(new IPRange(startIP,endIP),new IPRecord(startIP,endIP,Integer.parseInt(areaId),Integer.parseInt(netTypeId)));
                            logger.debug("Match IPRecord["+(i+1)+"]:"+records.get(i));
                            count++;
                        }catch (NumberFormatException e){
                            logger.error("Does not match IPRecord["+(i+1)+"]:"+records.get(i));
                            continue;
                        }
                    }else{
                        logger.error("Does not match IPRecord["+(i+1)+"]:"+records.get(i));
                        continue;
                    }
                }else{
                    logger.error("Does not match IPRecord["+(i+1)+"]:"+records.get(i));
                    continue;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            this.ipRangeMap = ipRangeMap;
            logger.debug("Load IP File("+file+") records["+recordsCount+":"+count+"]");
        }
    }


}
