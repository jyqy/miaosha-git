package com.gpnu.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gpnu.miaosha.domain.User;
import com.gpnu.miaosha.redis.RedisService;
import com.gpnu.miaosha.redis.UserKey;
import com.gpnu.miaosha.result.Result;
import com.gpnu.miaosha.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	@RequestMapping("/")
	@ResponseBody
	String Home(){
		return "hello!";
	}
	@RequestMapping("/db/get")
	@ResponseBody
	public Result<User> dbGet(){
		User user = userService.getById(1);
		return Result.success(user);
	}
	@RequestMapping("/db/tx")
	@ResponseBody
	public Result<Boolean> dbTx(){
		userService.tx();
		return Result.success(true);
	}
	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<User> redisGet(){
		User user = redisService.get(UserKey.getById,""+1,User.class);
		return Result.success(user);
	}
	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<User> redisSet(){
		User user = new User(1,"LZW");
		redisService.set(UserKey.getById,""+2,user);
		User user1= redisService.get(UserKey.getById,""+2, User.class);
		return Result.success(user1);
	}
}
