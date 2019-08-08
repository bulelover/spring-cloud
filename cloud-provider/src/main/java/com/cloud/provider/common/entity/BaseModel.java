package com.cloud.provider.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * @author SouthXia 19.8.6
 */
public abstract class BaseModel<T extends Model> extends Model {
    /**
     *   页码
     */
    @TableField(exist = false)
    private int pageCurrent=1;
    /**
     * 每页条数
     */
    @TableField(exist = false)
    private int pageSize=20;

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
