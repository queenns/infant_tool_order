package cc.tool.order.model.confirm;

import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmInfoIntegral extends ConfirmInfo {

    private ObjectId customerId;

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "ConfirmInfoIntegral{" +
                "customerId=" + customerId +
                "} " + super.toString();
    }
}
