package io.leego.unique.core.dao;

import io.leego.unique.core.entity.Sequence;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yihleego
 */
public interface SequenceDAO {

    @Select("select * from ${table} where seq_key = #{key}")
    @Results(value = {
            @Result(column = "seq_key", property = "key", javaType = String.class),
            @Result(column = "seq_value", property = "value", javaType = Long.class),
            @Result(column = "seq_increment", property = "increment", javaType = Integer.class),
            @Result(column = "seq_cache", property = "cache", javaType = Integer.class),
            @Result(column = "seq_version", property = "version", javaType = Integer.class),
            @Result(column = "seq_create_time", property = "createTime", javaType = LocalDateTime.class),
            @Result(column = "seq_update_time", property = "updateTime", javaType = LocalDateTime.class)})
    Sequence findByKey(@Param("key") String key, @Param("table") String table);

    @Select("select * from ${table}")
    @Results(value = {
            @Result(column = "seq_key", property = "key", javaType = String.class),
            @Result(column = "seq_value", property = "value", javaType = Long.class),
            @Result(column = "seq_increment", property = "increment", javaType = Integer.class),
            @Result(column = "seq_cache", property = "cache", javaType = Integer.class),
            @Result(column = "seq_version", property = "version", javaType = Integer.class),
            @Result(column = "seq_create_time", property = "createTime", javaType = LocalDateTime.class),
            @Result(column = "seq_update_time", property = "updateTime", javaType = LocalDateTime.class)})
    List<Sequence> findAll(@Param("table") String table);

    @Update("update ${table} set seq_value = #{value} where seq_key = #{key} and seq_value < #{value}")
    int updateValue(@Param("key") String key, @Param("value") long value, @Param("table") String table);

    @Insert("insert into ${table} values(#{s.key}, #{s.value}, #{s.increment}, #{s.cache}, #{s.version}, #{s.createTime}, null)")
    int save(@Param("s") Sequence sequence, @Param("table") String table);

    @Update("update ${table} set seq_increment = #{s.increment}, seq_cache = #{s.cache}, seq_version = seq_version + 1, seq_update_time = #{s.updateTime} where seq_key = #{s.key}")
    int update(@Param("s") Sequence sequence, @Param("table") String table);

    @Delete("delete from ${table} where seq_key = #{key}")
    int delete(@Param("key") String key, @Param("table") String table);

}
