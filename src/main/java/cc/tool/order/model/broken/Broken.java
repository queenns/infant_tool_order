package cc.tool.order.model.broken;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-16
 */
public class Broken {

    /**
     * 购买个数
     */
    private Long count;

    /**
     * goodsId
     */
    private ObjectId goodsId;

    /**
     * 库存
     * shopId --> inventory
     */
    private Map<String, Long> inventoryInfos;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public ObjectId getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(ObjectId goodsId) {
        this.goodsId = goodsId;
    }

    public Map<String, Long> getInventoryInfos() {
        return inventoryInfos;
    }

    public void setInventoryInfos(Map<String, Long> inventoryInfos) {
        this.inventoryInfos = inventoryInfos;
    }

    @Override
    public String toString() {
        return "Broken{" +
                "count=" + count +
                ", goodsId=" + goodsId +
                ", inventoryInfos=" + inventoryInfos +
                '}';
    }

}