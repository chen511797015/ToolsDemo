package cn.pax.toolsdemo.tool;

/**
 * Created by xiexq on 2017/1/13.
 */

public class EcrPosprintException extends Exception {

    private static final long serialVersionUID = 1L;
    public static final byte PRINTER_BUSY = 1;
    public static final byte OUT_OF_MAX_VALUE = 2;
    public static final byte DATA_PACKET_ERROR = 3;
    public static final byte PRINTER_PROBLEMS = 4;
    public static final byte PRINTER_OVER_HEATING = 8;
    public static final byte PRINT_UNFINISHED = -16;
    public static final byte LACK_OF_FONT = -4;
    public static final byte PACKAGE_TOO_LONG = -2;
    public static final byte ERROR_UNSUPPORTED_ENCODING = 97;
    public static final byte ERR_NULL_POINT = 98;
    public static final byte CONN_ERROR = 99;
    public byte exceptionCode = 99;

    public EcrPosprintException(byte exCode)
    {
        super(searchMessage(exCode));
        this.exceptionCode = exCode;
    }

    public EcrPosprintException(String message)
    {
        super(message);
    }

    public EcrPosprintException(byte exCode, String message)
    {
        super(message);
        this.exceptionCode = exCode;
    }

    private static String searchMessage(byte exCode)
    {
        String message = "";
        switch (exCode) {
            case 98:
                message = "parameter cannot be null";
                break;
            case 99:
                message = "RPC I/O error";
                break;
            case 1:
                message = "Printer busy";
                break;
            case 2:
                message = "Out of paper";
                break;
            case 3:
                message = "The format of print data packet error";
                break;
            case 4:
                message = "Printer is already open OR Printer problems ";
                break;
            case 8:
                message = "Printer over heating";
                break;
            case -16:
                message = "Print unfinished";
                break;
            case -4:
                message = "Lack of font";
                break;
            case -2:
                message = "Data package is too long.";
                break;
            case 97:
                message = "Unsupported encoding";
        }

        return message;
    }
}
