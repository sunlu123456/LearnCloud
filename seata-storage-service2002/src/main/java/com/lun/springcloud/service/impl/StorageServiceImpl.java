package com.lun.springcloud.service.impl;

import com.lun.springcloud.dao.StorageDao;
import com.lun.springcloud.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Resource
    private StorageDao storageDao;

    /**
     * 扣减库存
     */
    @Override
    public void decrease(Long productId, Integer count) {
        LOGGER.info("------->storage-service中扣减库存开始");
        storageDao.decrease(productId,count);
        // 异常测试  出现异常后test数据回滚
        //int i = 1/0;
        LOGGER.info("------->storage-service中扣减库存结束");
    }
}
