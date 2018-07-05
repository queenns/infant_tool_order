package cc.tool.order.bucket.order;

import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.util.NumberUtil;
import org.bson.types.ObjectId;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created by lxj on 18-5-4
 * <p>
 * 基础的信息处理
 */
public abstract class BaseBucket<P, G> extends OrderBucket<P, G> {

    BaseBucket(OrderGenerator<?, P, G> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    protected Long cacheAmount() {

        setAmount(0L);

        Map<ObjectId, OrderGoodsInfo> orderGoodsInfos = findOrderGoodsInfos();

        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfos.values()) {

            Long price = orderGoodsInfo.getPrice(), count = orderGoodsInfo.getCount();

            Long total = NumberUtil.multiply(price, count);

            setAmount(NumberUtil.add(getAmount(), total));

        }

        return getAmount();

    }

    @Override
    protected Long cachePostage() {

        Long postage = orderGenerator.operationManager.defaultPostage.calculate(this);

        setPostage(postage);

        return getPostage();

    }

    @Override
    protected Long cacheActualAmount() {

        Long actualAmount = NumberUtil.add(findAmount(), findPostage());

        setActualAmount(actualAmount);

        return getActualAmount();

    }

    @Override
    protected Dress cacheDress() {

        Dress dress = operationQueryDress();

        setDress(!ObjectUtils.isEmpty(dress) ? dress : nullDress);

        getDress().unsetId();

        return getDress();

    }

    @Override
    protected Customer cacheCustomer() {

        Customer customer = operationQueryCustomer();

        setCustomer(customer);

        return getCustomer();

    }

    protected abstract Dress operationQueryDress();

    protected abstract Customer operationQueryCustomer();

}
