package com.cloud.provider.gdws.sql.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cloud.provider.common.entity.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 村室系统sql记录模型
 * @author SouthXia
 * @since 2019-08-09
 */
@TableName("conv_sql_record")
public class SqlRecord extends BaseModel<SqlRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * id唯一主键
     */
    private String id;

    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 更新者代码
     */
    private String ucode;

    /**
     * 更新者姓名
     */
    private String uname;

    /**
     * 更新时间
     */
    private String utime;

    /**
     * 创建时间
     */
    private String ctime;

    /**
     * 创建者代码
     */
    private String ccode;

    /**
     * 创建者姓名
     */
    private String cname;

    private Integer gzdb121;

    private Integer xagldb;

    private Integer flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getUcode() {
        return ucode;
    }

    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }
    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }
    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    public Integer getGzdb121() {
        return gzdb121;
    }

    public void setGzdb121(Integer gzdb121) {
        this.gzdb121 = gzdb121;
    }
    public Integer getXagldb() {
        return xagldb;
    }

    public void setXagldb(Integer xagldb) {
        this.xagldb = xagldb;
    }
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SqlRecord{" +
        "id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", ucode=" + ucode +
        ", uname=" + uname +
        ", utime=" + utime +
        ", ctime=" + ctime +
        ", ccode=" + ccode +
        ", cname=" + cname +
        ", gzdb121=" + gzdb121 +
        ", xagldb=" + xagldb +
        ", flag=" + flag +
        "}";
    }
}
