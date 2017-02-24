package com.pax.printer;

import android.widget.Toast;


import rego.printlib.export.regoPrinter;


/**
 * @author qiwenming
 * @date 2016/2/29 0029 下午 4:20
 * @ClassName: PrintTerUtils
 * @PackageName: com.qwm.qwmprinterdemo.utils
 * @Description:  打印类
 */
public class PrintTerUtils {
    public regoPrinter printer;
    private static PrintTerUtils printTerUtils;
    public int myState = 0;
    private String printName="RG-MTP58B";
    private MainActivity mainActivity;
    public boolean mBconnect = false;
    private final int wordSize = 20;
    /**
     * 打印的行数
     */
    private int pintCol = 0;
    /**
     * 小票的高度
     */
    private int pintHeight = 540;

    private PrintTerUtils(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        connectDevices(mainActivity);
    }

    public static PrintTerUtils getPrintTerUtils(MainActivity mainActivity){
        if (printTerUtils==null){
            printTerUtils = new PrintTerUtils(mainActivity);
        }
        return printTerUtils;
    }

    /**
     * 连接设备
    */
    private void connectDevices(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        if(printer==null){
            printer = new regoPrinter(mainActivity);//创建设备
        }
        //1.判读设备是否连着
        if(!mBconnect){//没有连接，那么我们连接 （连接设备）
            myState = printer.CON_ConnectDevices(printName, "usb", 200);//练级
            if (myState > 0) {
                Toast.makeText(mainActivity, "打印机连接成功",
                        Toast.LENGTH_SHORT).show();
                mBconnect = true;
            }else{
                mBconnect = false;
                return;
            }
        }
    }

    /**
     * 打印购物网当
     */
    public void toPrintOrder() {
        //TODO 设置我们的 订单
        printer.CON_PageStart(myState,false,0,0);
        printer.ASCII_CtrlAlignType(myState, preDefiniation.AlignType.AT_CENTER.getValue());
        printer.ASCII_PrintString(myState, 0, 1, 0, 0, 0, "欢迎光临  ", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "店铺名称  ", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_CtrlAlignType(myState, preDefiniation.AlignType.AT_LEFT.getValue());
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "收银员：收银员名称", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "时间：2015-01-01", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "----------------------------", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 1, 0, 0, "商品名称        单价      数量      金额", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "花生           2.00      1      2.00", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "----------------------------", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "合计:￥100元", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "优惠金额:￥90元", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "电话：01062985019", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "地址：汉武大帝大厦", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 0, 0, 0, 0, 0, "谢谢惠顾，欢迎下次光临！", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_CtrlReset(myState);
        printer.ASCII_CtrlCutPaper(myState, 66, 0);//切纸
        printer.CON_PageEnd(myState,0);
    }

    /**
     * 回收单子
     */
    public void toPrint2() {
        printer.CON_PageStart(myState, true, 540, 540);
        printer.DRAW_SetFillMode(false);
        printer.DRAW_SetLineWidth(4);
        //矩形
        printer.DRAW_PrintRectangle(myState, 20, 10, 540, 550);
        printer.ASCII_PrintString(myState, 153, 20, 0, 0, 0, "手机回收凭证", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
//        printer.DRAW_PrintText(myState,153,32,"爱易收手机回收凭证",30);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 20, "服务门店：小明大厦店", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.DRAW_PrintLine(myState, 38, 70, 500, 70);//线条
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "订单号：123456789", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "时间：2016-01-26 14:50", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "姓名：杞文明              联系电话：13000000000", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.DRAW_PrintLine(myState, 38, 140, 500, 140);//线条
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "机型：iPhone 6s", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "IMEI：123456789987654123", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "：", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "大陆国行/三网通/过保/xxxxx/小米/嘻嘻嘻一/附件是弟弟福建省的/十分激动手机覅及地上/啦啦啦", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.DRAW_PrintLine(myState, 38, 290, 500, 290);//线条
        printer.ASCII_PrintString(myState, 153, 0, 0, 0, 0, "回收价：1000元RMB", "gb2312");
        printer.ASCII_CtrlPrintCRLF(myState, 1);
//        printer.ASCII_CtrlPrintCRLF(myState, 1);
        printer.ASCII_CtrlCutPaper(myState, 555, 0);//切纸
        printer.CON_PageEnd(myState,0);
    }

    /**
     * 回收单子2
     */
    public void toPrint3(){
        printer.CON_PageStart(myState, true, 540, 540);
        printer.DRAW_SetFillMode(false);
        printer.DRAW_SetLineWidth(4);
        //矩形
        printer.DRAW_PrintRectangle(myState, 18, 10, 540, 540);
        printer.DRAW_PrintText(myState, 108, 32, "爱易收手机回收凭证", 40);
        printer.DRAW_PrintText(myState, 108, 82, "服务门店：西海明珠大厦店", 20);
        printer.DRAW_SetLineWidth(2);
        printer.DRAW_PrintLine(myState, 38, 107, 500, 107);//线条
        printer.DRAW_PrintText(myState, 38, 127, "订单号：123456789", 25);
        printer.DRAW_PrintText(myState, 38, 167, "时间：2016-01-26 14:50", 25);
        printer.DRAW_PrintText(myState, 38, 207, "姓名：杞文明     联系电话：13000000", 25);
        printer.DRAW_PrintLine(myState, 38, 237, 500, 237);//线条
        printer.DRAW_PrintText(myState, 38, 257, "机型：iPhone 6s", 25);
        printer.DRAW_PrintText(myState, 38, 297, "IMEI：123456789987654123", 25);
        printer.DRAW_PrintText(myState, 38, 337, "设备情况：", 25);
        //下面的高度需要更具字符的个数计算
        printer.DRAW_PrintText(myState, 38, 377, "大陆国行/三网通/过保/xxxxx/小米/嘻嘻嘻一/附件是弟弟福建省的/十分激动手机覅及地上/啦啦啦", 25);
        printer.DRAW_PrintLine(myState, 38, 462, 500, 462);//线条
        printer.DRAW_PrintText(myState, 38, 482, "回收价：1000元RMB", 25);
        printer.CON_PageEnd(myState, 0);
        //切纸功能
        printer.CON_PageStart(myState, false, 0, 0);
        printer.ASCII_CtrlCutPaper(myState, 30, 0);
        printer.CON_PageEnd(myState,0);
    }


    //    /////////////////////////////////////////////////////////Json对象的处理//////////////////////////////////////////////
//   /** 打印账单
//    * @param orderJson
//    */
//    public void printOrder(MainActivity mainActivity,JsonElement orderJson){
//        this.mainActivity = mainActivity;
//        if(printer==null){
//            printer = new regoPrinter(mainActivity);//创建设备
//        }
//        //1.判读设备是否连着
//        if(!mBconnect){//没有连接，那么我们连接 （连接设备）
//            myState = printer.CON_ConnectDevices(printName, "usb", 200);//练级
//            if (myState > 0) {
//                Toast.makeText(mainActivity, "打印机连接成功",
//                        Toast.LENGTH_SHORT).show();
//                mBconnect = true;
//            }else{
//                mBconnect = false;
//                return;
//            }
//        }
//    }
//
//    private OrderBean getOrderBean(JsonElement orderJson){
//        Gson gson = new Gson();
//        try{
//            OrderBean orderBean = gson.fromJson(orderJson,OrderBean.class);
//            //设备情况的行数计算
//            //1.拼接所有的字符串
//            StringBuilder sb = new StringBuilder();
//            for (int i=0;i<orderBean.qids.size();i++){
//                if(i>0){
//                    sb.append("/");
//                }
//                sb.append(orderBean.qids.get(i));
//            }
//            String qidStr = sb.toString();
//            //2.根据字符串的长度截取字符
//            int col = qidStr.length()/wordSize+1;
//            List<String> qidList = new ArrayList<String>();
//            if(col<=1){
//                qidList.add(qidStr);
//            }else{
//                for (int i=1;i<=col;i++){
//                    if(i!=col){//不是最后一行
//                        qidList.add(qidStr.substring((i - 1) * wordSize, i * wordSize));
//                    }else{//最后一行，直接加入
//                        qidList.add(qidStr.substring((col-1)*wordSize));
//                    }
//                }
//            }
//            orderBean.qids = qidList;
//            return orderBean;
//        }catch (Exception e){
//            Toast.makeText(mainActivity,"传递的订单有误哦",Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    /////////////////////////////////////////////////////////String的处理//////////////////////////////////////////////
//
//    /**
//     * 打印账单
//     * @param orderStr
//     */
//    public void printOrder(MainActivity mainActivity,String orderStr){
//        this.mainActivity = mainActivity;
//        if(printer==null){
//            printer = new regoPrinter(mainActivity);//创建设备
//        }
//        //1.判读设备是否连着
//        if(!mBconnect){//没有连接，那么我们连接 （连接设备）
//            myState = printer.CON_ConnectDevices(printName, "usb", 200);//练级
//            if (myState > 0) {
//                Toast.makeText(mainActivity, "打印机连接成功",
//                        Toast.LENGTH_SHORT).show();
//                mBconnect = true;
//            }else{
//                mBconnect = false;
//                return;
//            }
//        }
//
////        orderStr = "{\"order_num\":\"YS1601252946\",\"zduser\":\"******\",\"time\":\"******\",\"username\":\"more\",\"uphone\":\"13632696141\",\"mname\":\"iphone 6\",\"price\":\"2600\",\"qids\":[\"\u5927\u9646\u56fd \u884c\",\"\u4e09\u7f51\u901a\",\"\u4fdd\u4fee\u4e00\u4e2a\u6708\u4ee5\u4e0a\",\"16G\",\"\u91d1\",\"\u6389\u6f06\u78d5\u78b0\",\"\u5c4f\u5e55\u65e0\u5212\u75d5\",\"\u663e\u793a \u6b63\u5e38\",\"转发ID梢发挥到死啊哈佛ID胡搜爱好覅懂撒哈佛ID会死哦啊发货地撒谎发电话撒范德萨覅偶抖擞呢\"],\"pay_type\":\"1\",\"rid\":\"65\",\"imei\":\"ee\"}";
//
//        //2、打印
////        toPrint();
////        toPrint3();
//        Log.i("-------orderStr-----------",orderStr);
//        OrderBean orderBean = getOrderBean(orderStr);
//        if(orderBean!=null){
//            toPrintOrder(orderBean);
//        }
//
//    }
//
//    /**
//     * 获取订单
//     * @param orderStr
//     * @return
//     */
//    private OrderBean getOrderBean(String orderStr){
//        Gson gson = new Gson();
//        try{
//            OrderBean orderBean = gson.fromJson(orderStr, OrderBean.class);
//            //设备情况的行数计算
//            //1.拼接所有的字符串
//            StringBuilder sb = new StringBuilder();
//            for (int i=0;i<orderBean.qids.size();i++){
//                if(i>0){
//                    sb.append("/");
//                }
//                sb.append(orderBean.qids.get(i));
//            }
//            String qidStr = sb.toString();
//            //2.根据字符串的长度截取字符
//            int col = qidStr.length()/wordSize+1;
//            List<String> qidList = new ArrayList<String>();
//            if(col<=1){
//                qidList.add(qidStr);
//            }else{
//                for (int i=1;i<=col;i++){
//                    if(i!=col){//不是最后一行
//                        qidList.add(qidStr.substring((i - 1) * wordSize, i * wordSize));
//                    }else{//最后一行，直接加入
//                        qidList.add(qidStr.substring((col-1)*wordSize));
//                    }
//                }
//            }
//            orderBean.qids = qidList;
//            return orderBean;
//        }catch (Exception e){
//            Toast.makeText(mainActivity,"传递的订单有误哦",Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    private void toPrintOrder(OrderBean orderBean){
//        //到设备情况的高度 + 线条 + 回收价
//        int devicesQueHeight = 337+orderBean.qids.size()*35 +30+ 5 + 20 + 40;
//        int width1 = 337;
//        printer.CON_PageStart(myState, true, 540, devicesQueHeight);
//        printer.DRAW_SetFillMode(false);
//        printer.DRAW_SetLineWidth(4);
//        //矩形
//        printer.DRAW_PrintRectangle(myState, 18, 10, 540, devicesQueHeight);
//        printer.DRAW_PrintText(myState, 108, 32, "爱易收手机回收凭证", 40);
//        printer.DRAW_PrintText(myState, 108, 82, "服务门店："+orderBean.store, 20);
//        printer.DRAW_SetLineWidth(2);
//        printer.DRAW_PrintLine(myState, 38, 107, 500, 107);//线条
//        printer.DRAW_PrintText(myState, 38, 127, "订单号：" + orderBean.order_num, 25);
//        printer.DRAW_PrintText(myState, 38, 167, "时间："+orderBean.create_time, 25);
//        printer.DRAW_PrintText(myState, 38, 207, "姓名："+orderBean.username+"     联系电话："+orderBean.uphone, 25);
//        printer.DRAW_PrintLine(myState, 38, 237, 500, 237);//线条
//        printer.DRAW_PrintText(myState, 38, 257, "机型："+orderBean.mname, 25);
//        printer.DRAW_PrintText(myState, 38, 297, "IMEI："+orderBean.imei, 25);
//        printer.DRAW_PrintText(myState, 38, 337, "设备情况：", 25);
//        //下面的高度需要更具字符的个数计算
//        for (int i = 0; i < orderBean.qids.size(); i++) {
//            width1+=35;
//            printer.DRAW_PrintText(myState, 38, width1, orderBean.qids.get(i), 25);
//        }
//        width1 +=25;//上面的最后一行
//        printer.DRAW_PrintLine(myState, 38, width1+5, 500, width1+5);//线条
//        printer.DRAW_PrintText(myState, 38, width1 + 5 + 20, "回收价：" + orderBean.price + "元RMB", 25);
//        printer.CON_PageEnd(myState, 0);
//        //切纸功能
//        printer.CON_PageStart(myState, false, 0, 0);
//        printer.ASCII_CtrlCutPaper(myState, 10, 30);
//        printer.CON_PageEnd(myState,0);
//    }


}
