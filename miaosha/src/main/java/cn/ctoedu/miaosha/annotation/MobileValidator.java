package cn.ctoedu.miaosha.annotation;

import com.lhd.common.util.CommonUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Created by xiaomi on 2019/07/23
 */
public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(ValidMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (required)
            return CommonUtil.isMobile(value);

        if (StringUtils.isEmpty(value))
            return true;

        return CommonUtil.isMobile(value);

    }
}
