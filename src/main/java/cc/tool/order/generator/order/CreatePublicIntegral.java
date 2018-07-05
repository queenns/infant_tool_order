package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.IntegralGoodsGroup;
import cc.admore.infant.model.order.IntegralOrder;
import cc.tool.order.bucket.order.BucketPublicIntegral;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderPublicIntegral;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-7
 * <p>
 * 公众号
 * <p>
 * 积分订单生成器
 */
public class CreatePublicIntegral extends OrderGenerator<IntegralOrder, IntegralGoodsGroup, Goods> {

    public CreatePublicIntegral(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<IntegralGoodsGroup, Goods> createOrderBucket() {

        return new BucketPublicIntegral(this);

    }

    @Override
    protected OrderBuilder<IntegralOrder> createOrderBuilder() {

        return new BuilderPublicIntegral(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfo orderInfo = (OrderInfo) i;

        ParamValidate.isEmpty("openId", orderInfo.getOpenId());

    }

}