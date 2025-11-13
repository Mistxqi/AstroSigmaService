import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

//Interfaces go Here
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
    //lowk coupons should be another class ngl.
}


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

    public String getName(){
        return this.name;
    }
    
    @Override
    public float getPrice() {
    return getBundlePrice();
    }


    @Override
    public void applyDiscountFlat(int  discount){
        price -= discount;
    }

    @Override
    public void applyDiscountPerc(int discount) {
        price -= price * (discount / 100f);
    }
    // @Override
    // public void applyCoupon(Coupon coupon, int quantity){
    //     int required=0;
    //     if (quantity==coupon.req){
            
    //     }

    //     if (quantity >= required){
    //         price -= price * (coupon.disc / 100f); 
    //     }
    // }

    // public String toString(){
    //     return name + "Price: $" + price +", Stock : "+ stock; 
    // }

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

class BundleBuy extends Products {
    
    int bundleDisc;
    ArrayList<Products> bundle;

    public BundleBuy(String name, int stock, float price, int bundleDisc, ArrayList bundle) {
        super(name, stock, price);
        this.bundleDisc = bundleDisc;
        this.bundle = bundle;
    }

    public float getBundleDisc() {
        return bundleDisc;
    }

    public ArrayList<Products> getBundle() {
        return bundle;
    }

    public void addItem(Products product) {
        bundle.add(product);
    }

    public void setBundleDisc(int bundleDisc) {
        this.bundleDisc = bundleDisc;
    }
    
    public float getBundlePrice(){
    float totalPrice = 0;
    for (Products m : bundle){
        totalPrice += m.getPrice();
    }
    return totalPrice - (totalPrice * (bundleDisc / 100f));
}


    public BundleBuy createBundle(){
        ArrayList<Products> savedItems = new ArrayList<>();
        int amt =0;
            System.out.println("Whats the name of the Bundle");
        while (true){
            System.out.println("How many Items? (Max 4)");
            amt = In.nextInt();
            if (amt > 4) {
                System.out.println("Too Large. Retry");
            }
            break;
        }
        List<Products> allProducts = new ArrayList<>(ListsProduct.getProductList().keySet());
        for (int i = 0; i < amt; i++) {
            int index = 1;
            for (Products m : ListsProduct.getProductList().keySet()){
                System.out.println(index + m.getName());
            }
            int choice = In.nextInt();
            if (choice >= 1 && choice <= allProducts.size()) {
                savedItems.add(allProducts.get(choice-1));
            } else {
                System.out.println("Invalid choice, skipping.");
            }
        }

        System.out.println("Apply Discount? \n 1.yes \n 2.no");
        //Darvell, pls finish this partttttt
        BundleBuy bundle = new BundleBuy("", amt, amt, amt, savedItems);
    }
}

class ListsProduct {
    private static HashMap<Products, ItemCategory> productList = new HashMap<>();


    public ListsProduct(HashMap<Products, ItemCategory> productList) {
        this.productList = productList;
    }

    public static HashMap<Products, ItemCategory> getProductList() {
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
 class ShoppingCart {
    private ArrayList<CartItem> cart;

    public ShoppingCart() {
        this.cart = new ArrayList<>();
    }

    private class CartItem {
        Products product;
        int quantity;
        String notes;
    
        CartItem(Products products, int quantity, String notes) {
            this.product = products;
            this.quantity = quantity;
            this.notes = notes;
        }

        public Products getProduct(){
            return this.product;
        }

        @Override
        public String toString() {
            return product.name + " Quantity: " + quantity + " Price : Rp." + product.getPrice() + " Notes : " + (notes == null ? "None" : notes);

        }
        private void checkForBundle() {
    List<Products> currentProducts = new ArrayList<>();
    for (CartItem item : cart) {
        currentProducts.add(item.product);
    }

    // Check if any bundle matches
    for (BundleBuy bundle : ListsBundles.getBundles()) {
        if (currentProducts.containsAll(bundle.getBundle())) {
            System.out.println("🎉 Bundle Detected: " + bundle.getName() + " applied with " 
                + bundle.getBundleDisc() + "% off!");
            
            // Remove individual items
            cart.removeIf(item -> bundle.getBundle().contains(item.product));
            
            // Add the bundle as a single cart item
            cart.add(new CartItem(bundle, 1, "Bundle Deal"));
            return;
        }
    }
}

    }
    public void addItem(Products product, int quantity, String notes) {
        for (CartItem item: cart) {
            if (item.product.equals(product)) {
                item.quantity += quantity;
                if (notes != null && !notes.isEmpty()) {
                    item.notes = notes;

                }
                System.out.println(product.name + " quantity has been updated to " + item.quantity);
                checkForBundle();
                return;
            }
            
        }
        cart.add(new CartItem(product, quantity, notes));
        System.out.println(product.name + " added to cart.");
        checkForBundle();
    }
    public void removeItem(String productName) {
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            if (item.product.name.equalsIgnoreCase(productName)) {
                cart.remove(i);
                System.out.println(productName + " removed from the cart");
                return;
            }
        }
        System.out.println(productName + " Not found in cart.");
    }
    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("You're cart is empty.");
            return;
        }
        System.out.println( "Total price is Rp."+ getTotalPrice());
    }

    public float getTotalPrice() {
        float total = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            total += item.product.getPrice() * item.quantity;
        }
        return total;
    }

    public void clearCart() {
        cart.clear();
        System.out.println("Cart cleared.");
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


enum GETcoupons{
    BUY1GET1(50, 1),
    BUY2GET1(33,2),
    BUY3GET1(25,3),
    BUY4GET1(20,4);

    public final int disc;
    public final int req;

    private GETcoupons(int disc, int req) {
        this.disc = disc;
        this.req = req;
    }

    public BundleBuy applyCoupon(Products product, int amount){
        int totalPrice = 0;
        ArrayList<Products> bundle = new ArrayList<>();
        int required = this.req;
        if (amount < required) {
            System.out.println("Not enough items");
            return null;
        }
        
        for (int i = 0; i < this.req; i++){
                bundle.add(product);
                totalPrice += product.getPrice();
        }
        BundleBuy bund = new BundleBuy("Bundle: " + product.name, 1 ,totalPrice, this.disc, bundle);
        return bund; 
        //String name, int stock, float price, int bundleDisc, ArrayList bundle)
    
    }

    class ListsBundles {
    private static ArrayList<BundleBuy> bundleList = new ArrayList<>();

    public static void addBundle(BundleBuy bundle) {
        bundleList.add(bundle);
    }

    public static ArrayList<BundleBuy> getBundles() {
        return bundleList;
    }

    // Optional: find bundle by items
    public static BundleBuy findBundleByItems(List<Products> items) {
        for (BundleBuy b : bundleList) {
            if (b.getBundle().containsAll(items) && items.containsAll(b.getBundle())) {
                return b;
            }
        }
        return null;
    }
}

}
