package cc.tool.order.model.broken;

import cc.tool.order.model.Info;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-5-16
 */
public class BrokenInfo extends Info {

    private ObjectId shopId;

    private Map<ObjectId, Long> countInfos = new HashMap<ObjectId, Long>();

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public Map<ObjectId, Long> getCountInfos() {
        return countInfos;
    }

    public void setCountInfos(Map<ObjectId, Long> countInfos) {
        this.countInfos = countInfos;
    }

    @Override
    public String toString() {
        return "BrokenInfo{" +
                "shopId=" + shopId +
                ", countInfos=" + countInfos +
                "}";
    }

}
