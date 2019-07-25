package cn.ctoedu.miaosha.controller;

import cn.ctoedu.miaosha.redis.RedisService;
import cn.ctoedu.miaosha.service.UserService;
import cn.ctoedu.miaosha.vo.LoginVo;
import com.lhd.common.util.AjaxJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by xiaomi on 2019/07/23
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LogManager.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/do_login")
    @ResponseBody
    public AjaxJson doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        String token = userService.login(response, loginVo);
        return AjaxJson.success(token);
    }
}
