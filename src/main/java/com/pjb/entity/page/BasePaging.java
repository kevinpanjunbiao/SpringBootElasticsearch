package com.pjb.entity.page;

/**
 * 公共分页类
 * @author pan_junbiao
 **/
public class BasePaging
{
    private int pageIndex; //当前页码
    private int pageSize; //分页大小
    private int totalData; //数据总数
    private int totalPage; //总页数
    private String orderBy; //排序
    private int offset; //偏移量
    private String indexName; //索引名称

    public int getPageIndex()
    {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getTotalData()
    {
        return totalData;
    }

    public void setTotalData(int totalData)
    {
        this.totalData = totalData;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    //计算偏移量：偏移量 = (page_index-1)*page_size
    public int getOffset()
    {
        int offset = 0;
        if(this.pageIndex>0 && this.pageSize>0)
        {
            offset = (this.pageIndex-1)*this.pageSize;
        }
        return offset;
    }

    //计算总页数：总页数 = (数据总数 + 分页大小 -1) / 分页大小
    public int getTotalPage()
    {
        int totalPage = 0;
        if (this.totalData > 0 && this.pageSize > 0)
        {
            totalPage = (this.totalData + this.pageSize - 1) / this.pageSize;
        }
        return totalPage;
    }

    public String getIndexName()
    {
        return indexName;
    }

    public void setIndexName(String indexName)
    {
        this.indexName = indexName;
    }
}