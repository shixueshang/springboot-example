package cn.ctoedu.miaosha.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by xiaomi on 2019/07/22
 */
@Data
public class User {

    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
