package cc.tool.order.util;

import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * Created by lxj on 18-4-27
 */
public abstract class NumberUtil {

    public static Long format(Double number) {

        if (number == null) return 0L;

        BigDecimal bigDecimalX = new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimalY = new BigDecimal(100);

        return bigDecimalX.multiply(bigDecimalY).longValue();

    }

    public static Double format(Long number) {

        if (number == null || number == 0) return 0D;

        BigDecimal bigDecimalX = new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimalY = new BigDecimal(100);

        return bigDecimalX.divide(bigDecimalY, 2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * x y 加
     *
     * @param x x
     * @param y y
     * @return x+y
     */
    public static Long add(Long x, Long y) {

        x = ObjectUtils.isEmpty(x) ? 0 : x;

        y = ObjectUtils.isEmpty(y) ? 0 : y;

        BigDecimal bigDecimalX = new BigDecimal(x).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimalY = new BigDecimal(y).setScale(2, BigDecimal.ROUND_HALF_UP);

        return bigDecimalX.add(bigDecimalY).setScale(2, RoundingMode.HALF_UP).longValue();

    }

    /**
     * x y 减
     *
     * @param x x
     * @param y y
     * @return x - y
     */
    public static Long subtract(Long x, Long y) {

        x = ObjectUtils.isEmpty(x) ? 0 : x;

        y = ObjectUtils.isEmpty(y) ? 0 : y;

        BigDecimal bigDecimalX = new BigDecimal(x).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimalY = new BigDecimal(y).setScale(2, BigDecimal.ROUND_HALF_UP);

        return bigDecimalX.subtract(bigDecimalY).setScale(2, RoundingMode.HALF_UP).longValue();

    }

    /**
     * x y 乘
     *
     * @param x x
     * @param y y
     * @return x*y
     */
    public static Long multiply(Long x, Long y) {

        x = ObjectUtils.isEmpty(x) ? 0 : x;

        y = ObjectUtils.isEmpty(y) ? 0 : y;

        BigDecimal bigDecimalX = new BigDecimal(x).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimalY = new BigDecimal(y).setScale(2, BigDecimal.ROUND_HALF_UP);

        return bigDecimalX.multiply(bigDecimalY).setScale(2, RoundingMode.HALF_UP).longValue();

    }

    /**
     * Collection<Long> 加
     *
     * @param values values
     * @return v1+v2+v3+v4...
     */
    public static Long add(Collection<Long> values) {

        Long count = 0L;

        for (Long value : values) {

            value = ObjectUtils.isEmpty(value) ? 0 : value;

            count = add(count, value);

        }

        return count;

    }

}
