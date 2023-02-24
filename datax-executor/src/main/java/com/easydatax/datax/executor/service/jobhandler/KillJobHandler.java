package com.easydatax.datax.executor.service.jobhandler;


import cn.hutool.core.io.FileUtil;
import com.easydatax.datatx.core.biz.model.ReturnT;
import com.easydatax.datatx.core.biz.model.TriggerParam;
import com.easydatax.datatx.core.handler.IJobHandler;
import com.easydatax.datatx.core.handler.annotation.JobHandler;
import com.easydatax.datatx.core.util.ProcessUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;

/**
 * DataX任务终止
 *
 * @author jingwk 2019-12-16
 */

@JobHandler(value = "killJobHandler")
@Component
public class KillJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(TriggerParam tgParam) {
        String processId = tgParam.getProcessId();
        boolean result = ProcessUtil.killProcessByPid(processId);
        //  删除临时文件
        if (!CollectionUtils.isEmpty(jobTmpFiles)) {
            String pathname = jobTmpFiles.get(processId);
            if (pathname != null) {
                FileUtil.del(new File(pathname));
                jobTmpFiles.remove(processId);
            }
        }
        return result ? IJobHandler.SUCCESS : IJobHandler.FAIL;
    }
}
