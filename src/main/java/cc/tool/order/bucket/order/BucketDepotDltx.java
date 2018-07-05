package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.tool.order.generator.order.OrderGenerator;

/**
 * Created by lxj on 18-5-14
 * <p>
 * 拆分订单预信息处理
 * <p>
 * 点亮童星
 */
public class BucketDepotDltx extends BucketDepot {

    public BucketDepotDltx(OrderGenerator<?, NormalGoodsGroup, Goods> orderGenerator) {
        super(orderGenerator);
    }

}
