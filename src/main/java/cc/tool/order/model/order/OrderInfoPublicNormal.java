package cc.tool.order.model.order;

/**
 * Created by lxj on 18-4-25
 * <p>
 * 常规订单参数
 */
public class OrderInfoPublicNormal extends OrderInfo {

    private String distributionId;

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    @Override
    public String toString() {
        return "OrderInfoPublicNormal{" +
                "distributionId='" + distributionId + '\'' +
                "} " + super.toString();
    }

}
