package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.dao.entity.User;

/**
 * Created by lxj on 18-6-12
 */
public interface DepotBucketCache extends OrderBucketCache<NormalGoodsGroup, Goods> {

    /**
     * 店铺
     *
     * @return {@link User}
     */
    User findUser();

}
