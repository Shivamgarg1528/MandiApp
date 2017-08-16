package ig.com.digitalmandi.beans.response.supplier;

import android.os.Parcel;
import android.os.Parcelable;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class SupplierUnitListRes extends AbstractResponse<SupplierUnitListRes.ResultBean> {

    public static class ResultBean implements Parcelable {
        private String unitId;
        private String unitName;
        private String unitStatus;
        private String kgValue;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.unitId);
            dest.writeString(this.unitName);
            dest.writeString(this.unitStatus);
            dest.writeString(this.kgValue);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.unitId = in.readString();
            this.unitName = in.readString();
            this.unitStatus = in.readString();
            this.kgValue = in.readString();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
