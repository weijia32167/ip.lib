package com.sohu.tv.ip;

import com.quiet.ip.IPUtil;

/**
 * Copyright tv.sohu.com
 * Author : jiawei
 * Date   : 2017/1/5
 * Desc   :
 */
public class IPRecord {

    private IPRange ipRange;

    private int areaId;

    private int netTypeId;

    public IPRecord(String startIP, String endIP, int areaId, int netTypeId) {
        ipRange = new IPRange(startIP,endIP);
        this.areaId = areaId;
        this.netTypeId = netTypeId;
    }


    public long getStart() {
        return ipRange.getStart();
    }
    public long getEnd() {
        return ipRange.getEnd();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("");
        sb.append(ipRange.toString()).append(" ");
        sb.append(areaId).append(" ");
        sb.append(netTypeId);
        return sb.toString();
    }



}
