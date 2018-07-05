package cc.tool.order.action;

import cc.admore.infant.dao.entity.enums.OperateResult;
import cc.admore.infant.model.holder.HolderBroken;
import cc.admore.infant.model.holder.Operation;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.operation.OperationManager;
import com.yunrang.libra.handle.ShopRedisHandle;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Created by lxj on 18-5-28
 */
public class HolderBrokenAction extends HolderAction<HolderBroken> {

    private Map<ObjectId, BrokenInfo> brokenInfos;

    public HolderBrokenAction(Map<ObjectId, BrokenInfo> brokenInfos, OperationManager operationManager) {

        super(operationManager);

        this.brokenInfos = brokenInfos;

    }

    @Override
    protected void holder() {

        ShopRedisHandle shopRedisHandle = operationManager.shopRedisHandle;

        for (BrokenInfo brokenInfo : brokenInfos.values()) {

            ObjectId shopId = brokenInfo.getShopId();

            Map<ObjectId, Long> countInfos = brokenInfo.getCountInfos();

            for (ObjectId goodsId : countInfos.keySet()) {

                Long count = countInfos.get(goodsId);

                OperateResult operateResult = shopRedisHandle.decrbyGoodsInventory(shopId.toString(), goodsId.toString(), count);

                if (OperateResult.SUCCESS.equals(operateResult)) {

                    addHolder(shopId, goodsId, count);

                } else {

                    holderFail(operateResult);

                    return;

                }

            }

        }

        callback();

        makeInfo.setSuccess(true);

    }

    /**
     * 持有失败处理
     * <p>
     * 1.持有成功记录{@link this#holders}为空时返回
     * 2.持有成功记录{@link this#holders}库存回写处理，异步队列回写
     */
    @Override
    protected void fail() {

        if (CollectionUtils.isEmpty(holders)) return;

        for (HolderBroken holderBroken : holders) {

            operationManager.holderClientHandle.sendHolderBrokenQueue(holderBroken);

            logger.info("holder broken fail send queue {}", holderBroken.toString());

        }

    }

    /**
     * 添加持有成功记录
     *
     * @param shopId  shopId
     * @param goodsId goodsId
     * @param count   count
     */
    private void addHolder(ObjectId shopId, ObjectId goodsId, Long count) {

        HolderBroken holder = new HolderBroken();

        holder.setCount(count);
        holder.setShopId(shopId);
        holder.setGoodsId(goodsId);
        holder.setOperation(Operation.INCRBY);

        holders.add(holder);

    }

}
