import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;

public class AppProductList {

    private static ObservableList<Product> productList = FXCollections.observableArrayList();

    static {

        int[] bananasExp = {2025, 12, 31};
        int[] peachExp = {2026, 1, 4};

        int[] meatExp = {2025, 12, 14};

        productList.add(new Perishable("Banana", 10, (float) 3.29, ItemCategory.FRUITS_N_VEGETABLES, bananasExp, 3));
        productList.add(new Perishable("Apple", 10, (float) 2.4, ItemCategory.FRUITS_N_VEGETABLES, bananasExp, 2));
        productList.add(new Perishable("Peach", 10, (float) 1.2, ItemCategory.FRUITS_N_VEGETABLES, peachExp, 1));
        productList.add(new Perishable("Pineapple", 10, (float) 6.7, ItemCategory.FRUITS_N_VEGETABLES, peachExp, 3));

        productList.add(new Product("Canned Beans", 10, (float)2.99, ItemCategory.ASTROBASIC));
        productList.add(new Product("Pasta", 13, (float)2.99, ItemCategory.ASTROBASIC));
        productList.add(new Product("Cereal Box", 15, (float)4.99, ItemCategory.ASTROBASIC));
        productList.add(new Product("Bottled water (12-pack)", 15, (float)3.99, ItemCategory.ASTROBASIC));

        productList.add(new Perishable("Chicken Breasts", 10, (float) 5.99, ItemCategory.MEATS, meatExp, 3));
        productList.add(new Perishable("Fresh Salmon Fillet", 10, (float) 8.49, ItemCategory.MEATS, meatExp, 2));
        productList.add(new Perishable("Ribeye Steak", 10, (float) 10.49, ItemCategory.MEATS, meatExp, 4));
        productList.add(new Perishable("Sirloin Steak", 10, (float) 12.49, ItemCategory.MEATS, meatExp, 2));

        productList.add(new Perishable("Lays", 10, (float) 3.49, ItemCategory.SNACKS, peachExp, 3));
        productList.add(new Perishable("Ritz Crackers", 10, (float) 4.49, ItemCategory.SNACKS, peachExp, 3));
        productList.add(new Perishable("Oreos", 10, (float) 12.49, ItemCategory.SNACKS, peachExp, 3));
        productList.add(new Perishable("Jerky", 10, (float) 9.49, ItemCategory.SNACKS, peachExp, 3));
    }

    public static ObservableList<Product> getProductList() {
        return productList;
    }
}