import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Store store = new Store();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-create an Admin user for testing
        store.addUser(1, "Admin", "admin@gmail.com", "Admin");

        boolean running = true;
        while (running) {
            System.out.println("========================================");
            System.out.println("          ONLINE E-COMMERCE");
            System.out.println("========================================");
            System.out.println("1. Admin");
            System.out.println("2. Seller");
            System.out.println("3. Buyer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    sellerLogin();
                    break;
                case 3:
                    buyerLogin();
                    break;
                case 4:
                    System.out.println("Thank you for using Online E-Commerce!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }

    // ========== LOGIN METHODS ==========
    private static void adminLogin() {
        System.out.print("Enter Admin User ID: ");
        int userId = getIntInput();
        User user = store.findUser(userId);
        if (user != null && user.getRole().equals("Admin")) {
            System.out.println("Welcome, " + user.getName() + "!");
            adminMenu();
        } else {
            System.out.println("Invalid Admin ID!");
        }
    }

    private static void sellerLogin() {
        System.out.print("Enter Seller User ID: ");
        int userId = getIntInput();
        User user = store.findUser(userId);
        if (user != null && user.getRole().equals("Seller")) {
            System.out.println("Welcome, " + user.getName() + "!");
            sellerMenu(userId);
        } else {
            System.out.println("Invalid Seller ID!");
        }
    }

    private static void buyerLogin() {
        System.out.print("Enter Buyer User ID: ");
        int userId = getIntInput();
        User user = store.findUser(userId);
        if (user != null && user.getRole().equals("Buyer")) {
            System.out.println("Welcome, " + user.getName() + "!");
            buyerMenu(userId);
        } else {
            System.out.println("Invalid Buyer ID!");
        }
    }

    // ========== ADMIN MENU ==========
    private static void adminMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("========================================");
            System.out.println("             ADMIN MENU");
            System.out.println("========================================");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. View All Products");
            System.out.println("6. Add Product");
            System.out.println("7. Update Product");
            System.out.println("8. Delete Product");
            System.out.println("9. View All Orders");
            System.out.println("10. Update Order Status");
            System.out.println("11. View System Activity");
            System.out.println("12. Generate Inventory Report");
            System.out.println("13. Generate Sales Report");
            System.out.println("14. Back");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    adminAddUser();
                    break;
                case 2:
                    store.viewUsers();
                    break;
                case 3:
                    adminUpdateUser();
                    break;
                case 4:
                    adminDeleteUser();
                    break;
                case 5:
                    store.viewProducts();
                    break;
                case 6:
                    adminAddProduct();
                    break;
                case 7:
                    adminUpdateProduct();
                    break;
                case 8:
                    adminDeleteProduct();
                    break;
                case 9:
                    store.viewAllOrders();
                    break;
                case 10:
                    adminUpdateOrderStatus();
                    break;
                case 11:
                    store.viewActivityLog();
                    break;
                case 12:
                    store.generateInventoryReport();
                    break;
                case 13:
                    store.generateSalesReport();
                    break;
                case 14:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void adminAddUser() {
        System.out.print("User ID: ");
        int userId = getIntInput();
        scanner.nextLine(); // consume newline
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Role (Admin/Seller/Buyer): ");
        String role = scanner.nextLine();
        store.addUser(userId, name, email, role);
    }

    private static void adminUpdateUser() {
        System.out.print("User ID to update: ");
        int userId = getIntInput();
        scanner.nextLine();
        System.out.print("New Name (press Enter to keep current): ");
        String name = scanner.nextLine();
        System.out.print("New Email (press Enter to keep current): ");
        String email = scanner.nextLine();
        System.out.print("New Role (Admin/Seller/Buyer, press Enter to keep current): ");
        String role = scanner.nextLine();
        store.updateUser(userId, 
            name.isEmpty() ? null : name,
            email.isEmpty() ? null : email,
            role.isEmpty() ? null : role);
    }

    private static void adminDeleteUser() {
        System.out.print("User ID to delete: ");
        int userId = getIntInput();
        store.deleteUser(userId);
    }

    private static void adminAddProduct() {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Price: ");
        double price = getDoubleInput();
        System.out.print("Quantity: ");
        int quantity = getIntInput();
        System.out.print("Seller ID: ");
        int sellerId = getIntInput();
        store.addProduct(productId, name, description, price, quantity, sellerId);
    }

    private static void adminUpdateProduct() {
        System.out.print("Product ID to update: ");
        int productId = getIntInput();
        scanner.nextLine();
        System.out.print("New Name (press Enter to keep current): ");
        String name = scanner.nextLine();
        System.out.print("New Description (press Enter to keep current): ");
        String description = scanner.nextLine();
        System.out.print("New Price (press Enter to keep current): ");
        String priceStr = scanner.nextLine();
        System.out.print("New Quantity (press Enter to keep current): ");
        String qtyStr = scanner.nextLine();
        
        Double price = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
        Integer quantity = qtyStr.isEmpty() ? null : Integer.parseInt(qtyStr);
        
        store.updateProduct(productId, 
            name.isEmpty() ? null : name,
            description.isEmpty() ? null : description,
            price,
            quantity,
            1, "Admin");
    }

    private static void adminDeleteProduct() {
        System.out.print("Product ID to delete: ");
        int productId = getIntInput();
        store.deleteProduct(productId, 1, "Admin");
    }

    private static void adminUpdateOrderStatus() {
        System.out.print("Order ID: ");
        int orderId = getIntInput();
        System.out.println("1. Placed");
        System.out.println("2. Processing");
        System.out.println("3. Shipped");
        System.out.println("4. Delivered");
        System.out.println("5. Cancelled");
        System.out.print("Enter status number: ");
        int statusChoice = getIntInput();
        String[] statuses = {"Placed", "Processing", "Shipped", "Delivered", "Cancelled"};
        if (statusChoice >= 1 && statusChoice <= 5) {
            store.updateOrderStatus(orderId, statuses[statusChoice - 1], 1, "Admin");
        } else {
            System.out.println("Invalid choice!");
        }
    }

    // ========== SELLER MENU ==========
    private static void sellerMenu(int sellerId) {
        boolean back = false;
        while (!back) {
            System.out.println("========================================");
            System.out.println("             SELLER MENU");
            System.out.println("========================================");
            System.out.println("1. Add Product");
            System.out.println("2. View My Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Update Inventory");
            System.out.println("6. View Inventory");
            System.out.println("7. View My Orders");
            System.out.println("8. Update Order Status");
            System.out.println("9. View Sales Report");
            System.out.println("10. Back");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    sellerAddProduct(sellerId);
                    break;
                case 2:
                    store.viewProductsBySeller(sellerId);
                    break;
                case 3:
                    sellerUpdateProduct(sellerId);
                    break;
                case 4:
                    sellerDeleteProduct(sellerId);
                    break;
                case 5:
                    sellerUpdateInventory(sellerId);
                    break;
                case 6:
                    store.viewInventory(sellerId);
                    break;
                case 7:
                    store.viewSellerOrders(sellerId);
                    break;
                case 8:
                    sellerUpdateOrderStatus(sellerId);
                    break;
                case 9:
                    store.generateSellerSalesReport(sellerId);
                    break;
                case 10:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void sellerAddProduct(int sellerId) {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Price: ");
        double price = getDoubleInput();
        System.out.print("Quantity: ");
        int quantity = getIntInput();
        store.addProduct(productId, name, description, price, quantity, sellerId);
    }

    private static void sellerUpdateProduct(int sellerId) {
        System.out.print("Product ID to update: ");
        int productId = getIntInput();
        scanner.nextLine();
        System.out.print("New Name (press Enter to keep current): ");
        String name = scanner.nextLine();
        System.out.print("New Description (press Enter to keep current): ");
        String description = scanner.nextLine();
        System.out.print("New Price (press Enter to keep current): ");
        String priceStr = scanner.nextLine();
        System.out.print("New Quantity (press Enter to keep current): ");
        String qtyStr = scanner.nextLine();
        
        Double price = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
        Integer quantity = qtyStr.isEmpty() ? null : Integer.parseInt(qtyStr);
        
        store.updateProduct(productId, 
            name.isEmpty() ? null : name,
            description.isEmpty() ? null : description,
            price,
            quantity,
            sellerId, "Seller");
    }

    private static void sellerDeleteProduct(int sellerId) {
        System.out.print("Product ID to delete: ");
        int productId = getIntInput();
        store.deleteProduct(productId, sellerId, "Seller");
    }

    private static void sellerUpdateInventory(int sellerId) {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        System.out.print("New Quantity: ");
        int quantity = getIntInput();
        store.updateInventory(productId, quantity, sellerId);
    }

    private static void sellerUpdateOrderStatus(int sellerId) {
        System.out.print("Order ID: ");
        int orderId = getIntInput();
        System.out.println("1. Processing");
        System.out.println("2. Shipped");
        System.out.println("3. Delivered");
        System.out.println("4. Cancelled");
        System.out.print("Enter status number: ");
        int statusChoice = getIntInput();
        String[] statuses = {"Processing", "Shipped", "Delivered", "Cancelled"};
        if (statusChoice >= 1 && statusChoice <= 4) {
            store.updateOrderStatus(orderId, statuses[statusChoice - 1], sellerId, "Seller");
        } else {
            System.out.println("Invalid choice!");
        }
    }

    // ========== BUYER MENU ==========
    private static void buyerMenu(int buyerId) {
        boolean back = false;
        while (!back) {
            System.out.println("========================================");
            System.out.println("             BUYER MENU");
            System.out.println("========================================");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Products");
            System.out.println("3. Purchase Product");
            System.out.println("4. View Order History");
            System.out.println("5. Track Order");
            System.out.println("6. Add Product to Wishlist");
            System.out.println("7. Remove Product from Wishlist");
            System.out.println("8. View Wishlist");
            System.out.println("9. View Profile");
            System.out.println("10. Update Profile");
            System.out.println("11. Back");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    browseProducts();
                    break;
                case 2:
                    searchProducts();
                    break;
                case 3:
                    purchaseProduct(buyerId);
                    break;
                case 4:
                    store.viewBuyerOrders(buyerId);
                    break;
                case 5:
                    trackOrder(buyerId);
                    break;
                case 6:
                    addToWishlist(buyerId);
                    break;
                case 7:
                    removeFromWishlist(buyerId);
                    break;
                case 8:
                    store.viewWishlist(buyerId);
                    break;
                case 9:
                    store.viewProfile(buyerId);
                    break;
                case 10:
                    updateProfile(buyerId);
                    break;
                case 11:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void browseProducts() {
        System.out.println("========== AVAILABLE PRODUCTS ==========");
        boolean found = false;
        for (Product p : store.getProducts()) {
            if (p.getQuantity() > 0) {
                System.out.println(p.toString());
                System.out.println("Available Quantity: " + p.getQuantity());
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No products available.");
        }
    }

    private static void searchProducts() {
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Description");
        System.out.print("Enter choice: ");
        int choice = getIntInput();
        scanner.nextLine();
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        if (choice == 1) {
            store.searchProductsByName(keyword);
        } else if (choice == 2) {
            store.searchProductsByDescription(keyword);
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private static void purchaseProduct(int buyerId) {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        System.out.print("Quantity: ");
        int quantity = getIntInput();
        store.placeOrder(buyerId, productId, quantity);
    }

    private static void trackOrder(int buyerId) {
        System.out.print("Order ID: ");
        int orderId = getIntInput();
        store.trackOrder(orderId, buyerId);
    }

    private static void addToWishlist(int buyerId) {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        store.addToWishlist(buyerId, productId);
    }

    private static void removeFromWishlist(int buyerId) {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        store.removeFromWishlist(buyerId, productId);
    }

    private static void updateProfile(int buyerId) {
        scanner.nextLine();
        System.out.print("New Name (press Enter to keep current): ");
        String name = scanner.nextLine();
        System.out.print("New Email (press Enter to keep current): ");
        String email = scanner.nextLine();
        store.updateProfile(buyerId, name.isEmpty() ? null : name, email.isEmpty() ? null : email);
    }

    // ========== INPUT HELPERS ==========
    private static int getIntInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // clear buffer
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // clear buffer
            return -1;
        }
    }
}