package com.cloud.provider.system.user.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cloud.provider.common.entity.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author SouthXia
 * @since 2019-08-02
 */
@TableName("base_sys_user")
public class User extends BaseModel<User>{

    private static final long serialVersionUID = 1L;

    /**
     * id唯一主键
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    @TableField("id_no")
    private String idNo;

    /**
     * 状态0锁定 1正常
     */
    private Integer state;

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
    private Date utime;

    /**
     * 性别 1男 2女
     */
    private Integer sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", realName=" + realName +
        ", phone=" + phone +
        ", idNo=" + idNo +
        ", state=" + state +
        ", ucode=" + ucode +
        ", uname=" + uname +
        ", utime=" + utime +
        ", sex=" + sex +
        "}";
    }
}
