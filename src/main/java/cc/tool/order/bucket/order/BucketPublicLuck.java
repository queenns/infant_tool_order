package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.luckyDraw.LuckyDrawGoods;
import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.model.order.OrderInfoPublicLuck;
import cc.tool.order.util.NumberUtil;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lxj on 18-5-8
 * <p>
 * 公众号预信息处理
 * <p>
 * 抽奖
 */
public class BucketPublicLuck extends BaseBucket<Object, LuckyDrawGoods> {

    public BucketPublicLuck(OrderGenerator<?, Object, LuckyDrawGoods> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Dress operationQueryDress() {

        OrderInfoPublicLuck orderInfo = (OrderInfoPublicLuck) orderGenerator.orderInfo;

        return orderInfo.getLuckDrawCustomer().getDress();

    }

    @Override
    protected Customer operationQueryCustomer() {

        OrderInfo orderInfo = orderGenerator.orderInfo;

        return orderGenerator.operationManager.publicCustomerDao.find(orderInfo.getOpenId(), ChannelFromMark.WX_PUBLIC_ENSJ);

    }

    @Override
    protected Long cachePostage() {

        return 0L;

    }

    @Override
    protected Long cacheActualAmount() {

        return 0L;

    }

    @Override
    protected Set<String> cacheGoodsNos() {

        Map<ObjectId, LuckyDrawGoods> goodses = findGoodses();

        Set<String> goodsNos = new HashSet<String>(goodses.size());

        for (LuckyDrawGoods goods : goodses.values()) {

            goodsNos.add(goods.getGoodsNo());

        }

        setGoodsNos(goodsNos);

        return getGoodsNos();

    }

    @Override
    protected Map<ObjectId, Object> cacheGroups() {

        throw new IllegalStateException("BucketPublicLuck does not cache groups,Because it doesn't have a Group");

    }

    @Override
    protected Map<ObjectId, LuckyDrawGoods> cacheGoodses() {

        Map<ObjectId, LuckyDrawGoods> goodses = new HashMap<ObjectId, LuckyDrawGoods>(1);

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        for (ObjectId goodsId : countInfos.keySet()) {

            LuckyDrawGoods goods = orderGenerator.operationManager.goodsRedisHandle.findLuckyDrawGoods(goodsId.toString());

            goodses.put(goods.getId(), goods);

        }

        setGoodses(goodses);

        return getGoodses();

    }

    @Override
    protected Map<ObjectId, OrderGoodsInfo> cacheOrderGoodsInfos() {

        Map<ObjectId, OrderGoodsInfo> orderGoodsInfos = new HashMap<ObjectId, OrderGoodsInfo>();

        Map<ObjectId, LuckyDrawGoods> goodses = findGoodses();

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        for (ObjectId goodsId : countInfos.keySet()) {

            Long count = countInfos.get(goodsId);

            LuckyDrawGoods goods = goodses.get(goodsId);

            OrderGoodsInfo orderGoodsInfo = new OrderGoodsInfo();

            orderGoodsInfo.setCount(count);
            orderGoodsInfo.setGoodsId(goodsId);
            orderGoodsInfo.setPostage(findPostage());
            orderGoodsInfo.setGoodsNo(goods.getGoodsNo());
            orderGoodsInfo.setGoodsName(goods.getGoodsName());
            orderGoodsInfo.setPrice(NumberUtil.format(goods.getPrice()));
            orderGoodsInfo.setImageUrl(goods.getGoodsShopCarImage().getUrl());
            orderGoodsInfo.setCountPrice(NumberUtil.multiply(orderGoodsInfo.getPrice(), orderGoodsInfo.getCount()));

            orderGoodsInfos.put(goodsId, orderGoodsInfo);

        }

        setOrderGoodsInfos(orderGoodsInfos);

        return getOrderGoodsInfos();

    }

}
