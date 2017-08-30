package ig.com.digitalmandi.beans.response.supplier;

import java.io.Serializable;

import ig.com.digitalmandi.base_package.AbstractResponse;

public class SellerUnitList extends AbstractResponse<SellerUnitList.Unit> {

    public static class Unit implements Serializable {

        private String unitId;
        private String unitName;
        private String unitStatus;
        private String kgValue;

        // locally added
        private boolean hide;

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

        public String getUnitStatus() {
            return unitStatus;
        }

        public void setUnitStatus(String unitStatus) {
            this.unitStatus = unitStatus;
        }

        public String getKgValue() {
            return kgValue;
        }

        public void setKgValue(String kgValue) {
            this.kgValue = kgValue;
        }

        public boolean isHide() {
            return hide;
        }

        public void setHide(boolean hide) {
            this.hide = hide;
        }
    }
}
