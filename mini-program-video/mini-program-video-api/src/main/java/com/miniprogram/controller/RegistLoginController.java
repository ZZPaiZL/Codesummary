package com.miniprogram.controller;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.program.util.MD5Utils;
import com.mini.program.util.MiniJSONResult;
import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.vo.MiniUsersVo;
import com.miniprogram.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 用户注册与登录
 */

@RestController
@Api(value = "用户注册和登录的接口", tags = { "注册和登录的controller" })
public class RegistLoginController extends BasicController {

	@Autowired
	private UserService userService;

	@PostMapping("/regist")
	@ApiOperation(value = "用户注册", notes = "用户注册的接口")
	public MiniJSONResult regist(@RequestBody MiniUsers user) {
		// 1.判断用户名和密码必须不为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return MiniJSONResult.errorMsg("用户名和密码不能为空");
		}

		// 2.判断用户名是否存在
		// String name=user.getUsername();
		boolean isExistUserName = userService.isExistUserName(user.getUsername());

		// 3.保存用户，注册信息

		if (!isExistUserName) {
			user.setNickname(user.getUsername());
			try {
				user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			userService.saveUser(user);
		} else {
			return MiniJSONResult.errorMsg("用户名已存在");
		}
		user.setPassword("");

		MiniUsersVo userVo = setUserRedisSessionToken(user);

		return MiniJSONResult.ok(userVo);

	}

	/**
	 * 方法调用，创建uniqetoken
	 * 
	 * @param user
	 * @return
	 */
	public MiniUsersVo setUserRedisSessionToken(MiniUsers user) {
		String uniqeToken = UUID.randomUUID().toString();
		redis.set("USER_REDIS_SESSION" + ":" + user.getId(), uniqeToken, 1000 * 60);
		MiniUsersVo userVo = new MiniUsersVo();

		BeanUtils.copyProperties(user, userVo);
		userVo.setUniqeToken(uniqeToken);
		return userVo;
	}

	@PostMapping("/login")
	@ApiOperation(value = "用户登录", notes = "用户登录的接口")
	public MiniJSONResult login(@RequestBody MiniUsers user) {
		// 1.判断用户名和密码必须不为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return MiniJSONResult.errorMsg("用户名和密码不能为空");
		}

		// 2.判断用户信息是否正确
		MiniUsers backuser = userService.isExistUser(user);
		// 3.返回数据
		if (backuser != null) {
			backuser.setPassword("");
			MiniUsersVo userVo = setUserRedisSessionToken(backuser);

			return MiniJSONResult.ok(userVo);
		} else {
			return MiniJSONResult.errorMsg("用户账号或密码错误");
		}

	}

	@PostMapping("/logout")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@ApiOperation(value = "用户注销", notes = "用户注销的接口")
	public MiniJSONResult logout(String userId) {
		
		redis.del("USER_REDIS_SESSION" + ":" +userId );
		
		return MiniJSONResult.ok();

	}

}
