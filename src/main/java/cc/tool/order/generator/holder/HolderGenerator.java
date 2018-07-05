package cc.tool.order.generator.holder;

import cc.admore.infant.model.order.Order;
import cc.tool.order.OrderHolderTool;
import cc.tool.order.action.HolderMasterAction;
import cc.tool.order.exception.CreateException;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.MakeInfo;
import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-5-31
 * <p>
 * 库存持有生成器
 */
public class HolderGenerator extends AbstractGenerator<MakeInfo> implements OrderHolderTool {

    private Order order;

    private OrderFactory orderFactory;

    private MakeInfo makeInfo = new MakeInfo();

    public HolderGenerator(ObjectId orderId, OrderFactory orderFactory) {

        this.orderFactory = orderFactory;

        this.order = orderFactory.operationManager.masterOrderDao.find(orderId);

    }

    @Override
    public MakeInfo create() throws CreateException {

        makeInfo.setSuccess(true);

        if (order.getHoldMarker()) return makeInfo;

        HolderMasterAction holderMasterAction = orderFactory.findHolderMasterAction(order.getCountInfos());

        makeInfo = holderMasterAction.action();

        if (!makeInfo.success) return makeInfo;

        makeInfo = orderFactory.findBrokenAction(order).action();

        if (!makeInfo.success) holderMasterAction.fail();

        return makeInfo;

    }

}
