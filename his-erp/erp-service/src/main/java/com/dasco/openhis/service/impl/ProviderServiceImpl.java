package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.Provider;
import com.dasco.openhis.dto.ProviderDto;
import com.dasco.openhis.mapper.ProviderMapper;
import com.dasco.openhis.service.ProviderService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
* @author a
* @description 针对表【stock_provider(供应商信息表)】的数据库操作Service实现
*/
@Service
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {

    @Autowired
    private ProviderMapper providerMapper;


    @Override
    public DataGridView listProviderForPage(ProviderDto providerDto) {
        Page<Provider> page = new Page<>(providerDto.getPageNum(), providerDto.getPageSize());
        QueryWrapper<Provider> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(providerDto.getProviderName()),Provider.COL_PROVIDER_NAME,
                providerDto.getProviderName());
        wrapper.like(StringUtils.isNotBlank(providerDto.getContactName()),Provider.COL_CONTACT_NAME,
                providerDto.getContactName());
        wrapper.and(StringUtils.isNotBlank(providerDto.getContactTel()), new Consumer<QueryWrapper<Provider>>() {
            //(tel like ? or mobile like ?)
            @Override
            public void accept(QueryWrapper<Provider> providerQueryWrapper) {
                providerQueryWrapper.like(Provider.COL_CONTACT_TEL,providerDto.getContactTel()).or()
                        .like(Provider.COL_CONTACT_MOBILE,providerDto.getContactTel());
            }
        });
        wrapper.eq(StringUtils.isNotBlank(providerDto.getStatus()),Provider.COL_STATUS,
                providerDto.getStatus());
        providerMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addProvider(ProviderDto providerDto) {
        Provider provider = new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        provider.setCreateBy(providerDto.getSimpleUser().getUserName());
        provider.setCreateTime(DateUtil.date());
        return providerMapper.insert(provider);
    }

    @Override
    public int updateProvider(ProviderDto providerDto) {
        Provider provider = new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        provider.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return providerMapper.updateById(provider);
    }

    @Override
    public Provider getOne(Long providerId) {
        return providerMapper.selectById(providerId);
    }

    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        List<Long> ids = Arrays.asList(providerIds);
        if(ids != null && ids.size() > 0){
            return providerMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Provider> selectAllProvider() {
        QueryWrapper<Provider> qw=new QueryWrapper<>();
        qw.eq(Provider.COL_STATUS, Constants.STATUS_TRUE);
        return this.providerMapper.selectList(qw);
    }
}




