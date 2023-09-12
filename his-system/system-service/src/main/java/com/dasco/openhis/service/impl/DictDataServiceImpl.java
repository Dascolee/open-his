package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.DictData;
import com.dasco.openhis.dto.DictDataDto;
import com.dasco.openhis.mapper.DictDataMapper;
import com.dasco.openhis.service.DictDataService;
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
* @description 针对表【sys_dict_data(字典数据表)】的数据库操作Service实现
* @createDate 2023-07-21 21:47:51
*/
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData>
    implements DictDataService{

    @Resource
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public DataGridView listForPage(DictDataDto dictDataDto) {
        Page<DictData> page = new Page<>(dictDataDto.getPageNum(), dictDataDto.getPageSize());
        QueryWrapper<DictData> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(dictDataDto.getDictType()),
                DictData.COL_DICT_TYPE,dictDataDto.getDictType());
        wrapper.like(StringUtils.isNotBlank(dictDataDto.getDictLabel()),
                DictData.COL_DICT_LABEL,dictDataDto.getDictLabel());
        wrapper.eq(StringUtils.isNotBlank(dictDataDto.getStatus()),
                DictData.COL_STATUS,dictDataDto.getStatus());
        dictDataMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }


    @Override
    public DictData selectDictDataById(Long dictId) {
        return dictDataMapper.selectById(dictId);
    }

    @Override
    public int insert(DictDataDto dictDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        dictData.setCreateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setCreateTime(DateUtil.date());
        return dictDataMapper.insert(dictData);
    }

    @Override
    public int update(DictDataDto dictDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        dictData.setUpdateBy(dictDataDto.getSimpleUser().getUserName());
        return dictDataMapper.updateById(dictData);
    }

    @Override
    public int deleteDictData(Long[] dictIds) {
        List<Long> ids = Arrays.asList(dictIds);
        if(ids != null && ids.size() > 0){
            return dictDataMapper.deleteBatchIds(ids);
        }else{
            return 0;
        }
    }

    /**
     * 之前是从数据库里面查询
     * 因为我们做到redis的缓存，所以 现在要去redis里面去查询
     * @param dictType
     * @return
     */
    @Override
    public List<DictData> selectDictDataByDictType(String dictType) {
        String key= Constants.DICT_REDIS_PREFIX + dictType;
        //dict:sys_normal_disable
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String json = opsForValue.get(key);
        List<DictData> dictDatas= JSON.parseArray(json,DictData.class);
        return dictDatas;
    }
}




