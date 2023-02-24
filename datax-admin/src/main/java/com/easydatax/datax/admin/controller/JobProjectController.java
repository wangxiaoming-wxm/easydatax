package com.easydatax.datax.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.easydatax.datax.admin.entity.JobProject;
import com.easydatax.datax.admin.service.JobProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * project manage controller
 *
 * @author jingwk 2020-05-24 16:13:16
 */
@RestController
@RequestMapping("/api/jobProject")
public class JobProjectController extends BaseController {

    @Autowired
    private JobProjectService jobProjectService;


    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    public R<IPage<JobProject>> selectAll(@RequestParam(value = "searchVal", required = false) String searchVal,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam("pageNo") Integer pageNo) {

        return success(jobProjectService.getProjectListPaging(pageSize, pageNo, searchVal));
    }

    /**
     * Get all project
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<JobProject>> selectList() {
        QueryWrapper<JobProject> query = new QueryWrapper();
        query.eq("flag", true);
        return success(jobProjectService.list(query));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R<JobProject> selectOne(@PathVariable Serializable id) {
        return success(this.jobProjectService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param entity 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Boolean> insert(HttpServletRequest request, @RequestBody JobProject entity) {
        entity.setUserId(getCurrentUserId(request));
        return success(this.jobProjectService.save(entity));
    }


    /**
     * 修改数据
     *
     * @param entity 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Boolean> update(@RequestBody JobProject entity) {
        JobProject project = jobProjectService.getById(entity.getId());
        project.setName(entity.getName());
        project.setDescription(entity.getDescription());
        return success(this.jobProjectService.updateById(entity));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return success(this.jobProjectService.removeByIds(idList));
    }
}