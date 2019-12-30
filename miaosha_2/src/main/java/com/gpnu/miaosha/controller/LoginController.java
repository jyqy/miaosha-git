package com.gpnu.miaosha.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.gpnu.miaosha.redis.RedisService;
import com.gpnu.miaosha.result.CodeMsg;
import com.gpnu.miaosha.result.Result;
import com.gpnu.miaosha.service.MiaoshaUserService;
import com.gpnu.miaosha.util.ValidateUtil;
import com.gpnu.miaosha.vo.LoginVo;
import com.gpnu.miaosha.vo.RegisterVo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	MiaoshaUserService miaoshaUserService;
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/to_login")
	public String toLogin() {
		return "login";
	}
	@RequestMapping("/to_register")
	public String toRegister() {
		return "register";
	}
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
		miaoshaUserService.login(response,loginVo);
		return Result.success(true);
	}
	@RequestMapping("/do_register")
	@ResponseBody
	public Result<Boolean> doRegister(@Valid RegisterVo registerVo) {
		//将所有校验交给service 异常信息交给ExceptionHandler
		miaoshaUserService.register(registerVo);
		return Result.success(true);
	}
}
