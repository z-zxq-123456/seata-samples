package seata;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import seata.api.OrderService;

/**
 * @desc: Demo1  测试seata1 进程内部服务
 * @author: zhouxqh
 * @create: 2020-07-13 17:16
 **/
public class Demo1 {


    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:beans.xml"});
        OrderService orderService = (OrderService)context.getBean("orderService");
        orderService.create("zhouxqh","101",1);


        while (true);
    }
}
