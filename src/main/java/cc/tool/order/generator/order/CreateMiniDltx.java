package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.MiniOrder;
import cc.tool.order.bucket.order.BucketMiniDltx;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderMiniDltx;
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
 * 点亮童星订单生成器
 */
public class CreateMiniDltx extends OrderGenerator<MiniOrder, NormalGoodsGroup, Goods> {

    public CreateMiniDltx(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<NormalGoodsGroup, Goods> createOrderBucket() {

        return new BucketMiniDltx(this);

    }

    @Override
    protected OrderBuilder<MiniOrder> createOrderBuilder() {

        return new BuilderMiniDltx(this);

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