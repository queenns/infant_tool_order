package cc.tool.order.util;

/**
 * Created by lxj on 16-11-9
 */
public class WechatMediaUtil {

    public static String findMediaUrl(String basicsAccessToken,String mediaId) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(OrderConfigureUtil.getValue("mediaCommonUrl"));

        stringBuffer.append(OrderConfigureUtil.getValue("accessTokenKey")).append(OrderConfigureUtil.getValue("connectKeyValCode")).append(basicsAccessToken);

        stringBuffer.append(OrderConfigureUtil.getValue("connectParamCode"));

        stringBuffer.append(OrderConfigureUtil.getValue("mediaKey")).append(OrderConfigureUtil.getValue("connectKeyValCode")).append(mediaId);

        return stringBuffer.toString();

    }

}
