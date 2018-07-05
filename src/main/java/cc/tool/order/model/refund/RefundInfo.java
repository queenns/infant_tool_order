package cc.tool.order.model.refund;

import cc.admore.infant.model.follow.enums.FollowType;
import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 */
public class RefundInfo extends Info {

    /**
     * orderId
     */
    private ObjectId orderId;

    /**
     * 退款类型
     */
    private FollowType followType;

    /**
     * 图片凭证id
     */
    private List<String> serverIds;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 申请说明
     */
    private String applyRemark;

    /**
     * 退货信息
     */
    private Map<ObjectId, Long> refundGoodsInfos;

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public FollowType getFollowType() {
        return followType;
    }

    public void setFollowType(FollowType followType) {
        this.followType = followType;
    }

    public Map<ObjectId, Long> getRefundGoodsInfos() {
        return refundGoodsInfos;
    }

    public void setRefundGoodsInfos(Map<ObjectId, Long> refundGoodsInfos) {
        this.refundGoodsInfos = refundGoodsInfos;
    }

    @Override
    public String toString() {
        return "RefundInfo{" +
                "orderId=" + orderId +
                ", followType=" + followType +
                ", serverIds=" + serverIds +
                ", applyReason='" + applyReason + '\'' +
                ", applyRemark='" + applyRemark + '\'' +
                ", refundGoodsInfos=" + refundGoodsInfos +
                "} " + super.toString();
    }

}