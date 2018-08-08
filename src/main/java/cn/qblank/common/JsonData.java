package cn.qblank.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/8/8 10:21
 */
@Data
public class JsonData {
    /*true表示正常，false表示异常*/
    private boolean ret;

    private String msg;

    private Object data;

    public JsonData(boolean ret){
        this.ret = ret;
    }

    /**
     * 成功
     * @param object
     * @param msg
     * @return
     */
    public static JsonData success(Object object,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(Object object){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("ret",ret);
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }
}
