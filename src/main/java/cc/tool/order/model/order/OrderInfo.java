package cc.tool.order.model.order;

import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by lxj on 18-4-25
 * <p>
 * 订单参数
 */
public class OrderInfo extends Info {

    /**
     * 公众号openId
     */
    private String openId;

    /**
     * 购买商品信息
     */
    private Map<ObjectId, Long> countInfos;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Map<ObjectId, Long> getCountInfos() {
        return countInfos;
    }

    public void setCountInfos(Map<ObjectId, Long> countInfos) {
        this.countInfos = countInfos;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "openId='" + openId + '\'' +
                ", countInfos=" + countInfos +
                "} " + super.toString();
    }

}
