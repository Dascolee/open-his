package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.DictData;
import com.dasco.openhis.domain.DictType;
import com.dasco.openhis.dto.DictTypeDto;
import com.dasco.openhis.mapper.DictDataMapper;
import com.dasco.openhis.mapper.DictTypeMapper;
import com.dasco.openhis.service.DictTypeService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @author li118
* @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service实现
* @createDate 2023-07-21 21:47:51
*/
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType>
    implements DictTypeService{


    @Resource
    private DictTypeMapper dictTypeMapper;

    @Resource
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public DataGridView listForPage(DictTypeDto dictTypeDto) {
        Page<DictType> page = new Page<>(dictTypeDto.getPageNum(), dictTypeDto.getPageSize());
        QueryWrapper<DictType> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dictTypeDto.getDictName()),
                DictType.COL_DICT_NAME,dictTypeDto.getDictName());
        wrapper.like(StringUtils.isNotBlank(dictTypeDto.getDictType()),
                DictType.COL_DICT_TYPE,dictTypeDto.getDictType());
        wrapper.eq(StringUtils.isNotBlank(dictTypeDto.getStatus()),
                DictType.COL_STATUS,dictTypeDto.getStatus());
        wrapper.ge(dictTypeDto.getBeginTime()!=null,
                DictType.COL_CREATE_TIME,dictTypeDto.getBeginTime());
        wrapper.le(dictTypeDto.getEndTime()!=null,
                DictType.COL_CREATE_TIME,dictTypeDto.getEndTime());
        dictTypeMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public void dictCacheAsync() {
        QueryWrapper<DictType> wrapper = new QueryWrapper<>();
        wrapper.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        List<DictType> dictTypes = dictTypeMapper.selectList(wrapper);
        for (DictType dictType : dictTypes) {
            QueryWrapper<DictData> wrapper1 = new QueryWrapper<>();
            wrapper1.eq(DictData.COL_STATUS,Constants.STATUS_TRUE);
            wrapper1.eq(DictData.COL_DICT_TYPE,dictType.getDictType());
            wrapper1.orderByAsc(DictData.COL_DICT_SORT);
            List<DictData> dataList = dictDataMapper.selectList(wrapper1);
            String jsonString = JSON.toJSONString(dataList);
            ValueOperations<String, String> value = redisTemplate.opsForValue();
            value.set(Constants.DICT_REDIS_PREFIX + dictType.getDictType(),jsonString);
        }
    }

    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId = (dictId == null) ? -1L : dictId;
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        DictType sysDictType = this.dictTypeMapper.selectOne(qw);
        if(null!=sysDictType &&dictId.longValue()!=sysDictType.getDictId().longValue()){
            return true; //说明不存在
        }
        return false; //说明存在
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        DictType dictType=new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        //设置创建者，。创建时间
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        dictType.setCreateTime(DateUtil.date());
        return this.dictTypeMapper.insert(dictType);
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        DictType dictType=new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        //设置修改人
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.dictTypeMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        List<Long> ids= Arrays.asList(dictIds);
        if(null!=ids&&ids.size()>0){
            return this.dictTypeMapper.deleteBatchIds(ids);
        }else{
            return -1;
        }
    }

    @Override
    public DataGridView listAll() {
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.dictTypeMapper.selectList(qw));
    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectById(dictId);
    }
}




