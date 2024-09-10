import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class FurnitureTableModel implements TableModel {

    private List<Furniture> furnitureList;
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

    public FurnitureTableModel (List<Furniture> furnitureList) {
        this.furnitureList = furnitureList;
    }

    @Override
    public int getRowCount() {
        return furnitureList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Артикул";
            case 1:
                return "Описание товара";
            case 2:
                return "Цена";
        }
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Furniture furniture = furnitureList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return furniture.getArticle();
            case 1:
                return furniture.getDescription();
            case 2:
                return furniture.getPrice();
        }

        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
}
