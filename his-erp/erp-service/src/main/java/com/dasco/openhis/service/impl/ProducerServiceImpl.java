package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.Producer;
import com.dasco.openhis.dto.ProducerDto;
import com.dasco.openhis.mapper.ProducerMapper;
import com.dasco.openhis.service.ProducerService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【stock_producer(生产厂家表)】的数据库操作Service实现
*/
@Service
public class ProducerServiceImpl extends ServiceImpl<ProducerMapper, Producer> implements ProducerService {

    @Autowired
    private ProducerMapper producerMapper;


    @Override
    public DataGridView listProducerForPage(ProducerDto producerDto) {
        Page<Producer> page = new Page<>(producerDto.getPageNum(), producerDto.getPageSize());
        QueryWrapper<Producer> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(producerDto.getProducterName()), Producer.COL_PRODUCTER_NAME,
                producerDto.getProducterName());
        wrapper.like(StringUtils.isNotBlank(producerDto.getKeywords()), Producer.COL_KEYWORDS,
                producerDto.getKeywords());
        wrapper.like(StringUtils.isNotBlank(producerDto.getProducterTel()), Producer.COL_PRODUCTER_TEL,
                producerDto.getProducterTel());
        wrapper.eq(StringUtils.isNotBlank(producerDto.getStatus()), Producer.COL_STATUS,
                producerDto.getStatus());
        wrapper.ge(producerDto.getBeginTime() != null, Producer.COL_CREATE_TIME,
                producerDto.getBeginTime());
        wrapper.le(producerDto.getEndTime() != null, Producer.COL_CREATE_TIME,
                producerDto.getEndTime());
        producerMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addProducer(ProducerDto producerDto) {
        Producer producer = new Producer();
        BeanUtil.copyProperties(producerDto,producer);
        producer.setCreateBy(producerDto.getSimpleUser().getUserName());
        producer.setCreateTime(DateUtil.date());
        return producerMapper.insert(producer);
    }

    @Override
    public int updateProducer(ProducerDto producerDto) {
        Producer producer = new Producer();
        BeanUtil.copyProperties(producerDto,producer);
        producer.setUpdateBy(producerDto.getSimpleUser().getUserName());
        return producerMapper.updateById(producer);
    }

    @Override
    public Producer getOne(Long producerId) {
        return producerMapper.selectById(producerId);
    }

    @Override
    public int deleteProducterByIds(Long[] producerIds) {
        List<Long> ids = Arrays.asList(producerIds);
        if(ids != null && ids.size() > 0){
            return producerMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Producer> selectAllProducter() {
        QueryWrapper<Producer> qw=new QueryWrapper<>();
        qw.eq(Producer.COL_STATUS, Constants.STATUS_TRUE);
        return this.producerMapper.selectList(qw);
    }
}




