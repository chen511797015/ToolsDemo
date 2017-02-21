package cn.pax.toolsdemo.tool;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.pax.toolsdemo.bean.TicketItemBean;
import cn.pax.toolsdemo.util.NumberUtil;
import cn.pax.toolsdemo.util.PrinterUtil;

/**
 * Created by chendd on 2017/1/20.
 * 打印机测试页
 */

public class PrintOrderThread implements Runnable {

    public static final String TAG = "PrintTestThread";
    private Context mContext;

    public PrintOrderThread(Context context) {
        this.mContext = context;
    }

    @Override
    public void run() {
        printOfOrder();
    }


    /**
     * 打印订单
     */
    private void printOfOrder() {
        try {
            print(new CountDownLatch(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印
     *
     * @param latch
     */
    private void print(CountDownLatch latch) {
        String change = "14.00";
        String receiver = "50.00";
        // 打印配置信息
        boolean printCode = false;  // 商品编码
        boolean printStandard = false;  // 商品规格
        boolean printIntegral = false; // 积分
        boolean printEmployee = false; // 导购员
        boolean printVipCode = false; // 会员号

        double total = 0d;
        double discountTotal = 0d;
        HashMap<String, Object> hashMap = new HashMap<>();
        List<TicketItemBean> items = new ArrayList<>();
        try {
            TicketItemBean item;
            String newName = null;
            int num = 1;
            item = new TicketItemBean();
            newName = "老坛酸菜牛肉面";
            if (printStandard) {// 打印规格
                newName += "/500克";
            }

            int nameCount = PublicMethod.getWordCount(new String(newName));
            String priceStr = "18.00";
            if (nameCount >= 12) {
                item.setGoodsName(newName + "\r\n");
            }
            if (nameCount >= 12) {
                if (printCode) {
                    item.setPrice("6921531531796" + PublicMethod.getSpaceByNum(15 - PublicMethod.getWordCount("6921531531796")) + priceStr + PublicMethod.getSpaceByNum(9 - priceStr.length()));
                } else {
                    item.setPrice(PublicMethod.getSpaceByNum(15) + priceStr + PublicMethod.getSpaceByNum(9 - priceStr.length()));
                }
            } else {
                if (printCode) {
                    item.setPrice("6921531531796" + PublicMethod.getSpaceByNum(15 - PublicMethod.getWordCount("6921531531796")) + priceStr + PublicMethod.getSpaceByNum(9 - priceStr.length()));
                } else {
                    item.setPrice(PublicMethod.getSpaceByNum(15 - nameCount) + priceStr + PublicMethod.getSpaceByNum(9 - priceStr.length()));
                }
            }
            item.setNum(2 + PublicMethod.getSpaceByNum(5 - (2 + "").length()));
            double gTotal = 36.00;
            //累加折前价
            total += gTotal;

            discountTotal += gTotal;
            item.setGTotal(PublicMethod.getSpaceByNum(7 - (NumberUtil.cutDouble(gTotal, 2) + "").length()) + NumberUtil.cutDouble(gTotal, 2) + "\r\n");
            num++;
            items.add(item);

            //店铺名称
            hashMap.put("shopName", "百富智能收银");
            /***********************************************
             * 订单信息部分↓
             ******************************************************************************/
            StringBuffer headDetail = new StringBuffer();
            // 订单号
            headDetail.append("订单号:");
            headDetail.append("123456789");
            headDetail.append(PublicMethod.getSpaceByNum(12));

            if (printEmployee) {
                headDetail.append("导购员:");
                headDetail.append(166666);
            }

            //收银员
            headDetail.append("\r\n").append("收银员:");
            String name = "188***8888";
            headDetail.append(name + PublicMethod.getSpaceByNum(6 - (name + "").length())).append("\r\n");
            //结账时间
            headDetail.append("结账时间:");
            headDetail.append("2017-01-01-12:00");
            hashMap.put("headDetail", headDetail.toString());
            /*******************************************************商品列表↓**********************************************************************/
            hashMap.put("items", items);
            hashMap.put("bold", new String(new byte[]{EpsonPosPrinterCommand.GS, '!', 0x10, EpsonPosPrinterCommand.GS, '!', 0x01}));
            hashMap.put("unbold", new String(new byte[]{EpsonPosPrinterCommand.GS, '!', 0x00}));
            hashMap.put("ALIGN_RIGHT", new String(EpsonPosPrinterCommand.ESC_ALIGN_RIGHT));
            hashMap.put("ALIGN_LEFT", new String(EpsonPosPrinterCommand.ESC_ALIGN_LEFT));
            hashMap.put("ALIGN_CENTER", new String(EpsonPosPrinterCommand.ESC_ALIGN_CENTER));
            hashMap.put("ESC_EM_ON", new String(EpsonPosPrinterCommand.ESC_EM_ON));
            hashMap.put("ESC_EM_OFF", new String(EpsonPosPrinterCommand.ESC_EM_OFF));

            /*************************************************************************
             * 结算金额部分↓
             **************************************************************************/
            StringBuffer cashDetail = new StringBuffer();
            // 合计金额
            String hj = "合  计";
            cashDetail.append(hj);
            cashDetail.append(PublicMethod.getSpaceByNum(20) + "36.00").append("\r\n");
            // 预付金
            String yfj = "预付金";
            cashDetail.append(yfj);
            cashDetail.append(PublicMethod.getSpaceByNum(20) + "0.00").append("\r\n");

            String yf = "应  付";
            cashDetail.append(yf).append(PublicMethod.getSpaceByNum(20));
            cashDetail.append("36.00").append("\r\n");

            // 实付金额
            String sf = "实  付";
            cashDetail.append(sf);
            String realPay = "50.00";
            cashDetail.append(PublicMethod.getSpaceByNum(20) + realPay).append("\r\n");

            // 找零
            String zl = "找  零";
            cashDetail.append(zl).append(PublicMethod.getSpaceByNum(20));
            cashDetail.append(change);
            hashMap.put("cashDetail", cashDetail.toString());

            /*****************************************************
             * 地址部分↓
             ************************************************************************/
            StringBuffer endDetail = new StringBuffer();
            //会员号
            if (printVipCode) {
                endDetail.append("会员号:");
                endDetail.append("152****1207").append("\r\n");
            }
            //本次积分
            if (printIntegral) {
                endDetail.append("本次积分:3").append("\r\n");
            }

            //联系电话
            endDetail.append("联系电话:").append("0755-86238594").append("\r\n");
            //地址
            endDetail.append("地址:");
            endDetail.append("深圳市南山区高新区科技中二路软件园3号楼四层");
            hashMap.put("endDetail", endDetail.toString());
            /****************************************************
             * 打印↓
             *************************************************************************/
            final String templeStr = PrinterUtil.getTempleStr(hashMap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(EpsonPosPrinterCommand.ESC_ALIGN_CENTER);
            byteArrayOutputStream.write(PrinterUtil.decodeBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("pa.png"))));
            byteArrayOutputStream.write(EpsonPosPrinterCommand.ESC_ALIGN_LEFT);
            PrinterUtil.writeData(mContext, byteArrayOutputStream.toByteArray());
            PrinterUtil.writeData(mContext, templeStr);
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
