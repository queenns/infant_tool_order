package cc.tool.order.validate;

import cc.tool.order.model.Info;

/**
 * Created by lxj on 18-5-7
 */
public interface Validate {

    <I extends Info> void validate(I i);

}
