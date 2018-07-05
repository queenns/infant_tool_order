package cc.tool.order.generator.broken;

import cc.admore.infant.model.order.Order;
import cc.tool.order.OrderBrokenTool;
import cc.tool.order.bucket.broken.BrokenBucket;
import cc.tool.order.exception.BrokenException;
import cc.tool.order.exception.CreateException;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by lxj on 18-5-16
 * <p>
 * 拆分信息生成器
 */
public class BrokenOrder extends BrokenGenerator implements OrderBrokenTool {

    private BrokenOrderFull generatorBrokenFull;

    private BrokenOrderLack generatorBrokenLack;

    public BrokenOrder(Order order, OperationManager operationManager) {

        super(new BrokenBucket(order, operationManager));

        this.generatorBrokenFull = new BrokenOrderFull(brokenBucket);

        this.generatorBrokenLack = new BrokenOrderLack(brokenBucket);

    }


    @Override
    public Map<ObjectId, BrokenInfo> create() throws CreateException {

        filter();

        Map<ObjectId, BrokenInfo> brokenInfosFull = generatorBrokenFull.create();

        Map<ObjectId, BrokenInfo> brokenInfosLack = generatorBrokenLack.create();

        merge(brokenInfosFull, brokenInfosLack);

        logger.info("brokenInfos : {}", brokenInfos.values().toString());

        return brokenInfos;

    }

    private void merge(Map<ObjectId, BrokenInfo> brokenInfosFull, Map<ObjectId, BrokenInfo> brokenInfosLack) {

        brokenInfos.putAll(brokenInfosFull);

        for (ObjectId shopId : brokenInfosLack.keySet()) {

            BrokenInfo brokenInfo = brokenInfos.get(shopId);

            BrokenInfo brokenInfoLack = brokenInfosLack.get(shopId);

            if (ObjectUtils.isEmpty(brokenInfo)) {

                brokenInfos.put(shopId, brokenInfoLack);

            } else {

                brokenInfoLack.getCountInfos().putAll(brokenInfo.getCountInfos());

                brokenInfos.put(shopId, brokenInfoLack);

            }

        }

    }

    @Override
    protected void filter() throws BrokenException {

        logger.info("brokens : {}", Arrays.toString(brokenBucket.getBrokens().values().toArray()));

        generatorBrokenFull.filter();

        generatorBrokenLack.filter();

        int fullSize = generatorBrokenFull.filters.size();

        int lackSize = generatorBrokenLack.filters.size();

        if (brokenBucket.getBrokens().size() == (fullSize + lackSize)) return;

        throw new BrokenException("filter full lack error");

    }

}
