package cn.pax.toolsdemo.util;

/**
 * Created by chendd on 2017/1/16.
 * 转换工具
 */

public class EncodersUtil {

    public static final int ORDER_DIRECTION = 1;//顺序
    public static final int REVERSE_DIRECTION = 2;//倒序

    /**
     * 获取ascii码格式字节数组
     *
     * @param arg0       要拼接的字节数组
     * @param values     要拼接的十进制数
     * @param maxLength  拼接的十进制数最大长度  例如:0x23  maxLength=2即可
     * @param mDirection 拼接的方向
     * @return 得到的byte数组
     */
    public static byte[] getBytes(byte[] arg0, int values, int maxLength, int mDirection) {
        return arrayByteArray(arg0, intToBytes(values, maxLength), mDirection);
    }


    /**
     * 获取ascii码格式字节数组
     *
     * @param values
     * @param maxLength
     * @param arg0
     * @param mDirection
     * @return
     */
    public static byte[] getBytes(int values, int maxLength, byte[] arg0, int mDirection) {
        return arrayByteArray(arg0, intToBytes(values, (maxLength * 2)), mDirection);
    }


    /**
     * 获取ascii码格式字节数组
     *
     * @param arg0       要拼接的字节数组
     * @param ars1       要拼接的字节数组
     * @param mDirection 拼接的方向
     * @return 得到的byte数组
     */
    public static byte[] getBytes(byte[] arg0, byte[] ars1, int mDirection) {
        return arrayByteArray(arg0, ars1, mDirection);
    }


    /**
     * 先将十进制转换成十六进制,然后再将十六进制转换成byte数组
     *
     * @param values    :要转换的十进制数
     * @param maxLength :需要转换成的十六进制的长度
     * @return
     */
    private static byte[] intToBytes(int values, int maxLength) {
        return hexString2Bytes(algorismToHEXString(values, maxLength));
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF,
     * 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    private static byte[] hexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytesArray(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"–> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    private static byte uniteBytesArray(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }


    /**
     * 将十进制转换成指定长度的十六进制字符串
     *
     * @param values    int 十进制数字
     * @param maxLength int 转换后的十六进制字符串长度
     * @return String 转换后的十六进制字符串
     */
    private static String algorismToHEXString(int values, int maxLength) {
        String mResult = "";
        mResult = Integer.toHexString(values);
        if (mResult.length() % 2 == 1) {
            mResult = "0" + mResult;
        }
        return patchHexString(mResult.toUpperCase(), maxLength);
    }

    /**
     * HEX字符串前补0，主要用于长度位数不足。
     *
     * @param str       String 需要补充长度的十六进制字符串
     * @param maxLength int 补充后十六进制字符串的长度
     * @return 补充结果
     */
    private static String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    /**
     * 将两个字节数组拼接起来,其中b是顺序连接
     *
     * @param a
     * @param b
     * @return
     */
    private static byte[] arrayByteArray(byte[] a, byte[] b, int mDirection) {

        int len = a.length + b.length;
        byte[] c = new byte[len];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i];
        }

        if (mDirection == ORDER_DIRECTION) {
            for (int i = 0; i < b.length; i++) {
                int index = a.length + i;
                c[index] = b[i];
            }
        } else {
            for (int i = 0; i < b.length; i++) {
                int j = (a.length) + i;
                c[j] = b[(b.length) - i - 1];
            }
        }
        return c;
    }


}
