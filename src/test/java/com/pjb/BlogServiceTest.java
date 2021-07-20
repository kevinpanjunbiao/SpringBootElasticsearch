package com.pjb;

import com.pjb.entity.BlogInfo;
import com.pjb.entity.page.PageResult;
import com.pjb.entity.param.BlogSearchParam;
import com.pjb.service.impl.BlogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客服务测试类
 * @author pan_junbiao
 **/
@SpringBootTest
public class BlogServiceTest
{
    @BeforeEach
    private void setUp()
    {
        System.out.println("\n\n\n");
    }

    @AfterEach
    private void tearDown()
    {
        System.out.println("\n\n\n");
    }

    /**
     * 博客业务逻辑类
     */
    @Autowired
    private BlogService blogService;

    /**
     * 索引名称
     * 注意：索引名称必须小写
     */
    private String _indexName = "blog_info";

    //====================================索引操作==================================

    /**
     * 测试：创建索引
     * @author pan_junbiao
     */
    @Test
    public void createIndex()
    {
        //判断索引是否存在
        boolean exists = blogService.existsIndex(_indexName);
        if(exists)
        {
            System.out.println("索引已经存在");
            return;
        }

        //创建索引
        boolean result = blogService.createIndex(_indexName);
        if(result)
        {
            System.out.println("索引创建成功");
        }
        else
        {
            System.out.println("索引创建失败");
        }
    }

    /**
     * 测试：删除索引
     * @author pan_junbiao
     */
    @Test
    public void deleteIndex()
    {
        //删除索引
        boolean result = blogService.deleteIndex(_indexName);
        if(result)
        {
            System.out.println("删除索引成功");
        }
        else
        {
            System.out.println("删除索引失败");
        }

        //判断索引是否存在
        boolean exists = blogService.existsIndex(_indexName);
        if(exists)
        {
            System.out.println("索引存在");
        }
        else
        {
            System.out.println("索引不存在");
        }
    }

    //====================================文档操作==================================

    /**
     * 测试：新增文档
     * @author pan_junbiao
     */
    @Test
    public void addDocument()
    {
        //创建博客实体类
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setBlogId(1);
        blogInfo.setBlogName("pan_junbiao的博客");
        blogInfo.setBlogUrl("https://blog.csdn.net/pan_junbiao");
        blogInfo.setBlogPoints(120.68);
        blogInfo.setCreateDate(new Date());
        blogInfo.setBlogDescribe("您好，欢迎访问 pan_junbiao的博客");

        //文档ID（根据业务，我们以博客ID作为文档ID）
        String documentId = String.valueOf(blogInfo.getBlogId());

        //新增文档
        boolean result = blogService.addDocument(_indexName, documentId, blogInfo);
        if(result)
        {
            System.out.println("新增文档成功");
        }
        else
        {
            System.out.println("新增文档失败");
        }
    }

    /**
     * 测试：批量新增文档
     * @author pan_junbiao
     */
    @Test
    public void addBulkDocument()
    {
        //创建博客实体列表
        List<BlogInfo> blogInfoList = new ArrayList<>();
        blogInfoList.add(new BlogInfo(1,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",120.68,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(2,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",85.12,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(3,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",94.37,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(4,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",365.19,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(5,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",287.33,new Date(),"您好，欢迎访问 pan_junbiao的博客"));

        //批量新增文档
        boolean result = blogService.addBulkDocument(_indexName, blogInfoList);
        if(result)
        {
            System.out.println("批量新增文档成功");
        }
        else
        {
            System.out.println("批量新增文档失败");
        }
    }

    /**
     * 测试：修改文档
     * @author pan_junbiao
     */
    @Test
    public void updateDocument()
    {
        //创建博客实体类
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setBlogId(2);
        blogInfo.setBlogName("pan_junbiao的博客_002");
        blogInfo.setBlogUrl("https://blog.csdn.net/pan_junbiao");
        blogInfo.setBlogPoints(120.68);
        blogInfo.setCreateDate(new Date());
        blogInfo.setBlogDescribe("您好，欢迎访问 pan_junbiao的博客_002");

        //文档ID（根据业务，我们以博客ID作为文档ID）
        String documentId = String.valueOf(blogInfo.getBlogId());

        //新增文档
        boolean result = blogService.updateDocument(_indexName, documentId, blogInfo);
        if(result)
        {
            System.out.println("修改文档成功");
        }
        else
        {
            System.out.println("修改文档失败");
        }
    }

    /**
     * 测试：删除文档
     * @author pan_junbiao
     */
    @Test
    public void deleteDocument()
    {
        //删除文档ID为3的文档信息
        String documentId = "3";
        boolean result = blogService.deleteDocument(_indexName, documentId);
        if(result)
        {
            System.out.println("修改文档成功");
        }
        else
        {
            System.out.println("修改文档失败");
        }
    }

    /**
     * 测试：判断文档是否存在
     * @author pan_junbiao
     */
    @Test
    public void existsDocument()
    {
        //判断文档ID为3的文档是否存在
        String documentId = "3";
        boolean result = blogService.existsDocument(_indexName, documentId);
        if(result)
        {
            System.out.println("文档存在");
        }
        else
        {
            System.out.println("文档不存在");
        }
    }

    /**
     * 测试：获取文档
     * @author pan_junbiao
     */
    @Test
    public void getDocumentToBean() throws Exception
    {
        //判断文档ID为1的文档信息
        String documentId = "1";
        BlogInfo blogInfo = blogService.getDocumentToBean(_indexName, documentId,BlogInfo.class);

        //打印信息
        if(blogInfo!=null)
        {
            System.out.println("博客ID：" + blogInfo.getBlogId());
            System.out.println("博客名称：" + blogInfo.getBlogName());
            System.out.println("博客地址：" + blogInfo.getBlogUrl());
            System.out.println("博客积分：" + blogInfo.getBlogPoints());
            System.out.println("创建时间：" + blogInfo.getCreateDate());
            System.out.println("博客描述：" + blogInfo.getBlogDescribe());
        }
    }

    //====================================搜索操作==================================

    /**
     * 测试：批量新增文档
     * @author pan_junbiao
     */
    @Test
    public void addBulkDocument2()
    {
        //创建博客实体列表
        List<BlogInfo> blogInfoList = new ArrayList<>();
        blogInfoList.add(new BlogInfo(1,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",120.68,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(2,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",85.12,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(3,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",94.37,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(4,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",365.19,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(5,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",287.33,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(6,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",355.28,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(7,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",48.13,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(8,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",864.86,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(9,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",685.46,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(10,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",97.75,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(11,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",543.18,new Date(),"您好，欢迎访问 pan_junbiao的博客"));
        blogInfoList.add(new BlogInfo(12,"pan_junbiao的博客","https://blog.csdn.net/pan_junbiao",256.39,new Date(),"您好，欢迎访问 pan_junbiao的博客"));

        //批量新增文档
        boolean result = blogService.addBulkDocument(_indexName, blogInfoList);
        if(result)
        {
            System.out.println("批量新增文档成功");
        }
        else
        {
            System.out.println("批量新增文档失败");
        }
    }

    /**
     * 测试：分页搜索列表，按条件搜索
     * @author pan_junbiao
     */
    @Test
    public void searchBlogListPage()
    {
        //设置搜索条件
        BlogSearchParam blogSearchParam = new BlogSearchParam();
        blogSearchParam.setBlogDescribe("博客");
        blogSearchParam.setIndexName(_indexName);
        blogSearchParam.setPageIndex(1); //获取第1页数据
        blogSearchParam.setPageSize(5); //每页5条数据

        //执行分页搜索列表
        PageResult<BlogInfo> blogInfoPageResult = blogService.searchBlogListPage(blogSearchParam);

        //获取博客列表
        List<BlogInfo> blogList = blogInfoPageResult.getDataList();
        if (blogList != null && blogList.size()>0)
        {
            for(BlogInfo blog : blogList)
            {
                System.out.println("编号：" + blog.getBlogId() +" 名称：" + blog.getBlogName() + " 积分：" + blog.getBlogPoints() + " " + blog.getBlogUrl()+ " " + blog.getBlogDescribe());
            }
        }
        //分页信息
        System.out.println("当前页码：第" + blogInfoPageResult.getPageIndex()+"页");
        System.out.println("分页大小：每页" + blogInfoPageResult.getPageSize()+"条");
        System.out.println("数据总数：共" + blogInfoPageResult.getTotalData()+"条");
        System.out.println("总页数：共" + blogInfoPageResult.getTotalPage()+"页");
    }

    /**
     * 测试：分页搜索列表，按区间范围搜索
     * @author pan_junbiao
     */
    @Test
    public void searchBlogListPageByRange()
    {
        //设置搜索条件
        BlogSearchParam blogSearchParam = new BlogSearchParam();
        blogSearchParam.setBlogDescribe("博客");
        blogSearchParam.setIndexName(_indexName);
        blogSearchParam.setPageIndex(1); //获取第1页数据
        blogSearchParam.setPageSize(5); //每页5条数据

        //设置博客积分区间范围
        blogSearchParam.setPointsBegin(100);
        blogSearchParam.setPointsEnd(500);

        //执行分页搜索列表
        PageResult<BlogInfo> blogInfoPageResult = blogService.searchBlogListPage(blogSearchParam);

        //获取博客列表
        List<BlogInfo> blogList = blogInfoPageResult.getDataList();
        if (blogList != null && blogList.size()>0)
        {
            for(BlogInfo blog : blogList)
            {
                System.out.println("编号：" + blog.getBlogId() +" 名称：" + blog.getBlogName() + " 积分：" + blog.getBlogPoints() + " " + blog.getBlogUrl()+ " " + blog.getBlogDescribe());
            }
        }
        //分页信息
        System.out.println("当前页码：第" + blogInfoPageResult.getPageIndex()+"页");
        System.out.println("分页大小：每页" + blogInfoPageResult.getPageSize()+"条");
        System.out.println("数据总数：共" + blogInfoPageResult.getTotalData()+"条");
        System.out.println("总页数：共" + blogInfoPageResult.getTotalPage()+"页");
    }
}
