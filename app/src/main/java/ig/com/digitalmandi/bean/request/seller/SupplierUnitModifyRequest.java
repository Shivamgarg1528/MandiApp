package ig.com.digitalmandi.bean.request.seller;

public class SupplierUnitModifyRequest {

    private String unitId;
    private String unitName;
    private String kgValue;
    private String sellerId;
    private String unitStatus;
    private String unitOperation;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getKgValue() {
        return kgValue;
    }

    public void setKgValue(String kgValue) {
        this.kgValue = kgValue;
    }

    public String getSellerId() {
        return sellerId;
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

    public String getOperation() {
        return unitOperation;
    }

    public void setOperation(String operation) {
        this.unitOperation = operation;
    }
}
