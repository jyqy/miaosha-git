package com.gpnu.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gpnu.miaosha.domain.MiaoshaUser;
import com.gpnu.miaosha.domain.User;

@Mapper
public interface MiaoshaUserDao {
	@Select("select * from miaosha_user where id=#{id }")
	public MiaoshaUser getById(@Param("id")long id);
	@Insert("insert into miaosha_user(id,nickname,password,salt,register_date)values(#{id},#{nickname},#{password},#{salt},#{registerDate})")
	public void insert(MiaoshaUser user);
}
