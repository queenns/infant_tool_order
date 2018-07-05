package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.NormalGoodsGroup;
import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoMini;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-14
 */
public class BucketMiniKtzc extends SchemeBucket<NormalGoodsGroup> {

    public BucketMiniKtzc(OrderGenerator<?, NormalGoodsGroup, Goods> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Dress operationQueryDress() {

        Customer customer = findCustomer();

        return orderGenerator.operationManager.defineDressDao.findDefault(customer.getId(), ChannelFromMark.WX_PROGRAM_KTZC);

    }

    @Override
    protected Customer operationQueryCustomer() {

        OrderInfoMini orderInfo = (OrderInfoMini) orderGenerator.orderInfo;

        return orderGenerator.operationManager.miniCustomerDao.find(orderInfo.getMiniOpenId(), ChannelFromMark.WX_PROGRAM_KTZC);

    }

    @Override
    protected List<Goods> operationQueryGoods() {

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        return orderGenerator.operationManager.goodsRedisHandle.findGoodses(countInfos.keySet());

    }

    @Override
    protected List<NormalGoodsGroup> operationQueryGroups(Collection<ObjectId> groupIds) {

        return orderGenerator.operationManager.groupRedisHandle.findGroups(groupIds, NormalGoodsGroup.class);

    }

}
