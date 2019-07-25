package cn.ctoedu.miaosha.mapper;

import cn.ctoedu.miaosha.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xiaomi on 2019/07/22
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id")long id);

    @Insert("insert into user(id, user_name, password, salt, head, register_date, last_login_date, login_count) values (#{id}, #{userName}, " +
            "#{password}, #{salt}, #{head}, #{registerDate}, #{lastLoginDate}, #{loginCount})")
    void insert(User user);

    @Update("update user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);

    @Insert("<script>" +
            "INSERT INTO user(id, user_name, password, salt, head, register_date, last_login_date, login_count) VALUES" +
            "<foreach collection=\"list\" item=\"user\" index=\"index\"  separator=\",\">" +
            "(#{user.id}, #{user.userName}, #{user.password}, #{user.salt}, #{user.head}, #{user.registerDate}, #{user.lastLoginDate}, #{user.loginCount})" +
            "</foreach>" + "</script>")
    void batchAddUsers(@Param("list") List<User> users);

}
