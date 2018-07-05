package cc.tool.order.bucket.order;

import cc.admore.infant.dao.entity.Goods;
import cc.admore.infant.dao.entity.goods.PublicGoodsInfo;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.util.NumberUtil;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by lxj on 18-5-3
 * <p>
 * 计划形式的实物信息处理
 * <p>
 * Group must Subclasses of PublicGoodsInfo
 * <p>
 * Goods
 */
public abstract class SchemeBucket<P extends PublicGoodsInfo> extends BaseBucket<P, Goods> {

    SchemeBucket(OrderGenerator<?, P, Goods> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Set<String> cacheGoodsNos() {

        Map<ObjectId, Goods> goodses = findGoodses();

        Set<String> goodsNos = new HashSet<String>(goodses.size());

        for (Goods goods : goodses.values()) {

            goodsNos.add(goods.getGoodsNo());

        }

        setGoodsNos(goodsNos);

        return getGoodsNos();

    }

    @Override
    protected Map<ObjectId, P> cacheGroups() {

        Set<ObjectId> groupIds = new HashSet<ObjectId>(4);

        Map<ObjectId, Goods> goodses = findGoodses();

        for (Goods goods : goodses.values()) {

            groupIds.add(goods.getGroupId());

        }

        List<P> queryGroups = operationQueryGroups(groupIds);

        Map<ObjectId, P> groups = new HashMap<ObjectId, P>();

        for (P queryGroup : queryGroups) {

            groups.put(queryGroup.getId(), queryGroup);

        }

        setGroups(groups);

        return getGroups();

    }

    @Override
    protected Map<ObjectId, Goods> cacheGoodses() {

        Map<ObjectId, Goods> goodses = new HashMap<ObjectId, Goods>(8);

        List<Goods> goodsInfos = operationQueryGoods();

        for (Goods goods : goodsInfos) {

            goodses.put(goods.getId(), goods);

        }

        setGoodses(goodses);

        return getGoodses();

    }

    @Override
    protected Map<ObjectId, OrderGoodsInfo> cacheOrderGoodsInfos() {

        Map<ObjectId, OrderGoodsInfo> orderGoodsInfos = new HashMap<ObjectId, OrderGoodsInfo>();

        Map<ObjectId, P> groups = findGroups();

        Map<ObjectId, Goods> goodses = findGoodses();

        Map<ObjectId, Long> countInfos = orderGenerator.orderInfo.getCountInfos();

        for (ObjectId goodsId : countInfos.keySet()) {

            Goods goods = goodses.get(goodsId);

            Long count = countInfos.get(goodsId);

            P group = groups.get(goods.getGroupId());

            OrderGoodsInfo orderGoodsInfo = new OrderGoodsInfo();

            orderGoodsInfo.setCount(count);
            orderGoodsInfo.setGoodsId(goodsId);
            orderGoodsInfo.setColor(goods.getColor());
            orderGoodsInfo.setParam(goods.getParam());
            orderGoodsInfo.setSummary(group.getSummary());
            orderGoodsInfo.setGoodsNo(goods.getGoodsNo());
            orderGoodsInfo.setGoodsName(goods.getGoodsName());
            orderGoodsInfo.setPrice(NumberUtil.format(goods.getPrice()));
            orderGoodsInfo.setImageUrl(group.getThumbnailImage().getUrl());
            orderGoodsInfo.setDistributionMarker(goods.getIsDistribution());
            orderGoodsInfo.setPostage(NumberUtil.format(group.getPostage()));
            orderGoodsInfo.setPurchasePrice(NumberUtil.format(goods.getPurchasePrice()));
            orderGoodsInfo.setCountPrice(NumberUtil.multiply(orderGoodsInfo.getPrice(), orderGoodsInfo.getCount()));

            orderGoodsInfos.put(goods.getId(), orderGoodsInfo);

        }

        setOrderGoodsInfos(orderGoodsInfos);

        return getOrderGoodsInfos();

    }

    protected abstract List<Goods> operationQueryGoods();

    protected abstract List<P> operationQueryGroups(Collection<ObjectId> groupIds);

}
