package com.easydatax.datax.admin.controller;

import com.easydatax.datax.admin.service.JobService;
import com.easydatax.datatx.core.biz.model.ReturnT;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 *
 * @author jingwk 2019-12-22 16:13:16
 */
@RestController
@RequestMapping("/api")
public class IndexController {

    @Resource
    private JobService jobService;


    @GetMapping("/index")
    public ReturnT<Map<String, Object>> index() {
        return new ReturnT<>(jobService.dashboardInfo());
    }

    @PostMapping("/chartInfo")
    public ReturnT<Map<String, Object>> chartInfo() {
        return jobService.chartInfo();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
