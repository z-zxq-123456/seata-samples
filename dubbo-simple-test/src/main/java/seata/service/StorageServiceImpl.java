package seata.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seata.api.StorageService;

import javax.annotation.Resource;

/**
 * @desc: StorageServiceImpl
 * @author: zhouxqh
 * @create: 2020-07-13 17:21
 **/
@Service
public class StorageServiceImpl implements StorageService {

    private Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Resource
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void deduct(String commodityCode, int count) {


        LOGGER.info("Storage Service Begin ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting inventory SQL: update storage_tbl set count = count - {} where commodity_code = {}",
                count, commodityCode);

        jdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?",
                new Object[] {count, commodityCode});
        LOGGER.info("Storage Service End ... ");

    }
}
