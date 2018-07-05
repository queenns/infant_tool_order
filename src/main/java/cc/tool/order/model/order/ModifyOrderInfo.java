package cc.tool.order.model.order;

import cc.admore.infant.model.dress.Dress;
import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-6-22
 */
public class ModifyOrderInfo extends Info {

    private Dress dress;

    private String remark;

    private ObjectId orderId;

    public Dress getDress() {
        return dress;
    }

    public void setDress(Dress dress) {
        this.dress = dress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ModifyOrderInfo{" +
                "dress=" + dress +
                ", remark='" + remark + '\'' +
                ", orderId=" + orderId +
                "} " + super.toString();
    }

}