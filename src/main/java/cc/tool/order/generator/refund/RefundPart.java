package cc.tool.order.generator.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.builder.refund.RefundBuilder;
import cc.tool.order.builder.refund.RefundPartBuilder;
import cc.tool.order.model.Fail;
import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.operation.OperationManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 */
public class RefundPart extends RefundGenerator {

    public RefundPart(RefundInfo refundInfo, OperationManager operationManager) {
        super(refundInfo, operationManager);
    }

    @Override
    public RefundBuilder createRefundBuilder() {

        return new RefundPartBuilder(order, this);

    }

    @Override
    protected Boolean validate() {

        if (!Status.SIGNFOR.equals(order.getStatus())) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.REFUND_FAIL_STATUS.toString());

            return false;

        }

        return true;

    }

    @Override
    protected void updateOrderRefundInfo(FollowOrder followOrder) {

        Map<String, Object> updates = new HashMap<String, Object>();

        updates.put("followId", followOrder.getId());
        updates.put("status", Status.AFTERSALE_AFTER);

        operationManager.masterOrderDao.update(order.getId(), updates);

    }

}
