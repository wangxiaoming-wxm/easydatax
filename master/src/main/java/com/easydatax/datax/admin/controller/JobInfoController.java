package com.easydatax.datax.admin.controller;


import com.easydatax.datax.admin.entity.JobInfo;
import com.easydatax.datax.admin.service.JobService;
import com.easydatax.datatx.core.biz.model.ReturnT;
import com.easydatax.datatx.core.util.DateUtil;
import com.easydatax.datax.admin.core.cron.CronExpression;
import com.easydatax.datax.admin.core.thread.JobTriggerPoolHelper;
import com.easydatax.datax.admin.core.trigger.TriggerTypeEnum;
import com.easydatax.datax.admin.core.util.I18nUtil;
import com.easydatax.datax.admin.dto.DataXBatchJsonBuildDto;
import com.easydatax.datax.admin.dto.TriggerJobDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@RestController
@RequestMapping("/api/job")
public class JobInfoController extends BaseController{

    @Resource
    private JobService jobService;


    @GetMapping("/pageList")
    public ReturnT<Map<String, Object>> pageList(@RequestParam(required = false, defaultValue = "0") int current,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        int jobGroup, int triggerStatus, String jobDesc, String glueType, Integer[] projectIds) {

        return new ReturnT<>(jobService.pageList((current-1)*size, size, jobGroup, triggerStatus, jobDesc, glueType, 0, projectIds));
    }

    @GetMapping("/list")
    public ReturnT<List<JobInfo>> list(){
        return new ReturnT<>(jobService.list());
    }

    @PostMapping("/add")
    public ReturnT<String> add(HttpServletRequest request, @RequestBody JobInfo jobInfo) {
        jobInfo.setUserId(getCurrentUserId(request));
        return jobService.add(jobInfo);
    }

    @PostMapping("/update")
    public ReturnT<String> update(HttpServletRequest request,@RequestBody JobInfo jobInfo) {
        jobInfo.setUserId(getCurrentUserId(request));
        return jobService.update(jobInfo);
    }

    @PostMapping(value = "/remove/{id}")
    public ReturnT<String> remove(@PathVariable(value = "id") int id) {
        return jobService.remove(id);
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ReturnT<String> pause(int id) {
        return jobService.stop(id);
    }

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public ReturnT<String> start(int id) {
        return jobService.start(id);
    }

    @PostMapping(value = "/trigger")
    public ReturnT<String> triggerJob(@RequestBody TriggerJobDto dto) {
        // force cover job param
        String executorParam=dto.getExecutorParam();
        if (executorParam == null) {
            executorParam = "";
        }
        JobTriggerPoolHelper.trigger(dto.getJobId(), TriggerTypeEnum.MANUAL, -1, null, executorParam);
        return ReturnT.SUCCESS;
    }

    @GetMapping("/nextTriggerTime")
    public ReturnT<List<String>> nextTriggerTime(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = cronExpression.getNextValidTimeAfter(lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            return new ReturnT<>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_invalid"));
        }
        return new ReturnT<>(result);
    }

    @PostMapping("/batchAdd")
    public ReturnT<String> batchAdd(@RequestBody DataXBatchJsonBuildDto dto) throws IOException {
        if (dto.getTemplateId() ==0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_choose") + I18nUtil.getString("jobinfo_field_temp")));
        }
        return jobService.batchAdd(dto);
    }
}
