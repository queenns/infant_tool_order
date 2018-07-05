package cc.tool.order.operation;

import cc.admore.infant.dao.customer.MiniCustomerDao;
import cc.admore.infant.dao.customer.PublicCustomerDao;
import cc.admore.infant.dao.dress.DefineDressDao;
import cc.admore.infant.dao.dress.ShareDressDao;

import cc.admore.infant.dao.follow.FollowOrderDao;
import cc.admore.infant.dao.kuaidiCompany.ExpressCompanyDao;
import cc.admore.infant.dao.mongodb.ImageDao;
import cc.admore.infant.dao.mongodb.UserDao;
import cc.admore.infant.dao.mongodb.finance.MemberPointsDao;
import cc.admore.infant.dao.order.MasterOrderDao;
import cc.admore.infant.dao.order.MinceOrderDao;
import cc.admore.infant.dao.order.MiniOrderDao;
import cc.admore.infant.dao.order.NormalOrderDao;
import cc.admore.infant.dao.payment.PingChargeDao;
import cc.admore.infant.service.mongodb.GoodsService;
import cc.admore.infant.service.mongodb.IntegralGoodsGroupService;
import cc.tool.order.strategy.postage.DefaultPostage;
import cn.com.logclient.handle.HolderClientHandle;
import cn.com.logclient.handle.KuaiDiClientHandle;
import cn.com.logclient.handle.OrderClientHandle;
import cn.com.logclient.handle.PaymentClientHandle;
import com.yunrang.libra.handle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lxj on 18-5-2
 */
@Component
public class OperationManager {

    @Autowired
    public PingChargeDao pingChargeDao;

    @Autowired
    public MiniOrderDao miniOrderDao;

    @Autowired
    public NormalOrderDao normalOrderDao;

    @Autowired
    public LockRedisHandle lockRedisHandle;

    @Autowired
    public MasterOrderDao masterOrderDao;

    @Autowired
    public MinceOrderDao minceOrderDao;

    @Autowired
    public DefaultPostage defaultPostage;

    @Autowired
    public ShareDressDao shareDressDao;

    @Autowired
    public DefineDressDao defineDressDao;

    @Autowired
    public MiniCustomerDao miniCustomerDao;

    @Autowired
    public PublicCustomerDao publicCustomerDao;

    @Autowired
    public ShopRedisHandle shopRedisHandle;

    @Autowired
    public GroupRedisHandle groupRedisHandle;

    @Autowired
    public GoodsRedisHandle goodsRedisHandle;

    @Autowired
    public GoodsService goodsService;

    @Autowired
    public IntegralGoodsGroupService integralGoodsGroupService;

    @Autowired
    public MemberPointsDao memberPointsDao;

    @Autowired
    public PaymentClientHandle paymentClientHandle;

    @Autowired
    public HomePageRedisHandle homePageRedisHandle;

    @Autowired
    public HolderClientHandle holderClientHandle;

    @Autowired
    public OrderClientHandle orderClientHandle;

    @Autowired
    public UserDao userDao;

    @Autowired
    public FollowOrderDao followOrderDao;

    @Autowired
    public WechatRedisHandle wechatRedisHandle;

    @Autowired
    public ImageDao imageDao;

    @Autowired
    public KuaiDiClientHandle kuaiDiClientHandle;

    @Autowired
    public ExpressCompanyDao expressCompanyDao;

}
