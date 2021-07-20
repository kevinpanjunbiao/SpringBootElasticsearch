package com.pjb.service.impl;

import com.pjb.entity.BlogInfo;
import com.pjb.entity.page.PageResult;
import com.pjb.entity.param.BlogSearchParam;

import java.util.List;

/**
 * 博客业务逻辑接口
 * @author pan_junbiao
 **/
public interface BlogService
{
    //====================================索引操作==================================

    /**
     * 索引存在验证
     * @param indexName 索引名称
     */
    public boolean existsIndex(String indexName);

    /**
     * 创建索引
     * @param indexName 索引名称
     */
    public boolean createIndex(String indexName);

    /**
     * 删除索引
     * @param indexName 索引名称
     */
    public boolean deleteIndex(String indexName);

    //====================================文档操作==================================

    /**
     * 新增文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param blogInfo 博客实体类
     */
    public boolean addDocument(String indexName, String documentId, BlogInfo blogInfo);

    /**
     * 批量新增文档
     * @param indexName 索引名称
     * @param blogList 博客列表
     */
    public boolean addBulkDocument(String indexName, List<BlogInfo> blogList);

    /**
     * 修改文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param blogInfo 博客实体类
     */
    public boolean updateDocument(String indexName, String documentId, BlogInfo blogInfo);

    /**
     * 删除文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     */
    public boolean deleteDocument(String indexName, String documentId);

    /**
     * 判断文档是否存在
     * @param indexName 索引名称
     * @param documentId 文档ID
     */
    public boolean existsDocument(String indexName, String documentId);

    /**
     * 获取文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param beanType 返回结果的类型
     */
    public <T> T getDocumentToBean(String indexName, String documentId, Class<T> beanType);

    //====================================搜索操作==================================

    /**
     * 分页搜索列表
     */
    public PageResult<BlogInfo> searchBlogListPage(BlogSearchParam param);
}
