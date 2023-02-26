package com.easydatax.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by jingwk on 2020/05/24
 */
@Data
public class JobProject {

    private int id;
    private String name;
    private String description;
   private int userId;
    private Boolean flag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
    @TableField(exist=false)
    private String userName;

}
