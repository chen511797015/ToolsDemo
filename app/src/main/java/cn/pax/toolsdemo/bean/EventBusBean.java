package cn.pax.toolsdemo.bean;

/**
 * Created by chendd on 2017/1/11.
 * EventBus传递参数
 */

public class EventBusBean {

    String cameraResult;//扫码返回值


    public EventBusBean() {
    }

    public EventBusBean(String cameraResult) {
        this.cameraResult = cameraResult;
    }

    public String getCameraResult() {
        return cameraResult;
    }

    public void setCameraResult(String cameraResult) {
        this.cameraResult = cameraResult;
    }
}
