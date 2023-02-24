package com.easydatax.datax.admin.entity;

import lombok.Data;

import java.util.Date;

/**
 * xxl-job log for glue, used to track job code process
 *
 * @author xuxueli 2016-5-19 17:57:46
 */
@Data
public class JobLogGlue {

    private int id;
    private int jobId;
    private String glueType;
    private String glueSource;
    private String glueRemark;
    private Date addTime;
    private Date updateTime;

}
