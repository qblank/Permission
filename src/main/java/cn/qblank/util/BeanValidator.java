package cn.qblank.util;

import cn.qblank.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.validation.*;
import java.util.*;

/**
 * @author evan_qb
 * @date 2018/8/8 11:24
 */
public class BeanValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证并以Map方式返回错误信息
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T> Map<String,String> validate(T t,Class... groups){
        //获取验证器
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t,groups);
        if (validateResult.isEmpty()){
            return Collections.emptyMap();
        }else{
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator it = validateResult.iterator();
            while (it.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) it.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }

    }

    /**
     * 验证集合
     * @param collection
     * @return
     */
    public static Map<String,String> validateList(Collection<?> collection){
        Preconditions.checkNotNull(collection);
        Iterator<?> it = collection.iterator();
        Map errors = null;
        do{
            if (!it.hasNext()){
                return Collections.emptyMap();
            }
            Object object = it.next();
            errors = validate(object,new Class[0]);
        }while (errors.isEmpty());
        return errors;
    }

    public static Map<String,String> validateObject(Object first,Object... objects){
        if (objects != null && objects.length > 0){
            return validateList(Lists.asList(first,objects));
        }else{
            return validate(first,new Class[0]);
        }
    }

    /**
     * 验证信息
     * @param param
     * @throws ParamException
     */
    public static void check(Object param) throws ParamException{
        Map<String,String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)){
            throw new ParamException(map.toString());
        }
    }
}
