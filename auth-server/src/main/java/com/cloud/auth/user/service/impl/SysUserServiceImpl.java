package com.cloud.auth.user.service.impl;

import com.cloud.auth.user.entity.SysUser;
import com.cloud.auth.user.mapper.SysUserMapper;
import com.cloud.auth.user.service.ISysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SouthXia
 * @since 2019-05-27
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
