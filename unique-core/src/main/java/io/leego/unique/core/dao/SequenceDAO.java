package io.leego.unique.core.dao;

import io.leego.unique.core.entity.Sequence;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Yihleego
 */
public interface SequenceDAO {

    @Select("select * from ${table}")
    @Results(value = {
            @Result(column = "seq_key", property = "key"),
            @Result(column = "seq_value", property = "value"),
            @Result(column = "seq_increment", property = "increment"),
            @Result(column = "seq_cache", property = "cache"),
            @Result(column = "seq_version", property = "version")})
    List<Sequence> query(@Param("table") String table);

    @Update("update ${table} set seq_value = #{value} where seq_key = #{key} and seq_value < #{value}")
    int updateValue(@Param("key") String key, @Param("value") long value, @Param("table") String table);

    @Insert("insert into ${table} values(#{s.key}, #{s.value}, #{s.increment}, #{s.cache}, #{s.version})")
    int save(@Param("s") Sequence sequence, @Param("table") String table);

    @Update("update ${table} set seq_increment = #{s.increment}, seq_cache = #{s.cache}, seq_version = seq_version + 1 where seq_key = #{s.key}")
    int update(@Param("s") Sequence sequence, @Param("table") String table);

    @Delete("delete from ${table} where seq_key = #{key}")
    int delete(@Param("key") String key, @Param("table") String table);

}
