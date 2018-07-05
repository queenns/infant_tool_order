package cc.tool.order.bucket.order;


import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Set;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 订单预信息缓存
 * <p>
 * 1.暴露要生成的订单信息，便于处理
 * 2.防止涉及订单生成时，使用的信息重复查询
 * 3.统一接口式编写
 */
public interface OrderBucketCache<P, G> {

    /**
     * 默认地址
     *
     * @return {@link Dress}
     */
    Dress findDress();

    /**
     * 商品总价格
     *
     * @return Long
     */
    Long findAmount();

    /**
     * 邮费
     *
     * @return Long
     */
    Long findPostage();

    /**
     * 订单实际金额
     *
     * @return Long
     */
    Long findActualAmount();

    /**
     * 用户信息
     *
     * @return {@link Customer}
     */
    Customer findCustomer();

    /**
     * 商品编号集合
     *
     * @return goodsNos
     */
    Set<String> findGoodsNos();

    /**
     * 商品组信息
     *
     * @return Group
     */
    Map<ObjectId, P> findGroups();

    /**
     * 商品信息
     *
     * @return Goods
     */
    Map<ObjectId, G> findGoodses();

    /**
     * 订单快照
     *
     * @return {@link OrderGoodsInfo}
     */
    Map<ObjectId, OrderGoodsInfo> findOrderGoodsInfos();

}
