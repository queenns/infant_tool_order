package cc.tool.order.model;

/**
 * Created by lxj on 18-5-21
 */
public enum Fail {

    ERROR_FAIL("错误"),
    LOCK_FAIL("锁失败"),
    INIT_FAIL("初始失败"),
    ORDER_FAIL_EXPIRE("过期"),
    ORDER_FAIL_PAYMENT("已支付"),
    ORDER_FAIL_BROKEN("拆分失败"),
    ORDER_FAIL_MODIFY("订单已经修改信息"),
    ORDER_FAIL_STATUS("订单状态错误"),
    ORDER_FAIL_PACKING("订单已打包"),
    ORDER_FAIL_LOMS_PUSH("已推送云仓"),
    MEMBER_FAIL_MARKER("非会员"),
    MEMBER_FAIL_BALANCE("余额不足"),
    INTEGRAL_FAIL_BALANCE("积分不足"),
    HOLDER_FAIL_ZERO("库存为0"),
    HOLDER_FAIL_ERROR("未知的错误"),
    HOLDER_FAIL_UNSAFE("并发修改失败"),
    HOLDER_FAIL_GREATER("修改值大于剩余值"),
    EXPIRE_FAIL_CLOSE_PING("Ping订单关闭交易失败"),
    EXPIRE_FAIL_CLOSE_PING_PAYMENT("Ping订单关闭交易时已支付"),
    EXPIRE_FAIL_CLOSE_PING_REVERSE("Ping订单关闭交易错误"),
    EXPIRE_FAIL_CLOSE_WECHAT("微信订单关闭交易失败"),
    EXPIRE_FAIL_ORDER_LOCK("订单过期lock失败"),
    PAYMENT_FAIL_PING("PING++支付凭证失败"),
    PAYMENT_FAIL_UNIFIED("微信支付统一下单失败"),
    CONFIRM_FAIL_PING("订单支付确定失败"),
    REFUND_FAIL_PING("PING++退款通知错误"),
    REFUND_FAIL_PACKING("已打包"),
    REFUND_FAIL_STATUS("订单状态错误");

    String value = null;

    Fail(String value) {

        this.value = value;

    }

}
