package com.easydatax.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easydatax.datax.admin.entity.JobRegistry;
import com.easydatax.datax.admin.mapper.JobRegistryMapper;
import com.easydatax.datax.admin.service.JobRegistryService;
import org.springframework.stereotype.Service;

/**
 * JobRegistryServiceImpl
 * @author jingwk
 * @since 2019-03-15
 * @version v2.1.1
 */
@Service("jobRegistryService")
public class JobRegistryServiceImpl extends ServiceImpl<JobRegistryMapper, JobRegistry> implements JobRegistryService {

}