package com.gpnu.miaosha.controller;


import com.gpnu.miaosha.domain.MiaoshaUser;
import com.gpnu.miaosha.redis.RedisService;
import com.gpnu.miaosha.result.Result;
import com.gpnu.miaosha.service.MiaoshaUserService;
import com.gpnu.miaosha.vo.LoginVo;
import com.gpnu.miaosha.vo.RegisterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	MiaoshaUserService userService;
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/to_list")
	public String toList(Model model,MiaoshaUser user) {
		model.addAttribute("user",user);
		return "goods_list";
	}

}
