package cc.tool.order.model.payment;

import cc.tool.order.util.OrderConfigureUtil;
import cc.tool.order.util.WechatCoreUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lxj on 17-9-1
 */
@XStreamAlias("xml")
public class CloseUniFiedOrder {

    /**
     * 公众账号ID
     */
    @XStreamAlias("appid")
    private String appId;

    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId = OrderConfigureUtil.getValue("mchIdVal");

    /**
     * 商户订单号
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 随机字符串
     */
    @XStreamAlias("nonce_str")
    private String nonceStr = WechatCoreUtil.createMd5Random();

    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
