package cn.items.ssm.realm;


import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.items.ssm.po.ActiveUser;
import cn.items.ssm.po.SysPermission;
import cn.items.ssm.po.SysUser;
import cn.items.ssm.service.SysService;

public class CustomerRealm extends AuthorizingRealm{
	@Autowired
	private SysService sysService;
	
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		super.setName("自定义realm");
	}
	

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		
		// 用户输入的
	String username = (String) token.getPrincipal();
		
	
	
	
	
	// 模拟出数据库的信息
	
	
	SysUser user = null;
	
	List<SysPermission> menu = null;
	// 查詢数据库
	try {
		
		 user = this.sysService.findSysUserByUserCode(username);
		 
		 menu = this.sysService.findMenuListByUserId(user.getId());
	} catch (Exception e) {
		
		e.printStackTrace();
	}
	if(user==null)
	{
		// 账号不存在
		return null;
	}
	
	// 将用户信息封装进去
	ActiveUser activeUser = new ActiveUser();
	activeUser.setUserid(user.getId());
	activeUser.setUsercode(user.getUsercode());
	activeUser.setUsername(user.getUsername());
	activeUser.setMenus(menu);
	
	
	// 取出密码
	String password_db = user.getPassword();
	
	
	// 取出盐
	String salt_db = user.getSalt();
	
	SimpleAuthenticationInfo authenticationInfo =
			new SimpleAuthenticationInfo(activeUser, password_db,ByteSource.Util.bytes(salt_db),this.getName());
	
		return authenticationInfo;
	}
	
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection Principal) {
		ActiveUser activeUser = (ActiveUser) Principal.getPrimaryPrincipal();
		
		List<SysPermission> list = null;
		try {
			list = this.sysService.findPermissionListByUserId(activeUser.getUserid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> list2 = new ArrayList<String>();
		
		for (SysPermission sysPermission : list) {
			list2.add(sysPermission.getPercode());
		}
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		authorizationInfo.addStringPermissions(list2);
		
		return authorizationInfo;
	}


}
