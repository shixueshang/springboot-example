package cn.ctoedu.miaosha.service;

import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.exception.BadRequestException;
import cn.ctoedu.miaosha.mapper.UserMapper;
import cn.ctoedu.miaosha.redis.MiaoshaUserKey;
import cn.ctoedu.miaosha.redis.RedisService;
import cn.ctoedu.miaosha.vo.LoginVo;
import com.alibaba.druid.util.StringUtils;
import com.lhd.common.util.CodeMessage;
import com.lhd.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xiaomi on 2019/07/22
 */
@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    public void register(User user){
        userMapper.insert(user);
    }

    public User getUserById(long id) {
        User user = redisService.get(MiaoshaUserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        user = userMapper.getUserById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        User user = getUserById(id);
        if (user == null) {
            throw new BadRequestException(CodeMessage.MOBILE_NOT_EXIST.getMsg());
        }
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(CommonUtil.md5(formPass + user.getSalt()));
        userMapper.update(toBeUpdate);
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    @Transactional
    public void batchAddUsers(List<User> users){
        userMapper.batchAddUsers(users);
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token, token, User.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new BadRequestException(CodeMessage.SERVER_ERROR.getMsg());
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        User user = getUserById(Long.parseLong(mobile));
        if (user == null) {
            throw new BadRequestException(CodeMessage.MOBILE_NOT_EXIST.getMsg());
        }
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = CommonUtil.md5(formPass + saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new BadRequestException(CodeMessage.PASSWORD_ERROR.getMsg());
        }
        String token = CommonUtil.getUuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
