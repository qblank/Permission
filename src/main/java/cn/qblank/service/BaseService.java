package cn.qblank.service;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/7 15:25
 */
public interface BaseService<T> {
    void save(T t);

    void update(T t);

    void deleteById(Serializable id);

    T getById(Serializable id);

    List<T> getList();

    PageInfo<T> getListByPage(int currentNum, int pageSize);
}
