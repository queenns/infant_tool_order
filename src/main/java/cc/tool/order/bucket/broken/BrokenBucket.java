package cc.tool.order.bucket.broken;

import cc.admore.infant.model.order.Order;
import cc.tool.order.bucket.Bucket;
import cc.tool.order.model.broken.Broken;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by lxj on 18-5-16
 * <p>
 * 拆分订单预信息
 */
public class BrokenBucket extends Bucket {

    /**
     * 被拆分的订单
     */
    private Order order;

    /**
     * goodsId-->Broken
     */
    private Map<ObjectId, Broken> brokens;

    public OperationManager operationManager;

    public BrokenBucket(Order order, OperationManager operationManager) {

        this.order = order;

        this.operationManager = operationManager;

        init();

    }

    private void init() {

        initBrokens();

    }

    private void initBrokens() {

        Map<ObjectId, Long> countInfos = order.getCountInfos();

        List<ObjectId> goodsIds = new ArrayList<ObjectId>(countInfos.keySet());

        Map<ObjectId, List<String>> shopIdInfos = operationManager.goodsRedisHandle.pipelinedShopIds(goodsIds);

        Map<ObjectId, Broken> brokens = new HashMap<ObjectId, Broken>();

        for (ObjectId goodsId : shopIdInfos.keySet()) {

            List<String> shopIds = shopIdInfos.get(goodsId);

            if (CollectionUtils.isEmpty(shopIds)) continue;

            Long count = countInfos.get(goodsId);

            Map<String, Long> inventoryInfos = operationManager.shopRedisHandle.pipelinedInventorys(goodsId.toString(), shopIds);

            Broken broken = new Broken();

            broken.setCount(count);

            broken.setGoodsId(goodsId);

            broken.setInventoryInfos(inventoryInfos);

            brokens.put(goodsId, broken);

        }

        setBrokens(brokens);

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Map<ObjectId, Broken> getBrokens() {
        return brokens;
    }

    public void setBrokens(Map<ObjectId, Broken> brokens) {
        this.brokens = brokens;
    }

    @Override
    public String toString() {
        return "BrokenBucket{" +
                "order=" + order +
                ", brokens=" + brokens +
                "} " + super.toString();
    }

}
