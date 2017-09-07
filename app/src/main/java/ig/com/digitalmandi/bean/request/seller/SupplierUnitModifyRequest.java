package ig.com.digitalmandi.bean.request.seller;

public class SupplierUnitModifyRequest {

    private String unitId;
    private String unitName;
    private String kgValue;
    private String sellerId;
    private String unitStatus;
    private String unitOperation;

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setKgValue(String kgValue) {
        this.kgValue = kgValue;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public void setOperation(String operation) {
        this.unitOperation = operation;
    }
}
