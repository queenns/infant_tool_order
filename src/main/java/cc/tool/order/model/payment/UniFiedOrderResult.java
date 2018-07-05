package cc.tool.order.model.payment;

/**
 * Created by lxj on 16-10-10
 */
public class UniFiedOrderResult {

    private String resultCode;

    private String sign;

    private String mchId;

    private String prepayId;

    private String returnMsg;

    private String appId;

    private String nonceStr;

    private String returnCode;

    private String deviceInfo;

    private String tradeType;

    private String timeStamp;

    private String paySign;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    @Override
    public String toString() {
        return "UniFiedOrderResult{" +
                "resultCode='" + resultCode + '\'' +
                ", sign='" + sign + '\'' +
                ", mchId='" + mchId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", appId='" + appId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", paySign='" + paySign + '\'' +
                '}';
    }

}
