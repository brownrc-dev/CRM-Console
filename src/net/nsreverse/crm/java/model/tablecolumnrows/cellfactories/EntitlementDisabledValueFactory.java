package net.nsreverse.crm.java.model.tablecolumnrows.cellfactories;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import net.nsreverse.crm.java.model.tablecolumnrows.EntitlementRow;

public class EntitlementDisabledValueFactory implements Callback<TableColumn.CellDataFeatures<EntitlementRow, CheckBox>, ObservableValue<CheckBox>> {
    private Delegate delegate;

    public EntitlementDisabledValueFactory(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<EntitlementRow, CheckBox> param) {
        EntitlementRow currentRow = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(currentRow.isDisabled());
        checkBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            currentRow.setDisabled(newValue);
            int entitlement = currentRow.getEntitlement();

            if (entitlement > 10) {
                entitlement = entitlement % 10;
            }
            else {
                entitlement += 10;
            }

            currentRow.setEntitlement(entitlement);

            if (delegate != null) {
                delegate.checkBoxStatusChanged(entitlement);
            }
        }));

        return new SimpleObjectProperty<>(checkBox);
    }

    public interface Delegate {
        void checkBoxStatusChanged(int entitlement);
    }
}