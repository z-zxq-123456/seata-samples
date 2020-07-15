package seata.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seata.api.AccountService;

import javax.annotation.Resource;

/**
 * @desc: AccountServiceImpl
 * @author: zhouxqh
 * @create: 2020-07-13 17:20
 **/
@Service
public class AccountServiceImpl implements AccountService {


    private Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void debit(String userId, int money) {

        LOGGER.info("Account Service ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting balance SQL: update account_tbl set money = money - {} where user_id = {}",money,userId);

        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
        LOGGER.info("Account Service End ... ");

    }
}
