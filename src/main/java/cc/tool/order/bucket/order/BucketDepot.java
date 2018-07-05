package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.dao.entity.User;
import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoDepot;
import org.bson.types.ObjectId;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-11
 * <p>
 * 拆分订单预信息处理
 */
public abstract class BucketDepot extends SchemeBucket<NormalGoodsGroup> implements DepotBucketCache {

    /**
     * 默认地址
     */
    private User user;

    public BucketDepot(OrderGenerator<?, NormalGoodsGroup, Goods> orderGenerator) {
        super(orderGenerator);
    }

    protected User cacheUser() {

        ObjectId shopId = ((OrderInfoDepot) orderGenerator.orderInfo).getShopId();

        User user = orderGenerator.operationManager.userDao.createQuery().field("id").equal(shopId).get();

        setUser(user);

        return getUser();

    }

    @Override
    public User findUser() {

        return ObjectUtils.isEmpty(getUser()) ? cacheUser() : getUser();

    }

    @Override
    protected Dress operationQueryDress() {

        OrderInfoDepot orderInfo = (OrderInfoDepot) orderGenerator.orderInfo;

        return orderInfo.getOrder().getDress();

    }

    @Override
    protected Customer operationQueryCustomer() {

        OrderInfoDepot orderInfo = (OrderInfoDepot) orderGenerator.orderInfo;

        return orderInfo.getOrder().getCustomer();

    }

    protected List<Goods> operationQueryGoods() {

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        return orderGenerator.operationManager.goodsRedisHandle.findGoodses(countInfos.keySet());

    }

    @Override
    protected List<NormalGoodsGroup> operationQueryGroups(Collection<ObjectId> groupIds) {

        return orderGenerator.operationManager.groupRedisHandle.findGroups(groupIds, NormalGoodsGroup.class);

    }

    @Override
    protected Long cacheActualAmount() {

        setActualAmount(findAmount());

        return getActualAmount();

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
