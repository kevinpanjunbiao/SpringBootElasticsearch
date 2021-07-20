package com.pjb.entity.param;

import com.pjb.entity.page.BasePaging;

/**
 * 博客搜索参数类
 * @author pan_junbiao
 **/
public class BlogSearchParam extends BasePaging
{
    private String blogDescribe; //博客描述
    private double pointsBegin; //开始博客积分
    private double pointsEnd; //结束博客积分

    public String getBlogDescribe()
    {
        return blogDescribe;
    }

    public void setBlogDescribe(String blogDescribe)
    {
        this.blogDescribe = blogDescribe;
    }

    public double getPointsBegin()
    {
        return pointsBegin;
    }

    public void setPointsBegin(double pointsBegin)
    {
        this.pointsBegin = pointsBegin;
    }

    public double getPointsEnd()
    {
        return pointsEnd;
    }

    public void setPointsEnd(double pointsEnd)
    {
        this.pointsEnd = pointsEnd;
    }
}
