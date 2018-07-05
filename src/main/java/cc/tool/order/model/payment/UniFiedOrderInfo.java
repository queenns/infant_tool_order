package cc.tool.order.model.payment;

import cc.tool.order.util.OrderConfigureUtil;
import cc.tool.order.util.WechatCoreUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lxj on 16-10-9
 */
@XStreamAlias("xml")
public class UniFiedOrderInfo {

    /**
     * appId
     */
    @XStreamAlias("appid")
    private String appId;

    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId = OrderConfigureUtil.getValue("mchIdVal");

    /**
     * 设备号
     */
    @XStreamAlias("device_info")
    private String deviceInfo = OrderConfigureUtil.getValue("deviceInfoVal");

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

    /**
     * 商品描述
     */
    @XStreamAlias("body")
    private String body;

    /**
     * 商户订单号
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 货币类型
     */
    @XStreamAlias("fee_type")
    private String feeType = OrderConfigureUtil.getValue("feeType");

    /**
     * 总金额
     */
    @XStreamAlias("total_fee")
    private String totalFee;

    /**
     * 终端IP
     */
    @XStreamAlias("spbill_create_ip")
    private String spbillCreateIp = OrderConfigureUtil.getValue("spbillCreateIp");

    /**
     * 通知地址
     */
    @XStreamAlias("notify_url")
    private String notifyUrl = OrderConfigureUtil.getValue("notifyUrl");

    /**
     * 交易类型
     */
    @XStreamAlias("trade_type")
    private String tradeType = OrderConfigureUtil.getValue("tradeType");

    /**
     * 用户标识
     */
    @XStreamAlias("openid")
    private String openId;


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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
