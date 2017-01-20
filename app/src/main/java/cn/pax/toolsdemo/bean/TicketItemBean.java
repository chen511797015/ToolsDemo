package cn.pax.toolsdemo.bean;

/**
 * Entity mapped to table "TICKET_ITEM_BEAN".
 */
public class TicketItemBean {

    private String goodsName = "";
    private String num = "";
    private String price = "";
    private String gTotal = "";


    public TicketItemBean() {
    }

    public TicketItemBean(String goodsName, String num, String price, String gTotal) {
        this.goodsName = goodsName;
        this.num = num;
        this.price = price;
        this.gTotal = gTotal;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGTotal() {
        return gTotal;
    }

    public void setGTotal(String gTotal) {
        this.gTotal = gTotal;
    }

}
