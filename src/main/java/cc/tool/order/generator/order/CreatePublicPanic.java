package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.GoodsGroup;
import cc.admore.infant.model.order.PanicOrder;
import cc.tool.order.bucket.order.BucketPublicPanic;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderPublicPanic;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 公共号
 * <p>
 * 抢购订单生成器
 */
public class CreatePublicPanic extends OrderGenerator<PanicOrder, GoodsGroup, Goods> {

    public CreatePublicPanic(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<GoodsGroup, Goods> createOrderBucket() {

        return new BucketPublicPanic(this);

    }

    @Override
    protected OrderBuilder<PanicOrder> createOrderBuilder() {

        return new BuilderPublicPanic(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfo orderInfo = (OrderInfo) i;

        ParamValidate.isEmpty("openId", orderInfo.getOpenId());

    }

}