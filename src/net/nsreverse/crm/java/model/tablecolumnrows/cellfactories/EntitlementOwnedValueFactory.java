package net.nsreverse.crm.java.model.tablecolumnrows.cellfactories;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import net.nsreverse.crm.java.model.tablecolumnrows.EntitlementRow;

public class EntitlementOwnedValueFactory implements Callback<TableColumn.CellDataFeatures<EntitlementRow, CheckBox>, ObservableValue<CheckBox>> {

    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<EntitlementRow, CheckBox> param) {
        EntitlementRow currentRow = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(currentRow.isEntitled());
        checkBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            checkBox.setSelected(true);
        }));

        return new SimpleObjectProperty<>(checkBox);
    }
}