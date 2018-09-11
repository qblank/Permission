package cn.qblank.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author evan_qb
 * @date 2018/9/11
 */
@Getter
@Setter
@ToString
public class SearchLogParam {
    private Integer type; // LogType

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private String fromTime;//yyyy-MM-dd HH:mm:ss

    private String toTime;
}
