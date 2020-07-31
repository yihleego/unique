package io.leego.unique.client.autoconfigure;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import io.leego.unique.client.CachedUniqueClient;
import io.leego.unique.client.SimpleUniqueClient;
import io.leego.unique.client.UniqueClient;
import io.leego.unique.client.codec.ResponseErrorDecoder;
import io.leego.unique.client.service.UniqueServiceFeignClient;
import io.leego.unique.mybatis.support.interceptor.AutoSeqInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * @author Yihleego
 */
@Configuration
@EnableFeignClients(basePackages = "io.leego.unique")
@ComponentScan(basePackages = "io.leego.unique")
@ConditionalOnProperty(value = "spring.unique.enabled", matchIfMissing = true)
@EnableConfigurationProperties(UniqueClientProperties.class)
public class UniqueAutoConfiguration {

    @Bean
    public ResponseErrorDecoder responseErrorDecoder() {
        return new ResponseErrorDecoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public UniqueClient uniqueClient(UniqueServiceFeignClient uniqueServiceFeignClient, UniqueClientProperties properties) {
        if (properties.getCache() != null && properties.getCache().isEnabled()) {
            return new CachedUniqueClient(uniqueServiceFeignClient, properties.getCache().getSize(), properties.getTimeout());
        } else {
            return new SimpleUniqueClient(uniqueServiceFeignClient);
        }
    }

    @Configuration
    @EnableHystrix
    @ConditionalOnProperty("spring.unique.hystrix.enabled")
    @EnableConfigurationProperties(UniqueClientProperties.class)
    protected static class UniqueHystrixAutoConfiguration {

        @Bean
        @Scope("prototype")
        @ConditionalOnMissingBean
        public Feign.Builder feignHystrixBuilder() {
            return HystrixFeign.builder();
        }

    }

    @Configuration
    @ConditionalOnClass(AutoSeqInterceptor.class)
    @ConditionalOnBean(SqlSessionFactory.class)
    @ConditionalOnProperty("spring.unique.mybatis.plugin.enabled")
    @EnableConfigurationProperties(UniqueClientProperties.class)
    @AutoConfigureAfter(MybatisAutoConfiguration.class)
    protected static class UniqueMybatisAutoConfiguration {
        @Autowired(required = false)
        private List<SqlSessionFactory> sqlSessionFactoryList;

        @Bean
        public AutoSeqInterceptor autoSeqInterceptor(UniqueClient uniqueClient) {
            AutoSeqInterceptor interceptor = new AutoSeqInterceptor(uniqueClient);
            if (sqlSessionFactoryList != null) {
                for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                    boolean flag = true;
                    List<Interceptor> interceptors = sqlSessionFactory.getConfiguration().getInterceptors();
                    for (Interceptor o : interceptors) {
                        if (o != null && AutoSeqInterceptor.class == o.getClass()) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
                    }
                }
            }
            return interceptor;
        }

    }

}
