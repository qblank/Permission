package cn.qblank.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author evan_qb
 * @date 2018/8/29 13:35
 */
public class PageQuery {
    @Getter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo = 1;

    @Getter
    @Min(value = 1,message = "每页展示数量不合法")
    private int pageSize = 10;
    @Setter
    private int offset;

    public int getOffset(){
        return (pageNo - 1) * pageSize;
    }
}
