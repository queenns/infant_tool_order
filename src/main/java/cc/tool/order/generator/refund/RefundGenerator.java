package cc.tool.order.generator.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.order.Order;
import cc.tool.order.OrderRefundTool;
import cc.tool.order.builder.refund.RefundBuilder;
import cc.tool.order.exception.CreateException;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-6-15
 * <p>
 * 退款记录申请生成器
 */
public abstract class RefundGenerator extends AbstractGenerator<MakeInfo> implements OrderRefundTool {

    protected Order order;

    public RefundInfo refundInfo;

    public OperationManager operationManager;

    protected MakeInfo makeInfo = new MakeInfo();

    public RefundGenerator(RefundInfo refundInfo, OperationManager operationManager) {

        this.refundInfo = refundInfo;

        this.operationManager = operationManager;

        this.order = operationManager.masterOrderDao.find(refundInfo.getOrderId());

    }

    public abstract RefundBuilder createRefundBuilder();

    protected abstract Boolean validate();

    @Override
    public MakeInfo create() throws CreateException {

        if (!validate()) return makeInfo;

        FollowOrder followOrder = createRefundBuilder().build();

        operationManager.followOrderDao.save(followOrder);

        logger.info("refund build followOrder[{}]", followOrder.toString());

        updateOrderRefundInfo(followOrder);

        makeInfo.setSuccess(true);

        return makeInfo;

    }

    protected abstract void updateOrderRefundInfo(FollowOrder followOrder);

}
