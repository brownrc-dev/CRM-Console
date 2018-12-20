package net.nsreverse.crm.java.model.tablecolumnrows;

import javafx.beans.property.SimpleBooleanProperty;

public class EntitlementRow {
    private SimpleBooleanProperty isEntitled;
    private SimpleBooleanProperty isDisabled;
    private String entitlementName;

    public EntitlementRow(SimpleBooleanProperty isEntitled, SimpleBooleanProperty isDisabled, String entitlementName) {
        this.isEntitled = isEntitled;
        this.isDisabled = isDisabled;
        this.entitlementName = entitlementName;
    }

    public SimpleBooleanProperty isEntitled() {
        return isEntitled;
    }

    public void setEntitled(SimpleBooleanProperty entitled) {
        isEntitled = entitled;
    }

    public String getEntitlementName() {
        return entitlementName;
    }

    public void setEntitlementName(String entitlementName) {
        this.entitlementName = entitlementName;
    }

    public SimpleBooleanProperty isDisabled() {
        return isDisabled;
    }

    public void setDisabled(SimpleBooleanProperty disabled) {
        isDisabled = disabled;
    }
}
