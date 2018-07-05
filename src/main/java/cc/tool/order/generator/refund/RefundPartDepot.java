package cc.tool.order.generator.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.builder.refund.RefundBuilder;
import cc.tool.order.builder.refund.RefundPartDepotBuilder;
import cc.tool.order.model.Fail;
import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.model.refund.RefundInfoPartDepot;
import cc.tool.order.operation.OperationManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-6-19
 */
public class RefundPartDepot extends RefundGenerator {

    private DepotOrder depotOrder;

    public RefundPartDepot(RefundInfo refundInfo, OperationManager operationManager) {
        super(refundInfo, operationManager);
    }

    @Override
    public RefundBuilder createRefundBuilder() {

        return new RefundPartDepotBuilder(depotOrder, order, this);

    }

    @Override
    protected Boolean validate() {

        RefundInfoPartDepot refundInfoPartDepot = (RefundInfoPartDepot) refundInfo;

        depotOrder = operationManager.minceOrderDao.find(refundInfoPartDepot.getDepotOrderId());

        if (!Status.SIGNFOR.equals(depotOrder.getStatus())) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.REFUND_FAIL_STATUS.toString());

            return false;

        }

        return true;

    }

    @Override
    protected void updateOrderRefundInfo(FollowOrder followOrder) {

        operationManager.masterOrderDao.update(order.getId(), "status", Status.AFTERSALE_AFTER);

        Map<String, Object> updates = new HashMap<String, Object>();

        updates.put("followId", followOrder.getId());
        updates.put("status", Status.AFTERSALE_AFTER);

        operationManager.minceOrderDao.update(depotOrder.getId(), updates);

    }

}
