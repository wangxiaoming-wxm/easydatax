package com.easydatax.datax.admin.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.easydatax.datax.admin.service.DataxJsonService;
import com.easydatax.datax.admin.core.util.I18nUtil;
import com.easydatax.datax.admin.dto.DataXJsonBuildDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jingwk on 2020/05/05
 */

@RestController
@RequestMapping("api/dataxJson")
public class DataxJsonController extends BaseController {

    @Autowired
    private DataxJsonService dataxJsonService;


    @PostMapping("/buildJson")
    public R<String> buildJobJson(@RequestBody DataXJsonBuildDto dto) {
        String key = "system_please_choose";
        if (dto.getReaderDatasourceId() == null) {
            return failed(I18nUtil.getString(key) + I18nUtil.getString("jobinfo_field_readerDataSource"));
        }
        if (dto.getWriterDatasourceId() == null) {
            return failed(I18nUtil.getString(key) + I18nUtil.getString("jobinfo_field_writerDataSource"));
        }
        if (CollectionUtils.isEmpty(dto.getReaderColumns())) {
            return failed(I18nUtil.getString(key) + I18nUtil.getString("jobinfo_field_readerColumns"));
        }
        if (CollectionUtils.isEmpty(dto.getWriterColumns())) {
            return failed(I18nUtil.getString(key) + I18nUtil.getString("jobinfo_field_writerColumns"));
        }
        return success(dataxJsonService.buildJobJson(dto));
    }

}
