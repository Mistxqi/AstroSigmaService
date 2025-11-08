import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

public class Products implements applyDiscounts{

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

    @Override
    public void applyDiscountFlat(int  discount){
        price -= discount;
    }

    @Override
    public void applyDiscountPerc(int discount) {
        price -= price * (discount / 100f);
    }
    @Override
    public void applyCoupon(Coupon coupon, int quantity){
        int required=0;
        switch (coupon) {
            case BUY1GET1:
                required =1;
                break;
            case BUY2GET1:
                required =2;
                break;
            case BUY3GET1:
                required =3;
                break;
            case BUY4GET1:
                required =4;
                break;
            
            default:
                System.out.println("Invalid.");
                return;
        }

        if (quantity >= required){
            price -= price * (coupon.disc / 100f); 
        }
    }

    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock; 
    }

}

interface checkStock {
    //This one checks of amt in stock
    public int checkInStock();

    //This one checks if it is in stock :D
    public boolean isInStock();
}

interface applyDiscounts{
    //Percentage Discount
    public void applyDiscountPerc(int discount);
    //Flat Discount
    public void applyDiscountFlat(int discount);
    //Coupon
    public void applyCoupon(Coupon coupon, int quantity);
}


class Perishable extends Products implements checkStock{
    
    int[] ExpDate;
    int bad;

    public Perishable(String name, int stock, float price, int[] ExpDate, int bad) {
        super(name, stock, price);
        this.ExpDate = ExpDate;
        this.bad = bad;
    }

    public void changeExp(int[] newExpDate){
        ExpDate = newExpDate;
    }

    public int[] getExp(){
        return ExpDate;
    }

    public void setAmtExp(int amtExp) {
        this.bad = amtExp;
    }

    public int getAmtExp() {
        return bad;
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
    public int checkInStock(){
        System.out.println("what? \n1. Exclude bad Produce \n2. Include bad Produce");
        int choice = In.nextInt();
        while (true){
            switch (choice) {
                case 1:
                    return stock;
                case 2: 
                    return stock-bad;
                default:
                    System.out.println("Proper Output Pls");
                    break;
            }
        }
    }

    @Override
    public boolean isInStock(){
        System.out.println("what? \n1. Exclude bad Produce \n2. Include bad Produce");
        int choice = In.nextInt();
        while (true){
            switch (choice) {
                case 1:
                    return (stock > 0);
                
                case 2: 
                    return ((stock-bad) > 0);

                default:
                    System.out.println("Proper Output Pls");
                    break;
            }
        }
    }

    @Override
    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock+"\nBest Before (DD/MM/YY): " + Arrays.toString(ExpDate); 
    }
}

class NonPerishable extends Products implements checkStock{
    String manufacturer;
    int amtWarehouse;

    public NonPerishable(String name, int stock, float price, String manufacturer, int amtWarehouse) {
        super(name, stock, price);
        this.manufacturer = manufacturer;
        this.amtWarehouse = amtWarehouse;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int checkInStock(){
        System.out.println("where? \n1. In Store \n2. In Warehouse \n3. In total");
        int choice = In.nextInt();
        while (true){
                switch (choice) {
                case 1:
                    return stock;
                case 2: 
                    return amtWarehouse;
                case 3: 
                    return stock+amtWarehouse;
                default:
                System.out.println("Proper Output Pls");
                    break;
            }
        }
    }

    @Override
    public boolean isInStock(){
        if (stock+amtWarehouse > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return name + "Price: $" + price +", Stock : "+ stock+"\nMade By: " + manufacturer; 
    }
}



class ListsProduct {
    HashMap<Products, ItemCategory> productList = new HashMap<>();


    public ListsProduct(HashMap<Products, ItemCategory> productList) {
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

//can someone do this? - Maxmillion
enum Coupon{
    BUY1GET1(0.5),
    BUY2GET1(0.33),
    BUY3GET1(0.25),
    BUY4GET1(0.2);

    public final double disc;

    private Coupon(double disc) {
        this.disc = disc;
    }
}