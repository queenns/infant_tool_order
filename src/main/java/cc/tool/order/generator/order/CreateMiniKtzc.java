package cc.tool.order.generator.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.order.MiniOrder;
import cc.tool.order.bucket.order.BucketMiniKtzc;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.BuilderMiniKtzc;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.model.order.OrderInfoMiniKtzc;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-14
 */
public class CreateMiniKtzc extends OrderGenerator<MiniOrder, NormalGoodsGroup, Goods> {

    public CreateMiniKtzc(OrderInfo orderInfo, OperationManager operationManager) {
        super(orderInfo, operationManager);
    }

    @Override
    protected OrderBucket<NormalGoodsGroup, Goods> createOrderBucket() {

        return new BucketMiniKtzc(this);

    }

    @Override
    protected OrderBuilder<MiniOrder> createOrderBuilder() {

        return new BuilderMiniKtzc(this);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        OrderInfoMiniKtzc orderInfo = (OrderInfoMiniKtzc) i;

        ParamValidate.isEmpty("miniOpenId", orderInfo.getMiniOpenId());

    }

}
