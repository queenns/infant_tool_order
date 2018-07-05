package cc.tool.order.builder.voucher;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payInfo.PayVoucher;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;

/**
 * Created by lxj on 18-5-24
 */
public class BuilderVoucherFree extends BuilderVoucher {

    public BuilderVoucherFree(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    public BuilderVoucherFree(Order order, ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(order, confirmInfo, operationManager);
    }

    @Override
    protected PayVoucher buildType(PayVoucher voucher) {

        return voucher;

    }

    @Override
    protected PayVoucher buildGoods(Order order, ObjectId goodsId, Long count, PayVoucher voucher) {

        voucher.setGoodsId(goodsId.toString());

        voucher.setUnitPrice(0);

        voucher.setPurchaseCount(count.intValue());

        voucher.setTotalFee(0);

        return voucher;

    }

}
