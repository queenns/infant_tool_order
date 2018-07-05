package cc.tool.order.generator.refund;

import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-6-15
 * <p>
 * 订单直接售后
 */
public abstract class RefundOrder extends RefundGenerator {

    public RefundOrder(RefundInfo refundInfo, OperationManager operationManager) {

        super(refundInfo, operationManager);

    }

}
