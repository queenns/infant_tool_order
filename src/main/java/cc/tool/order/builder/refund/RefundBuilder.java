package cc.tool.order.builder.refund;

import cc.admore.infant.dao.entity.Image;
import cc.admore.infant.dao.entity.ImageLink;
import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.follow.enums.FollowStatus;
import cc.admore.infant.model.follow.reference.FollowGoodsInfo;
import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.reference.DistributionOrderInfo;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.builder.AbstractBuilder;
import cc.tool.order.generator.refund.RefundGenerator;
import cc.tool.order.util.AccessInternet;
import cc.tool.order.util.NumberUtil;
import cc.tool.order.util.OrderConfigureUtil;
import cc.tool.order.util.WechatMediaUtil;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 * <p>
 * 退货记录记录构造器
 */
public abstract class RefundBuilder extends AbstractBuilder<FollowOrder> {

    protected Order order;

    protected FollowOrder refundInfo;

    protected RefundGenerator refundGenerator;

    public RefundBuilder(Order order, RefundGenerator refundGenerator) {

        this.order = order;

        this.refundGenerator = refundGenerator;

        initType();

        try {

            this.refundInfo = (FollowOrder) this.clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

        }

    }

    @Override
    public FollowOrder build() {

        refundInfo.setAmount(findAmount());
        refundInfo.setMasterId(order.getId());
        refundInfo.setApplyDate(new DateTime());
        refundInfo.setPayment(order.getPayment());
        refundInfo.setFromMark(order.getFromMark());
        refundInfo.setMasterStatus(order.getStatus());
        refundInfo.setFollowStatus(FollowStatus.APPLLING);
        refundInfo.setChannelFromMark(order.getChannelFromMark());
        refundInfo.setDistributionMarker(order.getDistributionMarker());
        refundInfo.setApplyReason(refundGenerator.refundInfo.getApplyReason());
        refundInfo.setApplyRemark(refundGenerator.refundInfo.getApplyRemark());

        DistributionOrderInfo distributionOrderInfo = order.getDistributionOrderInfo();

        if (!ObjectUtils.isEmpty(distributionOrderInfo)) {

            refundInfo.setSourceOpenId(distributionOrderInfo.getSourceOpenId());

        }

        List<String> serverIds = refundGenerator.refundInfo.getServerIds();

        if (!CollectionUtils.isEmpty(serverIds)) {

            List<ImageLink> imageLinks = new ArrayList<ImageLink>(serverIds.size());

            for (String mediaId : serverIds) {

                if (StringUtils.isEmpty(mediaId)) break;

                ImageLink imageLink = new ImageLink();

                String mediaUrl = WechatMediaUtil.findMediaUrl(refundGenerator.operationManager.wechatRedisHandle.findBasicsAccessToken(), mediaId);

                Image image = new Image();

                byte[] bytes = AccessInternet.httpAccess(mediaUrl, image);

                String fileId = refundGenerator.operationManager.imageDao.save(image, bytes);

                imageLink.setId(fileId);
                imageLink.setExName(".jpg");
                imageLink.setUrl(OrderConfigureUtil.getValue("mediaUrl") + fileId + ".jpg");

                imageLinks.add(imageLink);

            }

            refundInfo.setImageLinks(imageLinks);

        }

        return refundInfo;

    }

    protected Long findAmount() {

        Long amount = 0L;

        Map<ObjectId, Long> refundGoodsInfos = refundGenerator.refundInfo.getRefundGoodsInfos();

        for (ObjectId goodsId : refundGoodsInfos.keySet()) {

            Long count = refundGoodsInfos.get(goodsId);

            OrderGoodsInfo orderGoodsInfo = order.getOrderGoodsInfos().get(goodsId);

            amount = NumberUtil.add(amount, NumberUtil.multiply(orderGoodsInfo.getPrice(), count));

        }

        return amount;

    }

    protected FollowGoodsInfo buildDepotGoodsInfo(DepotOrder depotOrder, ObjectId goodsId, Long count) {

        ObjectId shopId = depotOrder.getShopId();

        ObjectId depotOrderId = depotOrder.getId();

        OrderGoodsInfo orderGoodsInfo = depotOrder.getOrderGoodsInfos().get(goodsId);

        FollowGoodsInfo followGoodsInfo = new FollowGoodsInfo();

        followGoodsInfo.setShopId(shopId);
        followGoodsInfo.setGoodsId(goodsId);
        followGoodsInfo.setRefundCount(count);
        followGoodsInfo.setDeportOrderId(depotOrderId);
        followGoodsInfo.setUnitPrice(orderGoodsInfo.getPrice());
        followGoodsInfo.setGoodsNo(orderGoodsInfo.getGoodsNo());
        followGoodsInfo.setGoodsName(orderGoodsInfo.getGoodsName());
        followGoodsInfo.setPurchasePrice(orderGoodsInfo.getPurchasePrice());
        followGoodsInfo.setDistributionMarker(orderGoodsInfo.getDistributionMarker());

        return followGoodsInfo;

    }

    protected FollowGoodsInfo buildGoodsInfo(ObjectId goodsId, Long count) {

        OrderGoodsInfo orderGoodsInfo = order.getOrderGoodsInfos().get(goodsId);

        FollowGoodsInfo followGoodsInfo = new FollowGoodsInfo();

        followGoodsInfo.setGoodsId(goodsId);
        followGoodsInfo.setRefundCount(count);
        followGoodsInfo.setUnitPrice(orderGoodsInfo.getPrice());
        followGoodsInfo.setGoodsNo(orderGoodsInfo.getGoodsNo());
        followGoodsInfo.setGoodsName(orderGoodsInfo.getGoodsName());
        followGoodsInfo.setPurchasePrice(orderGoodsInfo.getPurchasePrice());
        followGoodsInfo.setDistributionMarker(orderGoodsInfo.getDistributionMarker());

        return followGoodsInfo;

    }

}
