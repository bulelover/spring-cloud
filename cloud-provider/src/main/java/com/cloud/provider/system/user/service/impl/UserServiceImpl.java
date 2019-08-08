package com.cloud.provider.system.user.service.impl;

import com.cloud.provider.system.user.entity.User;
import com.cloud.provider.system.user.mapper.UserMapper;
import com.cloud.provider.system.user.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SouthXia
 * @since 2019-08-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
