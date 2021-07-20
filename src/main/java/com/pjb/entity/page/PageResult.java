package com.pjb.entity.page;

import java.util.List;

/**
 * 分页结果类
 * @author pan_junbiao
 **/
public class PageResult<T> extends BasePaging
{
    private List<T> dataList; //数据列表

    public List<T> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<T> dataList)
    {
        this.dataList = dataList;
    }
}