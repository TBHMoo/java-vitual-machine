package com.xli.study.olexec.execute;


/**
 * 字节码修改工具
 */
public class ByteUtils {


    public static int byte2Int(byte[] b, int offset, int len) {
        int res = 0;
        int end = offset + len;
        for (int i = offset; i < end; i++) {
            //补前导零
            int cur = ((int) b[i]) & 0xff;
            //当前字节对应单位 左移8 代表 * 256
            cur <<= (--len) * 8;
            res += cur;
        }
        return res;
    }

    /**
     * len 字节数
     * @param num
     * @param len
     * @return
     */
    public static byte[] int2Byte(int num,int len){
        byte[] b = new byte[len];
        for (int i=0;i<len;i++){
            b[len - 1 - i] = (byte) ((num >> 8 * i) & 0xff);
        }
        return b;
    }

    public static String byte2String(byte[] bytes,int offset,int len){
        return new String(bytes,offset,len);
    }

    public static byte[] string2Byte(String str){
        return str.getBytes();
    }

    public static byte[] replace(byte[] oldBytes, int offset, int len, byte[] replaceBytes) {
        byte[] dest = new byte[oldBytes.length + replaceBytes.length - len];
        // prefixBytes before offset
        System.arraycopy(oldBytes, 0, dest, 0, offset);
        // after offset replaceBytes
        System.arraycopy(replaceBytes, 0, dest, offset, replaceBytes.length);
        // suffixBytes after offset plus len
        System.arraycopy(oldBytes, offset + len, dest, offset + replaceBytes.length, oldBytes.length - offset - len);
        return dest;
    }
}
