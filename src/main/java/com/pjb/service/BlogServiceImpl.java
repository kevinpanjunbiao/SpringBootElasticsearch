package com.pjb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.entity.BlogInfo;
import com.pjb.entity.page.PageResult;
import com.pjb.entity.param.BlogSearchParam;
import com.pjb.service.impl.BlogService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 博客业务逻辑类
 * @author pan_junbiao
 **/
@Service
public class BlogServiceImpl implements BlogService
{
    /**
     * Elasticsearch高级客户端
     */
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //====================================索引操作==================================

    /**
     * 索引存在验证
     * @param indexName 索引名称
     */
    @Override
    public boolean existsIndex(String indexName)
    {
        boolean exists = false;

        //参数验证
        if(indexName==null || indexName.length()<=0)
        {
            return false;
        }

        try
        {
            //构建索引存在验证请求
            GetIndexRequest request = new GetIndexRequest(indexName);

            //执行请求
            exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return exists;
    }

    /**
     * 创建索引
     * @param indexName 索引名称
     */
    @Override
    public boolean createIndex(String indexName)
    {
        boolean result = false;

        //参数验证
        if(indexName==null || indexName.length()<=0)
        {
            return false;
        }

        try
        {
            //1.创建索引的请求
            CreateIndexRequest request = new CreateIndexRequest(indexName);

            //构建索引结构
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {
                    //创建博客ID文档字段
                    builder.startObject("blogId");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();

                    //创建博客名称文档字段
                    builder.startObject("blogName");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    //创建博客地址文档字段
                    builder.startObject("blogUrl");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                    //创建博客积分字段
                    builder.startObject("blogPoints");
                    {
                        builder.field("type", "double");
                    }
                    builder.endObject();

                    //创建创建时间字段
                    builder.startObject("createDate");
                    {
                        builder.field("type", "date");
                    }
                    builder.endObject();

                    //创建博客描述字段
                    builder.startObject("blogDescribe");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_smart")
                                //搜索时分词
                                .field("search_analyzer", "ik_max_word");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
            request.mapping(builder);

            //2客户端执行请求，请求后获得响应
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 删除索引
     * @param indexName 索引名称
     */
    @Override
    public boolean deleteIndex(String indexName)
    {
        boolean result = false;

        //参数验证
        if(indexName==null || indexName.length()<=0)
        {
            return false;
        }

        //注意：删除索引前，必须先判断索引是否存在，否则会报异常
        if(!existsIndex(indexName))
        {
            //该索引不存在
            return false;
        }

        try
        {
            //创建删除索引请求
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);

            //执行请求返回响应结果
            AcknowledgedResponse deleteIndexResponse = restHighLevelClient.indices().delete(request,RequestOptions.DEFAULT);

            //解析响应结果
            result = deleteIndexResponse.isAcknowledged();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    //====================================文档操作==================================

    /**
     * 新增文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param blogInfo 博客实体类
     */
    @Override
    public boolean addDocument(String indexName, String documentId, BlogInfo blogInfo)
    {
        boolean result = false;

        try
        {
            //将博客信息实体类转换为Map格式
            Map<String, Object> blogMap = toBlogMap(blogInfo);

            //1、构建新增文档请求
            IndexRequest request = new IndexRequest(indexName).id(documentId).source(blogMap);

            //2、执行请求，返回响应结果
            IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);

            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 将博客信息实体类转换为Map格式
     */
    private Map<String,Object> toBlogMap(BlogInfo blogInfo)
    {
        Map<String, Object> blogMap = new HashMap<>();
        blogMap.put("blogId", blogInfo.getBlogId());
        blogMap.put("blogName", blogInfo.getBlogName());
        blogMap.put("blogUrl", blogInfo.getBlogUrl());
        blogMap.put("blogPoints", blogInfo.getBlogPoints());
        blogMap.put("createDate", blogInfo.getCreateDate());
        blogMap.put("blogDescribe", blogInfo.getBlogDescribe());
        return blogMap;
    }

    /**
     * 批量新增文档
     * @param indexName 索引名称
     * @param blogList 博客列表
     */
    @Override
    public boolean addBulkDocument(String indexName, List<BlogInfo> blogList)
    {
        boolean result = false;

        try
        {

            //1、构建批量请求
            BulkRequest request = new BulkRequest();

            //遍历博客列表，添加批量请求
            for(BlogInfo blogInfo : blogList)
            {
                //将博客信息实体类转换为Map格式
                Map<String, Object> blogMap = toBlogMap(blogInfo);

                //文档ID（根据业务，我们以博客ID作为文档ID）
                String documentId = String.valueOf(blogInfo.getBlogId());

                //加入请求
                request.add(new IndexRequest(indexName).id(documentId).source(blogMap));
            }

            //执行批量请求
            BulkResponse bulkItemResponses = restHighLevelClient.bulk(request,RequestOptions.DEFAULT);

            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 修改文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param blogInfo 博客实体类
     */
    @Override
    public boolean updateDocument(String indexName, String documentId, BlogInfo blogInfo)
    {
        boolean result = false;

        try
        {
            //将博客信息实体类转换为Map格式
            Map<String, Object> blogMap = toBlogMap(blogInfo);

            //1、构建更新文档请求
            UpdateRequest request = new UpdateRequest(indexName,documentId).doc(blogMap);

            //2、执行请求，返回响应结果
            UpdateResponse response = restHighLevelClient.update(request,RequestOptions.DEFAULT);

            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 删除文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     */
    @Override
    public boolean deleteDocument(String indexName, String documentId)
    {
        boolean result = false;

        try
        {
            //1、构建删除文档请求
            DeleteRequest request = new DeleteRequest(indexName,documentId);

            //2、执行删除请求，返回响应结果
            DeleteResponse deleteResponse = restHighLevelClient.delete(request,RequestOptions.DEFAULT);

            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 判断文档是否存在
     * @param indexName 索引名称
     * @param documentId 文档ID
     */
    @Override
    public boolean existsDocument(String indexName, String documentId)
    {
        boolean result = false;

        try
        {
            //1、构建请求
            GetRequest getRequest = new GetRequest(indexName, documentId);
            //禁用提取源
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            //禁用提取存储字段
            getRequest.storedFields("_none_");

            //2、执行请求，返回结果
            result = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * 获取文档
     * @param indexName 索引名称
     * @param documentId 文档ID
     * @param beanType 返回结果的类型
     */
    @Override
    public <T> T getDocumentToBean(String indexName, String documentId, Class<T> beanType)
    {
        T result = null;

        try
        {
            //1、构建请求
            GetRequest getRequest = new GetRequest(indexName, documentId);

            //2、执行请求，返回响应结果
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);

            //3、解析响应结果
            if (getResponse != null)
            {
                //获取JSON结果
                String jsonString = getResponse.getSourceAsString();

                //使用Jackson工具，将JSON转换为实体类
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(jsonString, beanType);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    //====================================搜索操作==================================

    /**
     * 分页搜索列表
     */
    @Override
    public PageResult<BlogInfo> searchBlogListPage(BlogSearchParam param)
    {
        PageResult<BlogInfo> blogInfoPageResult = new PageResult<>();
        List<BlogInfo> blogInfoList = new ArrayList<>();
        blogInfoPageResult.setDataList(blogInfoList);

        //参数验证
        if(param.getIndexName() ==null || param.getIndexName().length()<=0)
        {
            return blogInfoPageResult;
        }

        try
        {
            //1、构建搜索请求
            SearchRequest searchRequest = new SearchRequest(param.getIndexName());

            //大多数搜索参数都添加到 SearchSourceBuilder 类中
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            //设置“全部匹配”查询
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());

            //设置分页条件
            searchSourceBuilder.from(param.getOffset()).size(param.getPageSize());

            //查询条件1：博客描述
            QueryBuilder queryBuilder = QueryBuilders.boolQuery();
            if(param.getBlogDescribe()!=null && param.getBlogDescribe().length()>0)
            {
                MatchPhraseQueryBuilder mpq1 = QueryBuilders.matchPhraseQuery("blogDescribe", param.getBlogDescribe());
                ((BoolQueryBuilder) queryBuilder).must(mpq1);
            }

            //查询条件2：根据博客积分区间范围
            RangeQueryBuilder mpq2 = QueryBuilders.rangeQuery("blogPoints");
            if(param.getPointsBegin()>0)
            {
                mpq2.from(param.getPointsBegin());
            }
            if(param.getPointsEnd()>0)
            {
                mpq2.to(param.getPointsEnd());
            }
            ((BoolQueryBuilder) queryBuilder).must(mpq2);

            //将多个查询条件组合
            searchSourceBuilder.query(queryBuilder);

            //设置排序，注意：在ES中，只有keyword类型的字符串可以排序、分组聚合
            searchSourceBuilder.sort(new FieldSortBuilder("blogPoints").order(SortOrder.DESC));
            searchSourceBuilder.sort(new FieldSortBuilder("createDate").order(SortOrder.ASC));

            //设置一个可选的超时时间，控制允许搜索的时间
            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            //将 SearchSourceBuilder 添加到 SearchRequest 中
            searchRequest.source(searchSourceBuilder);

            //2、执行搜索请求，获取响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //3、解析搜索请求的响应结果
            System.out.println("响应结果：" + searchResponse);
            //获取HTTP状态码
            RestStatus status = searchResponse.status();
            System.out.println("HTTP状态码：" + status);

            //获取请求执行时间
            TimeValue took = searchResponse.getTook();
            System.out.println("请求执行时间：" + took);

            //获取请求是否超时
            boolean timedOut = searchResponse.isTimedOut();
            System.out.println("请求是否超时：" + timedOut);

            //查看搜索影响的分片总数
            int totalShards = searchResponse.getTotalShards();
            System.out.println("查看搜索影响的分片总数：" + totalShards);

            //执行搜索成功的分片的统计信息
            int successfulShards = searchResponse.getSuccessfulShards();
            System.out.println("执行搜索成功的分片的统计信息：" + successfulShards);

            //执行搜索失败的分片的统计信息
            int failedShards = searchResponse.getFailedShards();
            System.out.println("执行搜索失败的分片的统计信息：" + failedShards);

            //遍历错误信息
            for(ShardSearchFailure failure : searchResponse.getShardFailures())
            {
                System.out.println("错误信息：" + failure.toString());
            }

            //获取响应中包含的搜索结果
            SearchHits hits = searchResponse.getHits();

            //SearchHits提供了相关结果的全部信息，如：点击总数、最高分数
            TotalHits totalHits = hits.getTotalHits();

            //搜索结果的总数量
            long total = totalHits.value;
            System.out.println("搜索结果的总数量：" + total);

            //点击总数
            long numHits = totalHits.value;
            System.out.println("点击总数：" + numHits);

            //最高分数
            float maxScore = hits.getMaxScore();
            System.out.println("最高分数：" + maxScore);

            //嵌套在 SearchHits 中的是可以迭代的单个搜索结果
            SearchHit[] searchHits = hits.getHits();
            for(SearchHit hit : searchHits)
            {
                String index = hit.getIndex(); //索引
                String id = hit.getId(); //文档ID
                float score = hit.getScore(); //每次搜索的得分
                System.out.println("索引：" + index + "，文档ID：" + id + "，每次搜索的得分：" + score);

                //以JSON字符串形式返回文档源
                String sourceAsString = hit.getSourceAsString();
                System.out.println("以JSON字符串形式返回文档源：" + sourceAsString);

                //以键值对形式返回文档源
                Map<String,Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println("以键值对形式返回文档源：" + sourceAsMap.toString());

                //使用Jackson工具，将JSON转换为实体类
                ObjectMapper mapper = new ObjectMapper();
                BlogInfo blogInfo = mapper.readValue(sourceAsString, BlogInfo.class);
                blogInfoList.add(blogInfo);
            }

            //4、封装分页搜索结果类
            blogInfoPageResult.setDataList(blogInfoList); //数据列表
            blogInfoPageResult.setPageIndex(param.getPageIndex()); //当前页码
            blogInfoPageResult.setPageSize(param.getPageSize()); //分页大小
            blogInfoPageResult.setTotalData((int)total);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return blogInfoPageResult;
    }
}
