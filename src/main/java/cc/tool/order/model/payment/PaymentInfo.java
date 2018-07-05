package cc.tool.order.model.payment;

import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.admore.infant.model.order.enums.PayMark;
import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-5-18
 */
public class PaymentInfo extends Info {

    /**
     * openId
     */
    private String openId;

    /**
     * orderId
     */
    private ObjectId orderId;

    /**
     * 支付方式
     */
    private PayMark payMark;

    /**
     * 来源
     */
    private FromMark fromMark;

    /**
     * 渠道来源
     */
    private ChannelFromMark channelFromMark;

    /**
     * 公益单数量
     */
    private Integer welfare = 0;

    /**
     * 留言
     */
    private String remark;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    public PayMark getPayMark() {
        return payMark;
    }

    public void setPayMark(PayMark payMark) {
        this.payMark = payMark;
    }

    public FromMark getFromMark() {
        return fromMark;
    }

    public void setFromMark(FromMark fromMark) {
        this.fromMark = fromMark;
    }

    public ChannelFromMark getChannelFromMark() {
        return channelFromMark;
    }

    public void setChannelFromMark(ChannelFromMark channelFromMark) {
        this.channelFromMark = channelFromMark;
    }

    public Integer getWelfare() {
        return welfare;
    }

    public void setWelfare(Integer welfare) {
        this.welfare = welfare;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "openId='" + openId + '\'' +
                ", orderId=" + orderId +
                ", payMark=" + payMark +
                ", fromMark=" + fromMark +
                ", channelFromMark=" + channelFromMark +
                ", welfare=" + welfare +
                ", remark='" + remark + '\'' +
                "} " + super.toString();
    }

}
