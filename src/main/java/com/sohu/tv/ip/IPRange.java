package com.sohu.tv.ip;

import com.quiet.ip.IPUtil;

import java.util.Comparator;

/**
 * Copyright tv.sohu.com
 * Author : jiawei
 * Date   : 2017/1/6
 * Desc   :
 */
    public final class IPRange implements Comparable<IPRange>{

    private String startIP;
    private String endIP;
    private long start;
    private long end;



    public IPRange(String startIP, String endIP) {
        IPUtil.checkIPv4(startIP);
        IPUtil.checkIPv4(endIP);
        start = IPUtil.convert(startIP);
        end = IPUtil.convert(endIP);
        if(start > end){
            start = - 1;
            end = -1;
            throw new IllegalArgumentException("IP Range start < end("+startIP+":"+endIP+")!");
        }else{
            this.startIP = startIP;
            this.endIP = endIP;
        }
    }

    public long compare(long clientIP){
        if(clientIP < start){
            return clientIP - start;
        }else if(clientIP > end){
            return clientIP -end;
        }else{
            return 0;
        }
    }

    public boolean contain(long clientIP){
        if(clientIP>=start && clientIP <=end){
            return true;
        }else{
            return false;
        }
    }

    public long getEnd() {
        return end;
    }

    public long getStart() {
        return start;
    }

    @Override
    public int compareTo(IPRange o) {
        long result = this.end - o.start;
        if(result > 0){
            return 1;
        }else if(result==0){
            return 0;
        }else{
            return -1;
        }
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(startIP).append("["+start+"] ~ ");
        sb.append(endIP).append("["+end+"]");
        return sb.toString();
    }
}
