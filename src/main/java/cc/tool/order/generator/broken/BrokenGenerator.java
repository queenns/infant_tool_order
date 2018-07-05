package cc.tool.order.generator.broken;

import cc.admore.infant.model.dress.Dress;
import cc.tool.order.bucket.broken.BrokenBucket;
import cc.tool.order.exception.BrokenException;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.broken.Broken;
import cc.tool.order.model.broken.BrokenInfo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created by lxj on 18-5-16
 * <p>
 * 拆分信息生成器
 */
public abstract class BrokenGenerator extends AbstractGenerator<Map<ObjectId, BrokenInfo>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 拆分订单预信息
     */
    BrokenBucket brokenBucket;

    /**
     * 店铺地址
     */
    private Map<String, List<String>> dresses;

    /**
     * 满足条件的店铺id
     */
    Set<String> shopIds = new HashSet<String>();

    /**
     * 过滤拆分信息
     */
    Map<ObjectId, Broken> filters = new HashMap<ObjectId, Broken>();

    /**
     * 生成结果
     */
    Map<ObjectId, BrokenInfo> brokenInfos = new HashMap<ObjectId, BrokenInfo>();

    /**
     * 地址等级分组缓存
     * 优先级
     * head：高(省市区匹配)
     * body：中(省市匹配)
     * foot：低(省匹配)
     * none：N(省市区都不匹配)
     */
    private Map<String, List<String>> groups = new HashMap<String, List<String>>();

    public BrokenGenerator(BrokenBucket brokenBucket) {

        this.brokenBucket = brokenBucket;

    }

    /**
     * 过滤满足条件的属性
     * Broken
     * 设置到{@link BrokenGenerator#filters}
     * shopIds
     * 设置到{@link BrokenGenerator#shopIds}
     * dresses
     * 设置到{@link BrokenGenerator#dresses}
     */
    protected abstract void filter() throws BrokenException;

    /**
     * 缓存满足条件的店铺地址
     */
    void findDress() {

        if (CollectionUtils.isEmpty(shopIds)) return;

        this.dresses = brokenBucket.operationManager.shopRedisHandle.pipelinedDresses(new ArrayList<String>(shopIds));

        logger.info("dresses : {}", this.dresses.toString());

    }

    /**
     * 店铺排序
     * 根据用户地址和店铺地址远近进行
     *
     * @param shopIds shopIds
     * @return sort shopIds
     */
    List<String> sortDress(List<String> shopIds) {

        if (shopIds.size() == 1) return shopIds;

        Dress dress = brokenBucket.getOrder().getDress();

        if (ObjectUtils.isEmpty(dress)) return shopIds;

        clear();

        String province = dress.getProvince(), city = dress.getCity(), county = dress.getCounty();

        for (String shopId : shopIds) {

            List<String> shopDress = dresses.get(shopId);

            String shopProvince = shopDress.get(0), shopCity = shopDress.get(1), shopCounty = shopDress.get(2);

            if (shopProvince.equals(province) && shopCity.equals(city) && shopCounty.equals(county)) {

                groups.get("head").add(shopId);

            } else if (shopProvince.equals(province) && shopCity.equals(city)) {

                groups.get("body").add(shopId);

            } else if (shopProvince.equals(province)) {

                groups.get("foot").add(shopId);

            } else {

                groups.get("none").add(shopId);

            }

        }

        return merge();

    }

    /**
     * 清除地址等级分组缓存
     */
    private void clear() {

        this.groups.clear();

        groups.put("head", new ArrayList<String>());

        groups.put("body", new ArrayList<String>());

        groups.put("foot", new ArrayList<String>());

        groups.put("none", new ArrayList<String>());

    }

    /**
     * 按照优先级合并shopId
     *
     * @return sort shopIds
     */
    private List<String> merge() {

        List<String> sorts = new ArrayList<String>();

        sorts.addAll(groups.get("head"));

        sorts.addAll(groups.get("body"));

        sorts.addAll(groups.get("foot"));

        sorts.addAll(groups.get("none"));

        logger.info("sortDress : {}", Arrays.toString(shopIds.toArray()));

        return sorts;

    }

}
