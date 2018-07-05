package cc.tool.order.generator.broken;

import cc.tool.order.bucket.broken.BrokenBucket;
import cc.tool.order.exception.BrokenException;
import cc.tool.order.exception.CreateException;
import cc.tool.order.model.broken.Broken;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.util.NumberUtil;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created by lxj on 18-5-16
 * <p>
 * 拆分信息生成器
 * <p>
 * 单商品多店铺拆分
 */
class BrokenOrderLack extends BrokenGenerator {

    BrokenOrderLack(BrokenBucket brokenBucket) {
        super(brokenBucket);
    }

    /**
     * 生成拆分信息
     * 1.排序
     * 2.按照每个Broken开始生成拆分信息
     *
     * @return {@link BrokenGenerator#filters}
     * @throws CreateException 当商品购买数量拆分不完时抛出
     */
    @Override
    public Map<ObjectId, BrokenInfo> create() throws CreateException {

        sortRank();

        for (final Broken broken : filters.values()) {

            Long count = broken.getCount();

            Map<String, Long> inventoryInfos = broken.getInventoryInfos();

            for (String shopId : inventoryInfos.keySet()) {

                if (count == 0) break;

                ObjectId brokenInfoId = new ObjectId(shopId);

                BrokenInfo brokenInfo = ObjectUtils.isEmpty(brokenInfos.get(brokenInfoId)) ? new BrokenInfo() : brokenInfos.get(brokenInfoId);

                brokenInfo.setShopId(brokenInfoId);

                final Long inventory = inventoryInfos.get(shopId);

                if (count > inventory) {

                    brokenInfo.getCountInfos().put(broken.getGoodsId(), inventory);

                    count = count - inventory;

                } else {

                    brokenInfo.getCountInfos().put(broken.getGoodsId(), count);

                    count = 0L;

                }

                brokenInfos.put(brokenInfoId, brokenInfo);

            }

            if (count != 0)

                throw new BrokenException("lack broken inventory error,count[" + count + "]");

        }

        logger.info("brokenInfos : {}", brokenInfos.values().toString());

        return brokenInfos;

    }

    /**
     * 过滤单商品拆分多店的数据
     * 1.单商品购买数量>店铺的库存最大值既满足
     * 2.过滤掉库存null或者0的店铺
     * 3.过滤后的Broken设置到filters
     *
     * @throws BrokenException 1.满足单商品拆分多店铺，过滤后shopIds为空无法拆分抛出
     *                         2.满足单商品拆分多店铺，过滤后所有店铺库存和小于购买数量无法拆分抛出
     */
    @Override
    protected void filter() throws BrokenException {

        Map<ObjectId, Broken> brokens = brokenBucket.getBrokens();

        for (Broken broken : brokens.values()) {

            Long count = broken.getCount();

            Map<String, Long> inventoryInfos = broken.getInventoryInfos();

            Long max = Collections.max(inventoryInfos.values());

            if (count <= max) continue;

            Map<String, Long> filterInventoryInfos = new HashMap<String, Long>();

            for (String shopId : inventoryInfos.keySet()) {

                Long inventory = inventoryInfos.get(shopId);

                if (ObjectUtils.isEmpty(inventory) || inventory <= 0) continue;

                filterInventoryInfos.put(shopId, inventory);

            }

            if (CollectionUtils.isEmpty(filterInventoryInfos))

                throw new BrokenException("filter shopIds that is null," + broken.toString());

            if (count > NumberUtil.add(filterInventoryInfos.values()))

                throw new BrokenException("filter inventory < count not broken," + broken.toString());

            shopIds.addAll(filterInventoryInfos.keySet());

            Broken brokenLack = new Broken();

            brokenLack.setCount(count);
            brokenLack.setGoodsId(broken.getGoodsId());
            brokenLack.setInventoryInfos(filterInventoryInfos);

            filters.put(broken.getGoodsId(), brokenLack);

        }

        logger.info("filters : {}", Arrays.toString(filters.values().toArray()));

        findDress();

    }

    /**
     * 按照每一个Broken排序
     * 先按照库存大小排序
     * 当库存大小相同的店铺再次进行地址排序(发货就近原则)
     * 按照库存从大到小消耗，同一个商品拆分成多个订单的个数越少
     */
    private void sortRank() {

        if (CollectionUtils.isEmpty(filters)) return;

        for (Broken broken : filters.values()) {

            List<Map.Entry<String, Long>> sorts = new ArrayList<Map.Entry<String, Long>>(broken.getInventoryInfos().entrySet());

            Collections.sort(sorts, new Comparator<Map.Entry<String, Long>>() {

                @Override
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {

                    return o2.getValue().compareTo(o1.getValue());

                }

            });

            LinkedHashMap<Long, List<String>> groups = new LinkedHashMap<Long, List<String>>();

            for (Map.Entry<String, Long> countInfo : sorts) {

                Long inventory = countInfo.getValue();

                List<String> group = CollectionUtils.isEmpty(groups.get(inventory)) ? new ArrayList<String>() : groups.get(inventory);

                group.add(countInfo.getKey());

                groups.put(inventory, group);

            }

            LinkedHashMap<String, Long> inventoryInfos = new LinkedHashMap<String, Long>();

            for (Long inventory : groups.keySet()) {

                List<String> group = sortDress(groups.get(inventory));

                for (String shopId : group) {

                    inventoryInfos.put(shopId, inventory);

                }

            }

            broken.setInventoryInfos(inventoryInfos);

        }

        logger.info("sortRank : {}", filters.values().toString());

    }

}
