package lhd;

import com.lhd.dubbo.DubboServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DubboServerApplication.class)
public class TestReflect{


    @Test
    public void testReflect() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
       Class clas =  Class.forName("com.lhd.dubbo.service.impl.HelloServiceImpl");

       Object obj = clas.newInstance();
        Method method = clas.getDeclaredMethod("testPrivate");
        method.setAccessible(true);
        Object ret = method.invoke(obj);

        System.out.println("调用方法返回 ： " + ret);
    }
}