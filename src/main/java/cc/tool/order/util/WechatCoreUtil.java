package cc.tool.order.util;

import cc.tool.order.model.payment.CloseUniFiedOrder;
import cc.tool.order.model.payment.UniFiedOrderInfo;
import cc.tool.order.model.payment.UniFiedOrderResult;

import java.util.*;

/**
 * Created by lxj on 16-10-10
 */
public class WechatCoreUtil {

    /**
     * 生成随机字符串
     *
     * @return 随机字符串
     */
    public static String createMd5Random() {
        return Md5Encrypt.md5ByUTF8(String.valueOf(new Random().nextInt(10000)));
    }

    /**
     * 转化微信支付参数成Map
     *
     * @param uniFiedOrderInfo UniFiedOrderInfo
     * @return Map
     */
    public static Map<String, String> createPaymentSigns(UniFiedOrderInfo uniFiedOrderInfo) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("body", uniFiedOrderInfo.getBody());
        map.put("appid", uniFiedOrderInfo.getAppId());
        map.put("mch_id", uniFiedOrderInfo.getMchId());
        map.put("openid", uniFiedOrderInfo.getOpenId());
        map.put("fee_type", uniFiedOrderInfo.getFeeType());
        map.put("total_fee", uniFiedOrderInfo.getTotalFee());
        map.put("nonce_str", uniFiedOrderInfo.getNonceStr());
        map.put("notify_url", uniFiedOrderInfo.getNotifyUrl());
        map.put("trade_type", uniFiedOrderInfo.getTradeType());
        map.put("device_info", uniFiedOrderInfo.getDeviceInfo());
        map.put("out_trade_no", uniFiedOrderInfo.getOutTradeNo());
        map.put("spbill_create_ip", uniFiedOrderInfo.getSpbillCreateIp());

        return map;

    }

    public static UniFiedOrderResult transformUniFiedOrderResult(Map uniFiedOrderInfoAccessMap) {

        UniFiedOrderResult uniFiedOrderResult = new UniFiedOrderResult();

        uniFiedOrderResult.setSign((String) uniFiedOrderInfoAccessMap.get("sign"));
        uniFiedOrderResult.setAppId((String) uniFiedOrderInfoAccessMap.get("appid"));
        uniFiedOrderResult.setMchId((String) uniFiedOrderInfoAccessMap.get("mch_id"));
        uniFiedOrderResult.setNonceStr((String) uniFiedOrderInfoAccessMap.get("nonce_str"));
        uniFiedOrderResult.setPrepayId((String) uniFiedOrderInfoAccessMap.get("prepay_id"));
        uniFiedOrderResult.setTradeType((String) uniFiedOrderInfoAccessMap.get("trade_type"));
        uniFiedOrderResult.setReturnMsg((String) uniFiedOrderInfoAccessMap.get("return_msg"));
        uniFiedOrderResult.setResultCode((String) uniFiedOrderInfoAccessMap.get("result_code"));
        uniFiedOrderResult.setReturnCode((String) uniFiedOrderInfoAccessMap.get("return_code"));
        uniFiedOrderResult.setDeviceInfo((String) uniFiedOrderInfoAccessMap.get("device_info"));
        uniFiedOrderResult.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000).substring(0, 10));

        return uniFiedOrderResult;


    }

    public static Map<String, String> createClosePaySignMap(CloseUniFiedOrder closeUniFiedOrder) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("appid", closeUniFiedOrder.getAppId());
        map.put("mch_id", closeUniFiedOrder.getMchId());
        map.put("nonce_str", closeUniFiedOrder.getNonceStr());
        map.put("out_trade_no", closeUniFiedOrder.getOutTradeNo());

        return map;

    }

    /**
     * 签名Map
     *
     * @param uniFiedOrderResult uniFiedOrderResult
     * @return
     */
    public static Map<String, String> createPaymentSigns(UniFiedOrderResult uniFiedOrderResult) {

        Map<String, String> map = new HashMap<>();

        map.put("appId", uniFiedOrderResult.getAppId());
        map.put("timeStamp", uniFiedOrderResult.getTimeStamp());
        map.put("nonceStr", uniFiedOrderResult.getNonceStr());
        map.put("package", "prepay_id=" + uniFiedOrderResult.getPrepayId());
        map.put("signType", "MD5");

        return map;

    }

    public static Map<String, String> createJsApiTicketSignMap(String url, String jsApiTicket) {

        Map<String, String> map = new HashMap<>();

        map.put("noncestr", WechatCoreUtil.createMd5Random());
        map.put("jsapi_ticket", jsApiTicket);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000).substring(0, 10));
        map.put("url", url);

        return map;

    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLink(Map<String, String> params, Boolean isConnectionKey) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        if (isConnectionKey)
            return prestr + "&key=" + OrderConfigureUtil.getValue("key");
        else
            return prestr;

    }

}
