package cn.ctoedu.miaosha.vo;

import cn.ctoedu.miaosha.annotation.ValidMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by xiaomi on 2019/07/23
 */
@Data
public class LoginVo {

    @NotNull
    @ValidMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    @Override
    public String toString() {
        return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
    }
}
