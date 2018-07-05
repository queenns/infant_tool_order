package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.GoodsGroup;
import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.tool.order.generator.order.OrderGenerator;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-7
 * <p>
 * 公众号预信息处理
 * <p>
 * 抢购
 */
public class BucketPublicPanic extends SchemeBucket<GoodsGroup> {

    public BucketPublicPanic(OrderGenerator<?, GoodsGroup, Goods> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Dress operationQueryDress() {

        Customer customer = findCustomer();

        return orderGenerator.operationManager.defineDressDao.findDefault(customer.getId(), ChannelFromMark.WX_PUBLIC_ENSJ);

    }

    @Override
    protected Customer operationQueryCustomer() {

        String openId = orderGenerator.orderInfo.getOpenId();

        return orderGenerator.operationManager.publicCustomerDao.find(openId, ChannelFromMark.WX_PUBLIC_ENSJ);

    }

    @Override
    protected List<Goods> operationQueryGoods() {

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        return orderGenerator.operationManager.goodsRedisHandle.findGoodses(countInfos.keySet());

    }

    @Override
    protected List<GoodsGroup> operationQueryGroups(Collection<ObjectId> groupIds) {

        return orderGenerator.operationManager.groupRedisHandle.findGroups(groupIds, GoodsGroup.class);

    }

}
