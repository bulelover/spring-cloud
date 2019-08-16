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
    /**
     * 搜索框
     */
    @TableField(exist = false)
    private String pageSearch;
    /**
     * 排序方式
     */
    @TableField(exist = false)
    private boolean pageOrder = true;

    /**
     * 排序字段
     */
    @TableField(exist = false)
    private String pageOrderField;

    public String getPageSearch() {
        return pageSearch;
    }

    public void setPageSearch(String pageSearch) {
        this.pageSearch = pageSearch;
    }

    public boolean getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(boolean pageOrder) {
        this.pageOrder = pageOrder;
    }

    public String getPageOrderField() {
        return pageOrderField;
    }

    public void setPageOrderField(String pageOrderField) {
        this.pageOrderField = pageOrderField;
    }

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
