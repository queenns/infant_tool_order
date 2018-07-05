package cc.tool.order.builder.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentWechat;
import cc.admore.infant.util.XStreamUtil;
import cc.tool.order.constant.EncodingInfo;
import cc.tool.order.exception.PaymentException;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.model.payment.UniFiedOrderInfo;
import cc.tool.order.model.payment.UniFiedOrderResult;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-5-21
 */
public class BuilderPaymentWechat extends BuilderPaymentParty<PaymentWechat> {

    private Map<String, String> signs;

    private String link;

    private String sign;

    public BuilderPaymentWechat(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {
        super(order, paymentInfo, operationManager);
    }

    @Override
    protected PaymentWechat payment() {

        payment.setPaymentMarker(false);
        payment.setPayMark(PayMark.WX);

        return payment;

    }

    @Override
    public MakeInfo build() {

        try {

            UniFiedOrderInfo uniFiedOrderInfo = new UniFiedOrderInfo();

            uniFiedOrderInfo.setOpenId(paymentInfo.getOpenId());
            uniFiedOrderInfo.setBody(bodys.get(order.getChannelFromMark()));
            uniFiedOrderInfo.setTotalFee(order.getActualAmount().toString());
            uniFiedOrderInfo.setAppId(appIds.get(order.getChannelFromMark()));
            uniFiedOrderInfo.setOutTradeNo(paymentInfo.getOrderId().toString());

            signs = WechatCoreUtil.createPaymentSigns(uniFiedOrderInfo);
            link = WechatCoreUtil.createLink(signs, true);
            sign = Md5Encrypt.md5ByUTF8(link).toUpperCase();
            uniFiedOrderInfo.setSign(sign);

            String xml = XStreamUtil.modelToXml(uniFiedOrderInfo, EncodingInfo.XML_UTF8_HEAD, new HashMap<Class, String>());
            String accessXml = AccessInternet.httpAccessXml(OrderConfigureUtil.getValue("uniFiedOrderInfoUrl"), xml);
            accessXml = new String(accessXml.getBytes("ISO-8859-1"), EncodingInfo.UTF8);
            Map accesses = Dom4jUtil.findMapByText(accessXml);

            logger.info("统一下单:{}", accesses.toString());

            if (!"SUCCESS".equals(accesses.get("result_code"))) throw new PaymentException("统一下单异常");

            UniFiedOrderResult uniFiedOrderResult = WechatCoreUtil.transformUniFiedOrderResult(accesses);

            signs = WechatCoreUtil.createPaymentSigns(uniFiedOrderResult);
            link = WechatCoreUtil.createLink(signs, true);
            sign = Md5Encrypt.md5ByUTF8(link).toUpperCase();

            uniFiedOrderResult.setPaySign(sign);

            super.build();

            makeInfo.setSuccess(true);
            makeInfo.setObj(uniFiedOrderResult);
            makeInfo.setMsg(PayMark.WX.toString());

            return makeInfo;

        } catch (UnsupportedEncodingException | PaymentException e) {

            e.printStackTrace();

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.PAYMENT_FAIL_UNIFIED.toString());

            return makeInfo;

        }

    }

    private static final Map<ChannelFromMark, String> appIds = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("appIdVal"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("appIdValDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("appIdValQzgc"));

    }};

}
