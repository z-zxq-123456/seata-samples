package seata.service;


import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import seata.api.AccountService;
import seata.api.OrderService;
import seata.api.StorageService;
import seata.dto.Order;
import javax.annotation.Resource;
import java.sql.PreparedStatement;

/**
 * @desc: OrderServiceImpl
 * @author: zhouxqh
 * @create: 2020-07-13 17:20
 **/
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    AccountService accountService;
    @Resource
    StorageService storageService;

    @Resource
    JdbcTemplate jdbcTemplate;


    @GlobalTransactional
    @Override
    public Order create(String userId, String commodityCode, int orderCount) {


        LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());

        // 计算订单金额
        int orderMoney = 100*orderCount;

        // 从账户余额扣款
        accountService.debit(userId, orderMoney);

        storageService.deduct(commodityCode,orderCount);


        int a = 1/0;

        final Order order = new Order();
        order.userId = userId;
        order.commodityCode = commodityCode;
        order.count = orderCount;
        order.money = orderMoney;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        LOGGER.info("Order Service SQL: insert into order_tbl (user_id, commodity_code, count, money) values ({}, {}, {}, {})" ,userId ,commodityCode ,orderCount ,orderMoney );

        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
                    "insert into order_tbl (user_id, commodity_code, count, money) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setObject(1, order.userId);
            pst.setObject(2, order.commodityCode);
            pst.setObject(3, order.count);
            pst.setObject(4, order.money);
            return pst;
        }, keyHolder);

        order.id = keyHolder.getKey().longValue();

        LOGGER.info("Order Service End ... Created " + order);

        return order;
    }
}
