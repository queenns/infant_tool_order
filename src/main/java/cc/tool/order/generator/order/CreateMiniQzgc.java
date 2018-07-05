package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.MiniOrder;
import cc.tool.order.bucket.order.BucketMiniQzgc;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderMiniQzgc;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.model.order.OrderInfoMiniQzgc;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-7
 * <p>
 * 小程序
 * <p>
 * 亲子工厂订单生成器
 */
public class CreateMiniQzgc extends OrderGenerator<MiniOrder, NormalGoodsGroup, Goods> {

    public CreateMiniQzgc(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<NormalGoodsGroup, Goods> createOrderBucket() {

        return new BucketMiniQzgc(this);

    }

    @Override
    protected OrderBuilder<MiniOrder> createOrderBuilder() {

        return new BuilderMiniQzgc(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfoMiniQzgc orderInfo = (OrderInfoMiniQzgc) i;

        ParamValidate.isEmpty(

                new Object[]{"openId", orderInfo.getOpenId()},

                new Object[]{"miniOpenId", orderInfo.getMiniOpenId()}

        );


    }

}