package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.DepotOrder;
import cc.tool.order.bucket.order.BucketDepotDltx;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderDepotDltx;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.model.order.OrderInfoDepot;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-14
 * <p>
 * 拆分订单
 * <p>
 * 点亮童星订单生成器
 */
public class CreateDepotDltx extends GeneratorDepot {

    public CreateDepotDltx(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<NormalGoodsGroup, Goods> createOrderBucket() {

        return new BucketDepotDltx(this);

    }

    @Override
    protected OrderBuilder<DepotOrder> createOrderBuilder() {

        return new BuilderDepotDltx(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfoDepot orderInfo = (OrderInfoDepot) i;

        ParamValidate.isEmpty(

                new Object[]{"openId", orderInfo.getOpenId()},

                new Object[]{"shopId", orderInfo.getShopId()},

                new Object[]{"order", orderInfo.getOrder()}

        );

    }

}
