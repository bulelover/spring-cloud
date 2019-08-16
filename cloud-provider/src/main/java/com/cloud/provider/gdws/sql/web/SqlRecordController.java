package com.cloud.provider.gdws.sql.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cloud.auth.entity.Res;
import com.cloud.auth.utils.DateTools;
import com.cloud.provider.common.BaseController;
import com.cloud.provider.gdws.sql.entity.SqlRecord;
import com.cloud.provider.gdws.sql.service.ISqlRecordService;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SouthXia
 * @since 2019-08-09
 */
@Controller
@RequestMapping("/sqlRecord")
public class SqlRecordController extends BaseController {

    @Autowired
    private ISqlRecordService sqlRecordService;

    @RequestMapping("/query")
    @ResponseBody
    public Res query(@RequestBody SqlRecord entity){
        Page<SqlRecord> page = new Page<>(entity.getPageCurrent(), entity.getPageSize());
        Wrapper<SqlRecord> wrapper = new EntityWrapper<>();
        wrapper.like(!StringUtils.isEmpty(entity.getTitle()),"title",entity.getTitle());
        if(StringUtils.isNotEmpty(entity.getContent())){
            wrapper.like("content",entity.getContent());
        }
//                .orderBy("realName".equals(entity.getPageOrderField()),"real_name",entity.getPageOrder())
        wrapper.orderBy("ctime",false);
        int count = sqlRecordService.selectCount(wrapper);
        page= sqlRecordService.selectPage(page,wrapper);
        page.setTotal(count);
        return Res.success(page);
    }

    @RequestMapping("/query/{id}")
    @ResponseBody
    public Res query(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id)){
            return Res.failure("参数[id]不能为空");
        }
        SqlRecord record = sqlRecordService.selectById(id);
        return Res.success(record);
    }

    @RequestMapping("/save")
    @ResponseBody
    public Res save(@RequestBody SqlRecord entity){
        boolean bool;
        //新增
        if(StringUtils.isEmpty(entity.getId())){
            entity.setId(UUID.randomUUID().toString());
            entity.setCcode(this.getLoginer().getUsername());
            entity.setCname(this.getLoginer().getRealName());
            entity.setCtime(DateTools.getSysTime());
            entity.setUcode(this.getLoginer().getUsername());
            entity.setUname(this.getLoginer().getRealName());
            entity.setUtime(DateTools.getSysTime());
            bool = sqlRecordService.insert(entity);
            return Res.custom(bool,"插入数据失败！");
        }
        //编辑
        entity.setUcode(this.getLoginer().getUsername());
        entity.setUname(this.getLoginer().getRealName());
        entity.setUtime(DateTools.getSysTime());
        bool = sqlRecordService.updateById(entity);
        return Res.custom(bool,"未更新到任何数据！");
    }

}
