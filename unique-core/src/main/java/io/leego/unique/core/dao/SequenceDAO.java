package io.leego.unique.core.dao;

import io.leego.unique.core.constant.Constants;
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
            @Result(column = Constants.Jdbc.KEY, property = Constants.Property.KEY, javaType = String.class),
            @Result(column = Constants.Jdbc.VALUE, property = Constants.Property.VALUE, javaType = Long.class),
            @Result(column = Constants.Jdbc.INCREMENT, property = Constants.Property.INCREMENT, javaType = Integer.class),
            @Result(column = Constants.Jdbc.CACHE, property = Constants.Property.CACHE, javaType = Integer.class),
            @Result(column = Constants.Jdbc.VERSION, property = Constants.Property.VERSION, javaType = Integer.class),
            @Result(column = Constants.Jdbc.CREATE_TIME, property = Constants.Property.CREATE_TIME, javaType = LocalDateTime.class),
            @Result(column = Constants.Jdbc.UPDATE_TIME, property = Constants.Property.UPDATE_TIME, javaType = LocalDateTime.class)})
    Sequence findByKey(@Param("key") String key, @Param("table") String table);

    @Select("select * from ${table}")
    @Results(value = {
            @Result(column = Constants.Jdbc.KEY, property = Constants.Property.KEY, javaType = String.class),
            @Result(column = Constants.Jdbc.VALUE, property = Constants.Property.VALUE, javaType = Long.class),
            @Result(column = Constants.Jdbc.INCREMENT, property = Constants.Property.INCREMENT, javaType = Integer.class),
            @Result(column = Constants.Jdbc.CACHE, property = Constants.Property.CACHE, javaType = Integer.class),
            @Result(column = Constants.Jdbc.VERSION, property = Constants.Property.VERSION, javaType = Integer.class),
            @Result(column = Constants.Jdbc.CREATE_TIME, property = Constants.Property.CREATE_TIME, javaType = LocalDateTime.class),
            @Result(column = Constants.Jdbc.UPDATE_TIME, property = Constants.Property.UPDATE_TIME, javaType = LocalDateTime.class)})
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
