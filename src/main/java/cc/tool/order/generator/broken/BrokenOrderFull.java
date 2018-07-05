package cc.tool.order.generator.broken;

import cc.tool.order.bucket.broken.BrokenBucket;
import cc.tool.order.exception.CreateException;
import cc.tool.order.model.broken.Broken;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.util.OrderConfigureUtil;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created by lxj on 18-5-16
 * <p>
 * 拆分信息生成器
 * <p>
 * 单商品单店铺拆分
 */
class BrokenOrderFull extends BrokenGenerator {

    private static String masterId = OrderConfigureUtil.getValue("masterShopId");

    BrokenOrderFull(BrokenBucket brokenBucket) {
        super(brokenBucket);
    }

    /**
     * 生成拆分信息
     *
     * @return {@link BrokenGenerator#filters}
     * @throws CreateException none
     */
    @Override
    public Map<ObjectId, BrokenInfo> create() throws CreateException {

        recursion();

        logger.info("brokenInfos : {}", brokenInfos.values().toString());

        return brokenInfos;

    }

    /**
     * 递归拆分
     */
    private void recursion() {

        if (CollectionUtils.isEmpty(filters)) return;

        List<String> sortRanks = sortRank();

        List<String> sorts = sortDress(sortRanks);

        String shopId = confirm(sorts);

        logger.info("confirm shopId : {}", shopId);

        Iterator<ObjectId> iterator = filters.keySet().iterator();

        while (iterator.hasNext()) {

            ObjectId goodsId = iterator.next();

            Broken broken = filters.get(goodsId);

            if (!broken.getInventoryInfos().keySet().contains(shopId)) continue;

            ObjectId brokenInfoId = new ObjectId(shopId);

            BrokenInfo brokenInfo = ObjectUtils.isEmpty(brokenInfos.get(brokenInfoId)) ? new BrokenInfo() : brokenInfos.get(brokenInfoId);

            brokenInfo.setShopId(brokenInfoId);

            brokenInfo.getCountInfos().put(goodsId, broken.getCount());

            brokenInfos.put(brokenInfoId, brokenInfo);

            iterator.remove();

        }

        recursion();

    }

    private String confirm(List<String> shopIds) {

        if (shopIds.size() > 1) shopIds.remove(masterId);

        return shopIds.get(new Random().nextInt(shopIds.size()));

    }

    /**
     * 过滤单商品拆分单店的数据
     * 1.单商品购买数量小于店铺的库存最大值既满足
     * 2.过滤掉库存为null或者库存小于购买数量的店铺
     * 3.过滤后的Broken设置到filters
     */
    @Override
    protected void filter() {

        Map<ObjectId, Broken> brokens = brokenBucket.getBrokens();

        for (Broken broken : brokens.values()) {

            Long count = broken.getCount();

            Map<String, Long> inventoryInfos = broken.getInventoryInfos();

            Long max = Collections.max(inventoryInfos.values());

            if (count > max) continue;

            Map<String, Long> filterInventoryInfos = new HashMap<String, Long>();

            for (String shopId : inventoryInfos.keySet()) {

                Long inventory = inventoryInfos.get(shopId);

                if (ObjectUtils.isEmpty(inventory) || inventory < count) continue;

                filterInventoryInfos.put(shopId, inventory);

            }

            shopIds.addAll(filterInventoryInfos.keySet());

            Broken brokenFull = new Broken();

            brokenFull.setCount(count);
            brokenFull.setGoodsId(broken.getGoodsId());
            brokenFull.setInventoryInfos(filterInventoryInfos);

            filters.put(broken.getGoodsId(), brokenFull);

        }

        logger.info("filters : {}", Arrays.toString(filters.values().toArray()));

        findDress();

    }

    /**
     * 将所有Broken的shopId按照出现次数排序
     * 不同的商品合并到一个店铺(店铺出现次数越多，多个商品合并到一个店铺优先级越高)
     * 根据最高优先级店铺拆分完成后，还有Broken未拆分时重新排序拆分，直到所有Broken拆分完成
     *
     * @return sort shopIds
     */
    private List<String> sortRank() {

        List<String> shopIds = new ArrayList<String>();

        if (CollectionUtils.isEmpty(filters)) return shopIds;

        for (Broken broken : filters.values()) {

            shopIds.addAll(broken.getInventoryInfos().keySet());

        }

        Map<String, Integer> countInfos = new HashMap<String, Integer>();

        for (String shopId : shopIds) {

            Integer count = countInfos.get(shopId);

            countInfos.put(shopId, ObjectUtils.isEmpty(count) ? 0 : count + 1);

        }

        shopIds.clear();

        Integer max = Collections.max(countInfos.values());

        for (String shopId : countInfos.keySet()) {

            if (max.equals(countInfos.get(shopId))) shopIds.add(shopId);

        }

        logger.info("sortRank : {}", Arrays.toString(shopIds.toArray()));

        return shopIds;

    }

}
