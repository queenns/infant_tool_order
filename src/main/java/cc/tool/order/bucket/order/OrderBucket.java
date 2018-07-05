package cc.tool.order.bucket.order;

import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;

import cc.tool.order.bucket.Bucket;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.dress.NullDress;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 订单预信息
 */
public abstract class OrderBucket<P, G> extends Bucket implements OrderBucketCache<P, G> {

    /**
     * 默认地址
     */
    private Dress dress;

    /**
     * 邮费
     */
    private Long postage;

    /**
     * 商品总金额
     */
    private Long amount;

    /**
     * 订单实际金额
     */
    private Long actualAmount;

    /**
     * 用户信息
     */
    private Customer customer;

    /**
     * 商品编号集合
     */
    private Set<String> goodsNos;

    /**
     * 商品组
     */
    private Map<ObjectId, P> groups;

    /**
     * 商品
     */
    private Map<ObjectId, G> goodses;

    /**
     * 订单快照
     */
    private Map<ObjectId, OrderGoodsInfo> orderGoodsInfos;

    /**
     * 没有地址时引用，防止重复获取
     */
    protected static final NullDress nullDress = new NullDress();

    /**
     * 订单生成器
     */
    OrderGenerator<?, P, G> orderGenerator;

    OrderBucket(OrderGenerator<?, P, G> orderGenerator) {

        this.orderGenerator = orderGenerator;

    }

    protected abstract Dress cacheDress();

    @Override
    public Dress findDress() {

        Dress dress = ObjectUtils.isEmpty(getDress()) ? cacheDress() : getDress();

        return (dress instanceof NullDress) ? null : dress;

    }

    protected abstract Long cacheAmount();

    @Override
    public Long findAmount() {

        return ObjectUtils.isEmpty(amount) ? cacheAmount() : amount;

    }

    protected abstract Long cachePostage();

    @Override
    public Long findPostage() {

        return ObjectUtils.isEmpty(postage) ? cachePostage() : postage;

    }

    protected abstract Long cacheActualAmount();

    @Override
    public Long findActualAmount() {

        return ObjectUtils.isEmpty(actualAmount) ? cacheActualAmount() : actualAmount;

    }

    protected abstract Customer cacheCustomer();

    @Override
    public Customer findCustomer() {

        return ObjectUtils.isEmpty(customer) ? cacheCustomer() : customer;

    }

    protected abstract Set<String> cacheGoodsNos();

    @Override
    public Set<String> findGoodsNos() {

        return CollectionUtils.isEmpty(goodsNos) ? cacheGoodsNos() : goodsNos;

    }

    protected abstract Map<ObjectId, P> cacheGroups();

    @Override
    public Map<ObjectId, P> findGroups() {

        return CollectionUtils.isEmpty(groups) ? cacheGroups() : groups;

    }

    protected abstract Map<ObjectId, G> cacheGoodses();

    @Override
    public Map<ObjectId, G> findGoodses() {

        return CollectionUtils.isEmpty(goodses) ? cacheGoodses() : goodses;

    }

    protected abstract Map<ObjectId, OrderGoodsInfo> cacheOrderGoodsInfos();

    @Override
    public Map<ObjectId, OrderGoodsInfo> findOrderGoodsInfos() {

        return CollectionUtils.isEmpty(this.orderGoodsInfos) ? cacheOrderGoodsInfos() : orderGoodsInfos;

    }

    protected Dress getDress() {
        return dress;
    }

    protected void setDress(Dress dress) {
        this.dress = dress;
    }

    protected Long getAmount() {
        return amount;
    }

    protected void setAmount(Long amount) {
        this.amount = amount;
    }

    protected Long getPostage() {
        return postage;
    }

    protected void setPostage(Long postage) {
        this.postage = postage;
    }

    protected Long getActualAmount() {
        return actualAmount;
    }

    protected void setActualAmount(Long actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<String> getGoodsNos() {
        return goodsNos;
    }

    public void setGoodsNos(Set<String> goodsNos) {
        this.goodsNos = goodsNos;
    }

    protected Map<ObjectId, P> getGroups() {
        return groups;
    }

    protected void setGroups(Map<ObjectId, P> groups) {
        this.groups = groups;
    }

    protected Map<ObjectId, G> getGoodses() {
        return goodses;
    }

    protected void setGoodses(Map<ObjectId, G> goodses) {
        this.goodses = goodses;
    }

    protected Map<ObjectId, OrderGoodsInfo> getOrderGoodsInfos() {
        return orderGoodsInfos;
    }

    protected void setOrderGoodsInfos(Map<ObjectId, OrderGoodsInfo> orderGoodsInfos) {
        this.orderGoodsInfos = orderGoodsInfos;
    }

}
