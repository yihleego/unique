package io.leego.unique.client.autoconfigure;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import io.leego.unique.client.CachedUniqueClient;
import io.leego.unique.client.SimpleUniqueClient;
import io.leego.unique.client.UniqueClient;
import io.leego.unique.client.codec.ResponseErrorDecoder;
import io.leego.unique.client.service.UniqueServiceFeignClient;
import io.leego.unique.common.Validation;
import io.leego.unique.common.Validator;
import io.leego.unique.common.exception.SequenceNotFoundException;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Auto configuration for unique-client.
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
    public UniqueClient uniqueClient(UniqueServiceFeignClient uniqueServiceFeignClient, UniqueClientProperties properties, @Autowired(required = false) Validator validator) {
        UniqueClient uniqueClient;
        if (properties.getCache() != null && properties.getCache().isEnabled()) {
            uniqueClient = new CachedUniqueClient(uniqueServiceFeignClient, properties.getCache().getSize(), properties.getTimeout());
        } else {
            uniqueClient = new SimpleUniqueClient(uniqueServiceFeignClient);
        }
        if (properties.getValidation().isEnabled()) {
            Set<String> keys = new HashSet<>();
            if (!properties.getValidation().getKeys().isEmpty()) {
                keys.addAll(properties.getValidation().getKeys());
            }
            if (validator != null) {
                keys.addAll(validator.getKeys());
            }
            if (!keys.isEmpty()) {
                Validation validation = uniqueClient.validateKeys(keys);
                if (!validation.isSuccess()) {
                    throw new SequenceNotFoundException("The sequence(s) " + validation.getAbsentKeys().toString() + " was not found");
                }
            }
        }
        return uniqueClient;
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

        @Bean
        @ConditionalOnMissingBean(AutoSeqInterceptor.class)
        public AutoSeqInterceptor autoSeqInterceptor(UniqueClient uniqueClient, @Autowired(required = false) List<SqlSessionFactory> sqlSessionFactories) {
            AutoSeqInterceptor interceptor = new AutoSeqInterceptor(uniqueClient);
            if (sqlSessionFactories != null) {
                for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
                    List<Interceptor> interceptors = sqlSessionFactory.getConfiguration().getInterceptors();
                    if (isAbsent(interceptors)) {
                        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
                    }
                }
            }
            return interceptor;
        }

        private boolean isAbsent(List<Interceptor> interceptors) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor instanceof AutoSeqInterceptor) {
                    return false;
                }
            }
            return true;
        }

    }

}
