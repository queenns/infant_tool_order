package cc.tool.order.action;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.util.XStreamUtil;
import cc.tool.order.constant.EncodingInfo;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.CloseUniFiedOrder;
import cc.tool.order.util.*;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-6-7
 */
public class CloseOrderWechatAction extends CloseOrderAction {

    public CloseOrderWechatAction(Order order) {
        super(order);
    }

    static Map<ChannelFromMark, String> appIds = new HashMap<ChannelFromMark, String>(4) {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("appIdVal"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("appIdValDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("appIdValQzgc"));

    }};

    @Override
    public MakeInfo action() {

        MakeInfo makeInfo = new MakeInfo();

        CloseUniFiedOrder closeOrder = new CloseUniFiedOrder();

        closeOrder.setOutTradeNo(order.getId().toString());

        closeOrder.setAppId(appIds.get(order.getChannelFromMark()));

        Map<String, String> signs = WechatCoreUtil.createClosePaySignMap(closeOrder);

        String link = WechatCoreUtil.createLink(signs, true);

        String sign = Md5Encrypt.md5ByUTF8(link).toUpperCase();

        closeOrder.setSign(sign);

        String xml = XStreamUtil.modelToXml(closeOrder, EncodingInfo.XML_UTF8_HEAD, new HashMap<Class, String>());

        Map<String, Object> results;

        try {

            String accessXml = AccessInternet.httpAccessXml(OrderConfigureUtil.getValue("closeOrderWechatUrl"), xml);

            String accessXmlByUtf8 = new String(accessXml.getBytes("ISO-8859-1"), EncodingInfo.UTF8);

            results = Dom4jUtil.findMapByText(accessXmlByUtf8);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_WECHAT.toString());

            return makeInfo;

        }

        logger.info("close order wechat orderId[{}] results[{}]", order.getId(), results);

        if (!CollectionUtils.isEmpty(results) && results.get("result_code").toString().equals("SUCCESS")) {

            makeInfo.setSuccess(true);

            return makeInfo;

        } else {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_WECHAT.toString());

            return makeInfo;

        }

    }

}
