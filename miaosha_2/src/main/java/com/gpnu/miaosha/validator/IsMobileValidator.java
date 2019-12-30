package com.gpnu.miaosha.validator;

import com.gpnu.miaosha.util.ValidateUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidateUtil.isMobile(value);
        }else{
            if(StringUtils.isEmpty(value)) {
                return true;
            }else{
                return ValidateUtil.isMobile(value);
            }
        }
    }
}
