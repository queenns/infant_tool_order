package cc.tool.order.action;

import cc.admore.infant.dao.entity.enums.OperateResult;
import cc.admore.infant.model.holder.HolderMaster;
import cc.admore.infant.model.holder.Operation;
import cc.tool.order.operation.OperationManager;
import com.yunrang.libra.handle.GoodsRedisHandle;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Created by lxj on 18-5-28
 */
public class HolderMasterAction extends HolderAction<HolderMaster> {

    private Map<ObjectId, Long> countInfos;

    public HolderMasterAction(Map<ObjectId, Long> countInfos, OperationManager operationManager) {

        super(operationManager);

        this.countInfos = countInfos;

    }

    @Override
    protected void holder() {

        GoodsRedisHandle goodsRedisHandle = operationManager.goodsRedisHandle;

        for (ObjectId goodsId : countInfos.keySet()) {

            Long count = countInfos.get(goodsId);

            OperateResult operateResult = goodsRedisHandle.decrbyGoodsInventory(goodsId.toString(), count);

            if (OperateResult.SUCCESS.equals(operateResult)) {

                addHolder(goodsId, count);

            } else {

                holderFail(operateResult);

                return;

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
    public void fail() {

        if (CollectionUtils.isEmpty(holders)) return;

        for (HolderMaster holderMaster : holders) {

            operationManager.holderClientHandle.sendHolderMasterQueue(holderMaster);

            logger.info("holder master fail send queue {}", holderMaster.toString());

        }

    }

    /**
     * 添加持有成功记录
     *
     * @param goodsId goodsId
     * @param count   count
     */
    private void addHolder(ObjectId goodsId, Long count) {

        HolderMaster holder = new HolderMaster();

        holder.setCount(count);
        holder.setGoodsId(goodsId);
        holder.setOperation(Operation.INCRBY);

        holders.add(holder);

    }

}