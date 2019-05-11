package com.zhaodesong.Edocumentsystem;

import com.alibaba.druid.support.json.JSONUtils;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.po.Account;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
public class ApplicationTests {
	@Autowired
	private AccountService accountService;

//	@Test
//	public void testGetById() {
//		int id = 1;
//		Account account = accountService.getById(id);
//		Assert.assertEquals(id,account.getId());
//	}



	@Test
	public void testInsert() {
		Account account = new Account();
		account.setMail("test@gmail.com");
		account.setPassword("C4CA4238A0B923820DCC509A6F75849B");
		account.setNickName("测试");
		account.setCreateTime(LocalDateTime.now());
		account.setUpdateTime(LocalDateTime.now());

		int success = accountService.insert(account);

		Assert.assertEquals(1,success);
	}

	@Test
	public void testUpdateById() {
		Account account = new Account();
		account.setId(8);

		int success = accountService.updateById(account);

		Assert.assertEquals(1,success);
	}

	@Test
	public void testDeleteById() {
		int success = accountService.deleteById(8);
		Assert.assertEquals(1,success);
	}
}
