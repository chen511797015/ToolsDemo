package cn.pax.toolsdemo.tool;

import cn.pax.toolsdemo.util.EncodersUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by chendd on 2017/1/5.
 * 打印机指令常量
 */

public class PrinterConstants {

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


    public static final byte OPEN_CUT = 0x31;//开启切刀
    public static final byte CLOSE_CUT = 0x30;//关闭切刀


    /**
     * 初始化打印机
     */
    public static final byte[] InitPrint = {0x1b, 0x40};


    /**
     * 开钱箱
     */
    public static final byte[] OpenCashBox = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};

    /**
     * 全切--0x30      半切--0x31
     */
    public static final byte[] FullCut = {0x1b, 0x23, 0x23, 0x43, 0x54, 0x47, 0x48, 0x30};
    public static final byte[] HalfCut = {0x1b, 0x23, 0x23, 0x43, 0x54, 0x47, 0x48, 0x31};

    /**
     * 2.1  打印自检信息
     */
    public static final byte[] PRINT_SELF_CHECKING_INFORMATION = {ESC, '#', '#', 0x53, 0x45, 0x4C, 0x46};

    /**
     * 2.7设置打印机浓度(测试ok)
     *
     * @param mConcentration :打印机浓度值 0<=mConcentration<=39
     * @return
     */
    public static byte[] setPrinterConcentration(int mConcentration) throws EcrPosprintException {
        if (0 <= mConcentration && mConcentration <= 39) {
            byte[] a = {ESC, '#', '#', S, T, D, P};
            return EncodersUtil.getBytes(a, mConcentration, 2, EncodersUtil.ORDER_DIRECTION);
        } else {
            throw new EcrPosprintException("请输入正确的取值范围");
        }
    }

    /**
     * 2.52.打印浓度等级
     * Concentration level
     */
    public static final byte[] PRINT_CONCENTRATION_LEVEL = {ESC, '#', K};



    /**
     * 打印机信息
     */
    public static final byte[] PrinterAbout = {0x1b, 0x23, 0x23, 0x53, 0x45, 0x4c, 0x46};


    /**
     * 左对齐--0x00  中间对齐--0x01  右对齐--0x02
     */
    public static final byte[] ESC_ALIGN_LEFT = {ESC, 0x61, 0x00};
    public static final byte[] ESC_ALIGN_CENTER = {ESC, 0x61, 0x01};
    public static final byte[] ESC_ALIGN_RIGHT = {ESC, 0x61, 0x02};

    /**
     * 打印并向前走纸N行   0<=N<=255
     *
     * @param
     */
    public static byte[] paperSkip() {
        byte[] result = new byte[3];

        result[0] = 0x1b;
        result[1] = 0x64;
        result[2] = 0x64;
        return result;

    }
}
