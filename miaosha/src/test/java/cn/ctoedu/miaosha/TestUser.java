package cn.ctoedu.miaosha;

import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhd.common.util.CommonUtil;
import com.lhd.common.util.HttpClientUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Created by xiaomi on 2019/07/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestUser {

    Logger logger = LogManager.getLogger(TestUser.class);

    @Resource
    private UserService userService;

    private static final String DEFAULT_PASSWORD = "123456789";

    /**
     * 生成用户 以及token 做压力测试
     */
    @Test
    public void createUser() throws Exception {
        create(500, false);
    }

    private void create(int count, boolean addToDb) throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setLoginCount(1);
            user.setUserName("user" + i);
            user.setRegisterDate(new Date());
            user.setId((18077200000L + i));
            user.setLastLoginDate(new Date());
            user.setSalt(CommonUtil.createRandom(20, false));
            user.setHead("");
            user.setPassword(CommonUtil.md5(CommonUtil.md5(DEFAULT_PASSWORD) + user.getSalt()));
            users.add(user);
        }

        if (addToDb) {
            logger.info("begin insert users.....");
            userService.batchAddUsers(users);
        }

        logger.info("login and create token.....");

        String urlString = "http://localhost:8080/login/do_login";

        //创建文件存储token
        File file = new File("/Users/xiaomi/IdeaProjects/springboot-example/miaosha/src/main/resources/db/tokens.txt");
        if (file.exists()) {
            file.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);

        for (User user : users) {
            Map<String, String> params = new HashMap<>();
            params.put("mobile", user.getId().toString());
            params.put("password", CommonUtil.md5(DEFAULT_PASSWORD));
            String result = HttpClientUtils.doPost(urlString, params);

            JSONObject jo = JSON.parseObject(result);
            String token = jo.getString("data");
            System.out.println("create token : " + token);

            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }

        raf.close();
    }

}
