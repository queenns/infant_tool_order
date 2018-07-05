package cc.tool.order.model.confirm;

import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmInfo extends Info {

    private ObjectId orderId;

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ConfirmInfo{" +
                "orderId=" + orderId +
                "} ";
    }

}
