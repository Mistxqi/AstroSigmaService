import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

public class Products{

    float price;
    int stock;
    String name;

    public Products(String name, int stock, float price) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public ItemCategory categorySelect(String choice){
        for (ItemCategory itemCategory : ItemCategory.values()){
            if (choice.equalsIgnoreCase(itemCategory.name())){
                return itemCategory;
            }
        }
        ItemCategory itemCategory = ItemCategory.RECOMMENDATIONS;
        return itemCategory;
    }

    public float getPrice() {
        return price;
    }

    public void applyDiscount(int discount, boolean isPercentage) {
        if (isPercentage){
            price -= price * (discount / 100f);
            if (price < 0) price = 0;
        } else {
            price -= discount;
        }
    }

    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock; 
    }

}

class Perishable extends Products{
    
    int[] ExpDate;

    public Perishable(String name, int stock, float price, int[] ExpDate) {
        super(name, stock, price);
        this.ExpDate = ExpDate;
    }

    public void changeExp(int[] newExpDate){
        ExpDate = newExpDate;
    }

    public int[] getExp(){
        return ExpDate;
    }

    public boolean isExp(int[] expDate){
        LocalDate expiryDate = LocalDate.of(expDate[2], expDate[1], expDate[0]);
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiryDate);
    }

    public void changeExp(){

        while (true){
            YearMonth yearMonth = YearMonth.of(ExpDate[2], ExpDate[1]);
            System.out.println("What do you Want to change?\n1.Day\n2.Month\n3.Year");
            int choice = In.nextInt();
            switch (choice) {
                case 1:
                    while (true){
                        System.out.println("What date?");
                        int dateChoice = In.nextInt();
                        int daysInMonth = yearMonth.lengthOfMonth();
                        if (dateChoice >= 1 && dateChoice <= daysInMonth){
                            ExpDate[0] = dateChoice;
                            return;
                        } else {
                            System.out.println("Out of Bounds..");
                        }
                    }

                case 2:
                    while (true) {
                        System.out.println("What month(Number)?");
                        int dateChoice = In.nextInt();
                        if (dateChoice >= 1 && dateChoice <= 12){
                            ExpDate[1] = dateChoice;
                            return;
                        } else {
                            System.out.println("Out of Bounds..");
                        }
                    }
                case 3:
                    while (true) {
                        System.out.println("What year?");
                        int dateChoice = In.nextInt();
                        int currentYear = Year.now().getValue();
                        if (dateChoice >= currentYear){
                            ExpDate[2] = dateChoice;
                            return;
                        } else {
                            System.out.println("Out of Bounds..");
                        }
                    }

                default:
                    System.out.println("Out of Bounds..");
            }
        }
    }

    @Override
    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock+"\nBest Before (DD/MM/YY): " + Arrays.toString(ExpDate); 
    }
}

class NonPerishable extends Products{
    String manufacturer;

    public NonPerishable(String name, int stock, float price, String manufacturer) {
        super(name, stock, price);
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    @Override
    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock+"\nMade By: " + manufacturer; 
    }
}

class Lists {
    HashMap<Products, ItemCategory> productList = new HashMap<>();

    public Lists(HashMap<Products, ItemCategory> productList) {
        this.productList = productList;
    }

    public HashMap<Products, ItemCategory> getProductList() {
        return productList;
    }

    public ItemCategory findCategory(String name){
        return productList.get(name);
    }

    public void sortCategory(ItemCategory category){
        for (Products a : productList.keySet()){
            if (productList.get(a) == category){
                System.out.println(a.name + " is part of " + category);
            }
        }
    }
}



enum ItemCategory{
    RECOMMENDATIONS,
    FRUITS_N_VEGETABLES,
    ASTROBASIC,
    ASTROGOODS,
    MEATS,
    DAIRY,
    SEAFOOD,
    SNACKS,
    SEASONINGS;
}
