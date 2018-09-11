package cn.qblank.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author evan_qb
 * @date 2018/8/8 17:08
 */
public class LevelUtil {
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";
    /**
     * 0
     * 0.1
     * 0.1.2
     * 0.1.3
     * 0.2
     * 用于获取下层级别
     * @param parentLevel
     * @param parentId
     * @return
     */
    public static String calculaterLevel(String parentLevel,int parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else{
            //假如当前level为0,那么下层的level则为0.1
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }

}
