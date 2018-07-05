package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.DepotOrder;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-5-15
 * <p>
 * 拆分订单
 * <p>
 * 订单生成器
 */
public abstract class GeneratorDepot extends OrderGenerator<DepotOrder, NormalGoodsGroup, Goods> {

    public GeneratorDepot(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    protected void saveDataBase() {

        operationManager.minceOrderDao.save(this.order);

    }

}
