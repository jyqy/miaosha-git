package com.gpnu.miaosha.service;

import java.sql.Date;

import com.gpnu.miaosha.exception.GlobalException;
import com.gpnu.miaosha.redis.MiaoshaUserKey;
import com.gpnu.miaosha.redis.RedisService;
import com.gpnu.miaosha.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpnu.miaosha.dao.MiaoshaUserDao;
import com.gpnu.miaosha.domain.MiaoshaUser;
import com.gpnu.miaosha.result.CodeMsg;
import com.gpnu.miaosha.util.MD5Util;
import com.gpnu.miaosha.vo.LoginVo;
import com.gpnu.miaosha.vo.RegisterVo;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
	public static final String COOKIE_NAME_TOKEN = "token";
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;
	public MiaoshaUser getById(Long id) {
		return miaoshaUserDao.getById(id);
	}

	public boolean login(HttpServletResponse response,LoginVo loginVo) {
		if(loginVo == null)
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		String mobile = loginVo.getMobile();
		String password = loginVo.getPassword();
		//验证帐户是否存在
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
		}
		//验证密码
		String dbPass = user.getPassword();
		String dbSalt = user.getSalt();
		String md5Pass = MD5Util.FormPassToDBPass(password, dbSalt);
		if(!md5Pass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		String token = UUIDUtil.uuid();
		addCookie(response,token,user);
		return true;
	}
	public boolean register(RegisterVo registerVo) {
		String mobile = registerVo.getMobile();
		String password = registerVo.getPassword();
		String nickname = registerVo.getNickname();
		String salt = "1a2b3c4d";//改为随机盐
		//验证号码是否存在
		MiaoshaUser dbUser = getById(Long.parseLong(mobile));
		if(dbUser != null) {
			throw new GlobalException(CodeMsg.MOBILE_EXIT);
		}
		//密码加盐
		String md5Pass = MD5Util.FormPassToDBPass(password, salt);
		MiaoshaUser user = new MiaoshaUser();
		user.setId(Long.parseLong(mobile));
		user.setNickname(nickname);
		user.setPassword(md5Pass);
		user.setSalt(salt);
		user.setRegisterDate(new Date(new java.util.Date().getTime()));
		miaoshaUserDao.insert(user);
		return true;
	}

	public MiaoshaUser getByToken(HttpServletResponse response,String token) {
		if(StringUtils.isEmpty(token)){
			return null;
		}else{
			MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
			addCookie(response,token,user);
			return user;
		}
	}
	private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
		redisService.set(MiaoshaUserKey.token,token,user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);

	}
}
