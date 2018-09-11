package cn.qblank.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/29 13:44
 */
@Data
@ToString
@Builder
public class PageResult<T> {
    private List<T> data = Lists.newArrayList();

    private int total;
}
