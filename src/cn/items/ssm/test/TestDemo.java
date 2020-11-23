package cn.items.ssm.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class TestDemo {
	@Test
	public void test1() {
		Md5Hash md = new Md5Hash("123","eteokues",2);
		System.out.println(md);
	}
}
