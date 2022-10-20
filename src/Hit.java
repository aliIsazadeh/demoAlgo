public class Hit extends DataModel {

    public double valueNumber;
    public double valuePrice ;
    public double pivotType ;// 1 = support 0 = resistance

    public double getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(double valueNumber) {
        this.valueNumber = valueNumber;
    }

    public double getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(double valuePrice) {
        this.valuePrice = valuePrice;
    }

    public double getPivotType() {
        return pivotType;
    }

    public void setPivotType(double pivotType) {
        this.pivotType = pivotType;
    }
}
