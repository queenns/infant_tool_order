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
 * Created by lxj on 18-5-7
 * <p>
 * 小程序预订单信息处理
 * <p>
 * 亲子工厂
 */
public class BucketMiniQzgc extends SchemeBucket<NormalGoodsGroup> {

    public BucketMiniQzgc(OrderGenerator<?, NormalGoodsGroup, Goods> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Dress operationQueryDress() {

        Customer customer = findCustomer();

        return orderGenerator.operationManager.shareDressDao.findDefault(customer.getId(), ChannelFromMark.WX_PROGRAM_QZGC);

    }

    @Override
    protected Customer operationQueryCustomer() {

        OrderInfoMini orderInfoMini = (OrderInfoMini) orderGenerator.orderInfo;

        return orderGenerator.operationManager.miniCustomerDao.find(orderInfoMini.getMiniOpenId(), ChannelFromMark.WX_PROGRAM_QZGC);

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