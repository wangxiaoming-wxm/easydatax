package com.easydatax.datax.admin.core.route.strategy;

import com.easydatax.datax.admin.core.route.ExecutorRouter;
import com.easydatax.datatx.core.biz.model.ReturnT;
import com.easydatax.datatx.core.biz.model.TriggerParam;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
