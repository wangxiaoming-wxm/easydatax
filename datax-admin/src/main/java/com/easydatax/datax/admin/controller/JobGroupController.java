package com.easydatax.datax.admin.controller;

import com.easydatax.datax.admin.entity.JobGroup;
import com.easydatax.datax.admin.entity.JobRegistry;
import com.easydatax.datax.admin.mapper.JobGroupMapper;
import com.easydatax.datax.admin.mapper.JobInfoMapper;
import com.easydatax.datax.admin.mapper.JobRegistryMapper;
import com.easydatax.datatx.core.biz.model.ReturnT;
import com.easydatax.datatx.core.enums.RegistryConfig;
import com.easydatax.datax.admin.core.util.I18nUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/jobGroup")
public class JobGroupController {

    @Resource
    public JobInfoMapper jobInfoMapper;
    @Resource
    public JobGroupMapper jobGroupMapper;
    @Resource
    private JobRegistryMapper jobRegistryMapper;

    @GetMapping("/list")
    public ReturnT<List<JobGroup>> getExecutorList() {
        return new ReturnT<>(jobGroupMapper.findAll());
    }

    @PostMapping("/save")
    public ReturnT<String> save(@RequestBody JobGroup jobGroup) {

        // valid
        if (jobGroup.getAppName() == null || jobGroup.getAppName().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (jobGroup.getAppName().length() < 4 || jobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (jobGroup.getTitle() == null || jobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (jobGroup.getAddressType() != 0) {
            if (jobGroup.getAddressList() == null || jobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresses = jobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_invalid"));
                }
            }
        }

        int ret = jobGroupMapper.save(jobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @PostMapping("/update")
    public ReturnT<String> update(@RequestBody JobGroup jobGroup) {
        // valid
        if (jobGroup.getAppName() == null || jobGroup.getAppName().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (jobGroup.getAppName().length() < 4 || jobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (jobGroup.getTitle() == null || jobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (jobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(jobGroup.getAppName());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            jobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (jobGroup.getAddressList() == null || jobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresses = jobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_invalid"));
                }
            }
        }

        int ret = jobGroupMapper.update(jobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    private List<String> findRegistryByAppName(String appNameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<>();
        List<JobRegistry> list = jobRegistryMapper.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (JobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appName = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appName);
                    if (registryList == null) {
                        registryList = new ArrayList<>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appName, registryList);
                }
            }
        }
        return appAddressMap.get(appNameParam);
    }

    @PostMapping("/remove")
    public ReturnT<String> remove(int id) {

        // valid
        int count = jobInfoMapper.pageListCount(0, 10, id, -1, null, null, 0,null);
        if (count > 0) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<JobGroup> allList = jobGroupMapper.findAll();
        if (allList.size() == 1) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = jobGroupMapper.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping(value = "/loadById", method = RequestMethod.POST)
    public ReturnT<JobGroup> loadById(int id) {
        JobGroup jobGroup = jobGroupMapper.load(id);
        return jobGroup != null ? new ReturnT<>(jobGroup) : new ReturnT<>(ReturnT.FAIL_CODE, null);
    }

    @GetMapping("/query")
    public ReturnT<List<JobGroup>> get(
                                       @RequestParam(value = "appName", required = false) String appName,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "addressList", required = false) String addressList) {
        return new ReturnT<>(jobGroupMapper.find(appName, title, addressList));
    }

}
