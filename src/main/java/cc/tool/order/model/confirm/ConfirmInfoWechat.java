package cc.tool.order.model.confirm;

import java.util.Map;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmInfoWechat extends ConfirmInfo {

    private Map<String, Object> wechat;

    public Map<String, Object> getWechat() {
        return wechat;
    }

    public void setWechat(Map<String, Object> wechat) {
        this.wechat = wechat;
    }

    @Override
    public String toString() {
        return "ConfirmInfoWechat{" +
                "wechat=" + wechat +
                "} " + super.toString();
    }

}
