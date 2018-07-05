package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.luckyDraw.LuckyDrawGoods;
import cc.admore.infant.model.order.LuckOrder;
import cc.tool.order.bucket.order.BucketPublicLuck;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderPublicLuck;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.model.order.OrderInfoPublicLuck;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-8
 * <p>
 * 公众号
 * <p>
 * 抽奖订单生成器
 */
public class CreatePublicLuck extends OrderGenerator<LuckOrder, Object, LuckyDrawGoods> {

    public CreatePublicLuck(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<Object, LuckyDrawGoods> createOrderBucket() {

        return new BucketPublicLuck(this);

    }

    @Override
    protected OrderBuilder<LuckOrder> createOrderBuilder() {

        return new BuilderPublicLuck(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfoPublicLuck orderInfo = (OrderInfoPublicLuck) i;

        ParamValidate.isEmpty(

                new Object[]{"openId", orderInfo.getOpenId()},

                new Object[]{"luckCustomer", orderInfo.getLuckDrawCustomer()}

        );

    }

}
