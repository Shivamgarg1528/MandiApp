package ig.com.digitalmandi.utils;

import java.util.List;

import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;

/**
 * Created by shiva on 11/1/2016.
 */

public class ChangePurchaseModel {

    public static void onChangePurchaseModel(List<SupplierPurchaseListRes.ResultBean> dataList){
        for(int index = dataList.size() -1 ; index >=0 ; index--){
            dataList.get(index).setSumOfProductInKg(dataList.get(index).getSumOfProductInKg());
        }
    }
}
