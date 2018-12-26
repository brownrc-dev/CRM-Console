package net.nsreverse.crm.java.model.tablecolumnrows;

public class EntitlementRow {
    private Boolean isEntitled;
    private Boolean isDisabled;
    private String entitlementName;
    private int entitlement;

    public EntitlementRow(boolean isEntitled, boolean isDisabled, String entitlementName, int entitlement) {
        this.isEntitled = isEntitled;
        this.isDisabled = isDisabled;
        this.entitlementName = entitlementName;
        this.entitlement = entitlement;
    }

    public Boolean isEntitled() {
        return isEntitled;
    }

    public void setEntitled(boolean entitled) {
        this.isEntitled = entitled;
    }

    public String getEntitlementName() {
        return entitlementName;
    }

    public void setEntitlementName(String entitlementName) {
        this.entitlementName = entitlementName;
    }

    public Boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        this.isDisabled = disabled;
    }

    public int getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(int entitlement) {
        this.entitlement = entitlement;
    }
}
