package com.pax.api;

/**
 * usb打印机常量工具
 */
public class PrinterConstants {

    private static final String TAG = "ConstantsUtil";

    private static final byte ESC = 0x1B;
    private static final byte A = 0x41;
    private static final byte B = 0x42;
    private static final byte C = 0x43;
    private static final byte D = 0x44;
    private static final byte E = 0x45;
    private static final byte F = 0x46;
    private static final byte G = 0x47;
    private static final byte H = 0x48;
    private static final byte I = 0x49;
    private static final byte J = 0x4A;
    private static final byte K = 0x4B;
    private static final byte L = 0x4C;
    private static final byte M = 0x4D;
    private static final byte N = 0x4E;
    private static final byte O = 0x4F;
    private static final byte P = 0x50;
    private static final byte Q = 0x51;
    private static final byte R = 0x52;
    private static final byte S = 0x53;
    private static final byte T = 0x54;
    private static final byte U = 0x55;
    private static final byte V = 0x56;
    private static final byte W = 0x57;
    private static final byte X = 0x58;
    private static final byte Y = 0x59;


    /**
     * 初始化打印机
     */
    public static final byte[] INIT_PRINTER = {ESC, '@'};

    /**
     * 切刀
     */
    public static final byte[] FullCut = {0x0a, 0x0a, 0x1d, 0x56, 0x30};
    public static final byte[] HalfCut = {0x0a, 0x0a, 0x1d, 0x56, 0x31};


    /**
     * 设置打印机浓度
     *
     * @param mConcentration 打印机浓度等级:0-39
     * @return 传输的数据, 如果不输入值不在范围内, 则返回默认打印浓度
     */
    public static byte[] setPrinterConcentration(int mConcentration) {
        if (0 <= mConcentration && mConcentration <= 39) {
            byte[] a = {ESC, '#', '#', S, T, D, P};
            return EncodersUtil.getBytes(a, mConcentration, 2, EncodersUtil.ORDER_DIRECTION);
        } else
            return new byte[]{ESC, '#', '#', S, T, D, P, 0x15};
    }


    /**
     * 开钱箱
     */
    public static final byte[] OPEN_CASH_BOX = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};


}
