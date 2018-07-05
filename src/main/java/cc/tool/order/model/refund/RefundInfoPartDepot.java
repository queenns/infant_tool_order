package cc.tool.order.model.refund;

import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-6-19
 */
public class RefundInfoPartDepot extends RefundInfoPart {

    /**
     * depotOrderId
     */
    private ObjectId depotOrderId;

    public ObjectId getDepotOrderId() {
        return depotOrderId;
    }

    public void setDepotOrderId(ObjectId depotOrderId) {
        this.depotOrderId = depotOrderId;
    }

    @Override
    public String toString() {
        return "RefundInfoPartDepot{" +
                "depotOrderId=" + depotOrderId +
                "} " + super.toString();
    }

}