package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) " +
            "values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "accountId", column = "ACCOUNT_ID"),
            @Result(property = "token", column = "TOKEN"),
            @Result(property = "gmtCreate", column = "GMT_CREATE"),
            @Result(property = "gmtModified", column = "GMT_MODIFIED"),
            @Result(property = "avatarUrl", column = "AVATAR_URL"),
    })
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
