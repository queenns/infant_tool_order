package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.NormalOrder;
import cc.tool.order.bucket.order.BucketPublicNormal;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderPublicNormal;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 公众号
 * <p>
 * 常规订单生成器
 */
public class CreatePublicNormal extends OrderGenerator<NormalOrder, NormalGoodsGroup, Goods> {

    public CreatePublicNormal(OrderInfo orderInfo, OperationManager operationManager) {

        super(orderInfo, operationManager);

    }

    @Override
    protected OrderBucket<NormalGoodsGroup, Goods> createOrderBucket() {

        return new BucketPublicNormal(this);

    }

    @Override
    protected OrderBuilder<NormalOrder> createOrderBuilder() {

        return new BuilderPublicNormal(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfo orderInfo = (OrderInfo) i;

        ParamValidate.isEmpty("openId", orderInfo.getOpenId());

    }

}