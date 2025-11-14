import java.util.*;

public class MainCLI {

    private static ListsProduct store;
    private static ShoppingCart cart = new ShoppingCart();

    public static void main(String[] args) {

        HashMap<Products, ItemCategory> initial = new HashMap<>();
        store = new ListsProduct(initial);

        seedProducts(initial);

        while (true) {
            System.out.println("\n===== ASTRO MARKET CLI =====");
            System.out.println("1. View Products");
            System.out.println("2. Add Product Into Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Apply Discount");
            System.out.println("5. Check Stock");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = In.nextInt();

            switch (choice) {
                case 1 -> viewProducts();
                case 2 -> addToCart();
                case 3 -> cart.viewCart();
                case 4 -> applyDiscountMenu();
                case 5 -> checkStockMenu();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void viewProducts() {
        System.out.println("\n--- AVAILABLE PRODUCTS ---");
        int i = 1;
        for (Products p : ListsProduct.getProductList().keySet()) {
            System.out.println(i++ + ". " + p.getName() + "  Rp." + p.getPrice());
        }
    }

    static void addToCart() {
        viewProducts();

        System.out.print("Choose product number: ");
        int choice = In.nextInt();

        List<Products> all = new ArrayList<>(ListsProduct.getProductList().keySet());
        if (choice < 1 || choice > all.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Products selected = all.get(choice - 1);

        System.out.print("Quantity: ");
        int qty = In.nextInt();

        System.out.print("Notes (optional): ");
        String notes = In.nextLine();

        cart.addItem(selected, qty, notes);
    }

    static void applyDiscountMenu() {
        viewProducts();

        System.out.print("Choose product number: ");
        int choice = In.nextInt();

        List<Products> all = new ArrayList<>(ListsProduct.getProductList().keySet());
        if (choice < 1 || choice > all.size()) {
            System.out.println("Invalid product.");
            return;
        }

        Products product = all.get(choice - 1);

        System.out.print("Discount percentage: ");
        int disc = In.nextInt();

        product.applyDiscountPerc(disc);

        System.out.println("New price: Rp." + product.getPrice());
    }

    static void checkStockMenu() {
        viewProducts();

        System.out.print("Choose product: ");
        int choice = In.nextInt();

        List<Products> all = new ArrayList<>(ListsProduct.getProductList().keySet());
        if (choice < 1 || choice > all.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Products p = all.get(choice - 1);

        if (p instanceof checkStock checkable) {
            System.out.println("Stock: " + checkable.checkInStock());
        } else {
            System.out.println("This product type does not support stock checking.");
        }
    }


    static void seedProducts(HashMap<Products, ItemCategory> map) {
        Perishable banana = new Perishable(
                "Banana",
                40,
                8000,
                new int[]{15, 11, 2025},
                3
        );

        NonPerishable noodles = new NonPerishable(
                "Instant Noodles",
                100,
                3500,
                "Indomie",
                300
        );

        map.put(banana, ItemCategory.FRUITS_N_VEGETABLES);
        map.put(noodles, ItemCategory.ASTROBASIC);
    }
}
