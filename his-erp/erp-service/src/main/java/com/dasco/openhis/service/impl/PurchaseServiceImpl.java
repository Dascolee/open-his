package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.PurchaseDto;
import com.dasco.openhis.dto.PurchaseFormDto;
import com.dasco.openhis.dto.PurchaseItemDto;
import com.dasco.openhis.mapper.InventoryLogMapper;
import com.dasco.openhis.mapper.MedicinesMapper;
import com.dasco.openhis.mapper.PurchaseItemMapper;
import com.dasco.openhis.mapper.PurchaseMapper;
import com.dasco.openhis.service.PurchaseService;
import com.dasco.openhis.utils.IdGeneratorSnowFlake;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
* @author a
* @description 针对表【stock_purchase】的数据库操作Service实现
*/
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Autowired
    private MedicinesMapper medicinesMapper;


    @Override
    public DataGridView listPurchaseForPage(PurchaseDto purchaseDto) {
        Page<Purchase> page = new Page<>(purchaseDto.getPageNum(), purchaseDto.getPageSize());
        QueryWrapper<Purchase> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()),Purchase.COL_PROVIDER_ID,
                purchaseDto.getProviderId());
        wrapper.eq(StringUtils.isNotBlank(purchaseDto.getStatus()),Purchase.COL_STATUS,
                purchaseDto.getStatus());
        wrapper.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()),Purchase.COL_APPLY_USER_NAME,
                purchaseDto.getApplyUserName());
        wrapper.orderByDesc(Purchase.COL_CREATE_TIME);
        purchaseMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return purchaseMapper.selectById(purchaseId);
    }

    @Override
    public int doAudit(String purchaseId, SimpleUser currentSimpleUser) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        purchase.setApplyUserId(Long.valueOf(currentSimpleUser.getUserId().toString()));
        purchase.setApplyUserName(currentSimpleUser.getUserName());
        return purchaseMapper.updateById(purchase);
    }

    @Override
    public int doInvalid(String purchaseId, SimpleUser currentSimpleUser) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_5);
        return purchaseMapper.updateById(purchase);
    }

    @Override
    public int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if(purchase != null){
            //删除主表数据 stock_purchase
            purchaseMapper.deleteById(purchaseId);
            //删除明细表数据 stock_purchase_item
            QueryWrapper<PurchaseItem> wrapper = new QueryWrapper<>();
            wrapper.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            purchaseItemMapper.delete(wrapper);
        }
        //保存采购单主表数据
        Purchase purchase1 = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),purchase1);
        purchase1.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        purchase1.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        purchase1.setCreateTime(DateUtil.date());
        //设置申请人id和申请人名字
        purchase1.setApplyUserId(Long.valueOf(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserId().toString()));
        purchase1.setApplyUserName(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        int a = purchaseMapper.insert(purchase1);
        //保存采购单明细表
        for (PurchaseItemDto purchaseItemDto : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(purchaseItemDto,purchaseItem);
            purchaseItem.setPurchaseId(purchase1.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowFlake.snowflakeId().toString());
            purchaseItemMapper.insert(purchaseItem);
        }
        return a;
    }

    @Override
    public int addPurchaseAndItem(PurchaseFormDto purchaseFormDto) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if(purchase != null){
            //删除主表数据 stock_purchase
            purchaseMapper.deleteById(purchaseId);
            //删除明细表数据 stock_purchase_item
            QueryWrapper<PurchaseItem> wrapper = new QueryWrapper<>();
            wrapper.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            purchaseItemMapper.delete(wrapper);
        }
        //保存采购单主表数据
        Purchase purchase1 = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),purchase1);
        purchase1.setStatus(Constants.STOCK_PURCHASE_STATUS_1);
        purchase1.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        purchase1.setCreateTime(DateUtil.date());
        int a = purchaseMapper.insert(purchase1);
        //保存采购单明细表
        for (PurchaseItemDto purchaseItemDto : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(purchaseItemDto,purchaseItem);
            purchaseItem.setPurchaseId(purchase1.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowFlake.snowflakeId().toString());
            purchaseItemMapper.insert(purchaseItem);
        }
        return a;
    }

    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if(purchaseId != null){
            QueryWrapper<PurchaseItem> wrapper = new QueryWrapper<>();
            wrapper.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            return purchaseItemMapper.selectList(wrapper);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public int auditPass(String purchaseId) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_3);
        return purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditNoPass(String purchaseId,String auditMsg) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_4);
        purchase.setAuditMsg(auditMsg);
        return purchaseMapper.updateById(purchase);
    }

    @Override
    public int doInventory(String purchaseId, SimpleUser currentSimpleUser) {
        //根据purchaseId查询采购单数据
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        //查询明细数据
        if(purchase != null && purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
        //对明细数据进行遍历，过程中保存到stock_inventory_log表
            QueryWrapper<PurchaseItem> wrapper = new QueryWrapper<>();
            wrapper.eq(PurchaseItem.COL_PURCHASE_ID,purchase.getPurchaseId());
            List<PurchaseItem> items = purchaseItemMapper.selectList(wrapper);
            for (PurchaseItem purchaseItem : items) {
                InventoryLog inventoryLog = new InventoryLog();
                inventoryLog.setInventoryLogId(purchaseItem.getItemId());
                inventoryLog.setPurchaseId(purchaseItem.getPurchaseId());
                inventoryLog.setMedicinesId(purchaseItem.getMedicinesId());
                inventoryLog.setInventoryLogNum(purchaseItem.getPurchaseNumber());
                inventoryLog.setTradePrice(purchaseItem.getTradePrice());
                inventoryLog.setTradeTotalAmount(purchaseItem.getTradeTotalAmount());
                inventoryLog.setBatchNumber(purchaseItem.getBatchNumber());
                inventoryLog.setMedicinesName(purchaseItem.getMedicinesName());
                inventoryLog.setMedicinesType(purchaseItem.getMedicinesType());
                inventoryLog.setPrescriptionType(purchaseItem.getPrescriptionType());
                inventoryLog.setProducterId(purchaseItem.getProducterId());
                inventoryLog.setConversion(purchaseItem.getConversion());
                inventoryLog.setUnit(purchaseItem.getUnit());
                inventoryLog.setCreateTime(DateUtil.date());
                inventoryLog.setCreateBy(currentSimpleUser.getUserName());
                //保存数据
                inventoryLogMapper.insert(inventoryLog);
                //更新对应药品有库存数 stock_medicines
                Medicines medicines = medicinesMapper.selectById(purchaseItem.getMedicinesId());
                medicines.setMedicinesStockNum(medicines.getMedicinesStockNum() + purchaseItem.getPurchaseNumber());
                medicines.setUpdateBy(currentSimpleUser.getUserName());
                medicinesMapper.updateById(medicines);
            }
            //更新采购单状态为入库成功
            purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_6);
            purchase.setStorageOptTime(DateUtil.date());
            purchase.setStorageOptUser(currentSimpleUser.getUserName());
            return purchaseMapper.updateById(purchase);
        }
        return -1;
    }
}




