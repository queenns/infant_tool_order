package cc.tool.order;

import cc.tool.order.generator.Generator;
import cc.tool.order.model.broken.BrokenInfo;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by lxj on 18-5-18
 */
public interface OrderBrokenTool extends Generator<Map<ObjectId, BrokenInfo>> {
}
