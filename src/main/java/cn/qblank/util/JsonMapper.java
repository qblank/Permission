package cn.qblank.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author evan_qb
 * @date 2018/8/8 14:45
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //初始化配置
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 对象转json
     * @param src
     * @param <T>
     * @return
     */
    public static <T> String object2String(T src){
        if (src == null){
            return null;
        }
        try {
            return src instanceof String ? (String) src:objectMapper.writeValueAsString(src);
        }catch (Exception e){
            log.warn("parse object to String exception,error:{}",e);
            return null;
        }
    }

    /**
     * json转对象
     * @param src
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Object(String src, TypeReference tTypeReference){
        if (src == null || tTypeReference == null){
            return null;
        }
        try {
            return (T)(tTypeReference.getType().equals(String.class) ? src: objectMapper
                        .readValue(src,tTypeReference));
        }catch (Exception e){
            log.warn("parse string to Object exception,String:{},TypeRefrenece<T>:{},errors:{}",src,tTypeReference.getType(),e);
            return null;
        }
    }

}
