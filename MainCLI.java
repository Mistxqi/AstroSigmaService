import java.util.*;

class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    //user methods here, normal stuff :3
}

class Admin extends User {

    public Admin(String username) {
        super(username);
    }

    //admin methods here, like check expiry, discount, etc.

}

class Auth {
    private HashMap<User, String> logins;
    private User loggedin;

    public Auth() {
    }

    public User getLoggedin() {
        return loggedin;
    }

    public void createUser(){
        while (true){
            System.out.println("enter new Username: ");
            String newUser = In.nextLine();

            System.out.println("enter Password: ");
            String password = In.nextLine();

            for (User u : logins.keySet()){
                if (u.getUsername().equalsIgnoreCase(newUser)){
                    System.out.println("Invalid: User already Exists!");
                    continue;
                }
            }

            System.out.println("Is the acc Admin? (Yes/No): ");
            String isAdmin = In.nextLine();

            User newAcc = isAdmin.equalsIgnoreCase("yes")
                          ? new Admin(newUser)
                          : new User(newUser);

            logins.put(newAcc, password);
            System.out.println("Account created!");
            return;
        }
    }
    
    public void createUser(User user, String password){
        logins.put(user, password);
    }

    public User loginUser() {
        while (true) {
            System.out.println("\n===== LOGIN =====");
            System.out.print("Username: ");
            String usr = In.nextLine();

            System.out.print("Password: ");
            String pass = In.nextLine();

            for (User u : logins.keySet()) {
                if (u.getUsername().equalsIgnoreCase(usr)) {
                    if (logins.get(u).equals(pass)) {
                        loggedin = u;
                        System.out.println("\nLogin successful! Welcome, " + usr);
                        return u;
                    }
                }
            }

            System.out.println("Incorrect login — try again.\n");
        }
    }

    public void delUser(){
        System.out.println("Are you sure? (Yes/No)");
        String choice = In.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.println("Insert Password: ");
            String password = In.nextLine();

            if (logins.get(loggedin).equalsIgnoreCase(password)){
                System.out.println("User has been Deleted :(");
                logins.remove(loggedin);
            }
            
        }
    }

}


public class MainCLI {

    private static ListsProduct store;
    private static ShoppingCart cart = new ShoppingCart();

public static void main(String[] args) {

    HashMap<Products, ItemCategory> initial = new HashMap<>();
    store = new ListsProduct(initial);
    seedProducts(initial);

    // ========== AUTH SETUP ==========
    Auth auth = new Auth();
    auth.createUser(new Admin("root"), "admin123"); // default admin
    auth.createUser(new User("guest"), "guest123"); // default user

    

    // force login before menu
    User logged = auth.loginUser();
    boolean isAdmin = logged instanceof Admin;

    // ========== MAIN LOOP ==========
    while (true) {
        System.out.println("\n===== ASTRO MARKET CLI =====");
        System.out.println("Logged in as: " + logged.getUsername()
                + (isAdmin ? " (Admin)" : " (User)"));

        System.out.println("1. View Products");
        System.out.println("2. Add Product Into Cart");
        System.out.println("3. View Cart");

        if (isAdmin) {
            System.out.println("4. Apply Discount");
            System.out.println("5. Check Stock");
        }

        System.out.println("6. Exit");
        System.out.print("Choose option: ");

        int choice = In.nextInt();
        In.nextLine(); // clear buffer

        switch (choice) {
            case 1 -> viewProducts();
            case 2 -> addToCart();
            case 3 -> cart.viewCart();

            case 4 -> {
                if (!isAdmin) {
                    System.out.println("Unauthorized.");
                    break;
                }
                applyDiscountMenu();
            }

            case 5 -> {
                if (!isAdmin) {
                    System.out.println("Unauthorized.");
                    break;
                }
                checkStockMenu();
            }

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

        product.applyDiscount(disc);

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
