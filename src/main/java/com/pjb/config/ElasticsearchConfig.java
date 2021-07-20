package com.pjb.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch配置类
 * @author pan_junbiao
 **/
@Configuration
public class ElasticsearchConfig
{
    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.scheme}")
    private String scheme;

    /**
     * 初始化：高级客户端
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient()
    {
        RestHighLevelClient restHighLevelClient = null;
        try
        {
            RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme));
            restHighLevelClient = new RestHighLevelClient(builder);
            return restHighLevelClient;
        }
        catch (Exception ex)
        {
            System.out.println("初始化Elasticsearch高级客户端失败");
            ex.printStackTrace();
        }
        return restHighLevelClient;
    }
}
