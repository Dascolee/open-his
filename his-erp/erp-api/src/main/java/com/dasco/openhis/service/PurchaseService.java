package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Purchase;
import com.dasco.openhis.domain.PurchaseItem;
import com.dasco.openhis.domain.SimpleUser;
import com.dasco.openhis.dto.PurchaseDto;
import com.dasco.openhis.dto.PurchaseFormDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【stock_purchase】的数据库操作Service
*/
public interface PurchaseService extends IService<Purchase> {

    DataGridView listPurchaseForPage(PurchaseDto purchaseDto);

    Purchase getPurchaseById(String purchaseId);

    int doAudit(String purchaseId, SimpleUser currentSimpleUser);

    int doInvalid(String purchaseId, SimpleUser currentSimpleUser);

    int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto);

    int addPurchaseAndItem(PurchaseFormDto purchaseFormDto);

    List<PurchaseItem> getPurchaseItemById(String purchaseId);

    int auditPass(String purchaseId);

    int auditNoPass(String purchaseId,String auditMsg);

    int doInventory(String purchaseId, SimpleUser currentSimpleUser);
}
