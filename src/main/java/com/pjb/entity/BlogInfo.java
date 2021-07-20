package com.pjb.entity;

import java.util.Date;

/**
 * 博客信息实体类
 * @author pan_junbiao
 **/
public class BlogInfo
{
    private int blogId; //博客ID
    private String blogName; //博客名称
    private String blogUrl; //博客地址
    private double blogPoints; //博客积分
    private Date createDate; //创建时间
    private String blogDescribe; //博客描述

    //构建方法1
    public BlogInfo()
    {
    }

    //构建方法2
    public BlogInfo(int blogId, String blogName, String blogUrl, double blogPoints, Date createDate, String blogDescribe)
    {
        this.blogId = blogId;
        this.blogName = blogName;
        this.blogUrl = blogUrl;
        this.blogPoints = blogPoints;
        this.createDate = createDate;
        this.blogDescribe = blogDescribe;
    }

    public int getBlogId()
    {
        return blogId;
    }

    public void setBlogId(int blogId)
    {
        this.blogId = blogId;
    }

    public String getBlogName()
    {
        return blogName;
    }

    public void setBlogName(String blogName)
    {
        this.blogName = blogName;
    }

    public String getBlogUrl()
    {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl)
    {
        this.blogUrl = blogUrl;
    }

    public double getBlogPoints()
    {
        return blogPoints;
    }

    public void setBlogPoints(double blogPoints)
    {
        this.blogPoints = blogPoints;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public String getBlogDescribe()
    {
        return blogDescribe;
    }

    public void setBlogDescribe(String blogDescribe)
    {
        this.blogDescribe = blogDescribe;
    }
}