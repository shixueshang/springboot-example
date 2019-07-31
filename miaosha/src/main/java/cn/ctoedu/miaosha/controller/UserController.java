package cn.ctoedu.miaosha.controller;

import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.service.UserService;
import com.lhd.common.util.AjaxJson;
import com.lhd.common.util.CodeMessage;
import com.lhd.common.util.CommonUtil;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by xiaomi on 2019/07/24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @PostMapping(value = "/register")
    public AjaxJson register(@Validated @RequestBody User user) {
        try {
            String salt = CommonUtil.createRandom(20, false);
            user.setSalt(salt);
            user.setRegisterDate(new Date());
            user.setPassword(CommonUtil.md5(user.getPassword() + salt));
            userService.register(user);
        } catch (Exception e) {
            logger.error("register fail ", e);
            return AjaxJson.error( CodeMessage.SERVER_ERROR);
        }

        return AjaxJson.success(CodeMessage.SUCCESS);
    }

    @GetMapping("info")
    public AjaxJson userInfo(HttpServletResponse response, @RequestParam("token") String token){
       User user =  userService.getByToken(response, token);
       return new AjaxJson(HttpStatus.SC_OK, user);
    }
}
