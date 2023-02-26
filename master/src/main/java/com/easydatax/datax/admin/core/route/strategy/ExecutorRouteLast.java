package com.easydatax.datax.admin.core.route.strategy;

import com.easydatax.datatx.core.biz.model.ReturnT;
import com.easydatax.datatx.core.biz.model.TriggerParam;
import com.easydatax.datax.admin.core.route.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
