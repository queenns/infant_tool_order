package cc.tool.order.model.order;

/**
 * Created by lxj on 18-5-7
 */
public abstract class OrderInfoMini extends OrderInfo {

    /**
     * 小程序openId
     */
    private String miniOpenId;

    public String getMiniOpenId() {
        return miniOpenId;
    }

    public void setMiniOpenId(String miniOpenId) {
        this.miniOpenId = miniOpenId;
    }

    @Override
    public String toString() {
        return "OrderInfoMini{" +
                "miniOpenId='" + miniOpenId + '\'' +
                "} " + super.toString();
    }

}
