import java.util.ArrayList;
import java.util.Scanner;

public class Store {
    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<String> orders;
    private ArrayList<String> wishlist;
    private ArrayList<String> activityLog;
    private int orderCounter;

    public Store() {
        this.users = new ArrayList<>();
        this.products = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.wishlist = new ArrayList<>();
        this.activityLog = new ArrayList<>();
        this.orderCounter = 1000;
    }

    // ========== USER MANAGEMENT ==========
    public boolean addUser(int userId, String name, String email, String role) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty!");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email format!");
            return false;
        }
        if (!role.equals("Admin") && !role.equals("Seller") && !role.equals("Buyer")) {
            System.out.println("Invalid role! Must be Admin, Seller, or Buyer.");
            return false;
        }
        for (User u : users) {
            if (u.getUserId() == userId) {
                System.out.println("User ID already exists!");
                return false;
            }
        }
        User newUser = new User(userId, name, email, role);
        users.add(newUser);
        activityLog.add("Admin added User " + userId);
        System.out.println("User added successfully!");
        return true;
    }

    public void viewUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User u : users) {
            System.out.println(u.toString());
            System.out.println("--------------------");
        }
    }

    public boolean updateUser(int userId, String newName, String newEmail, String newRole) {
        for (User u : users) {
            if (u.getUserId() == userId) {
                if (newName != null && !newName.trim().isEmpty()) {
                    u.setName(newName);
                }
                if (newEmail != null && !newEmail.trim().isEmpty()) {
                    if (newEmail.contains("@")) {
                        u.setEmail(newEmail);
                    } else {
                        System.out.println("Invalid email format!");
                        return false;
                    }
                }
                if (newRole != null && !newRole.trim().isEmpty()) {
                    if (newRole.equals("Admin") || newRole.equals("Seller") || newRole.equals("Buyer")) {
                        u.setRole(newRole);
                    } else {
                        System.out.println("Invalid role!");
                        return false;
                    }
                }
                activityLog.add("Admin updated User " + userId);
                System.out.println("User updated successfully!");
                return true;
            }
        }
        System.out.println("User not found!");
        return false;
    }

    public boolean deleteUser(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                // Check if user has products
                String role = users.get(i).getRole();
                if (role.equals("Seller")) {
                    for (Product p : products) {
                        if (p.getSellerId() == userId) {
                            System.out.println("Cannot delete seller with existing products!");
                            return false;
                        }
                    }
                }
                // Check if user has orders
                for (String order : orders) {
                    String[] parts = order.split("\\|");
                    if (parts.length >= 2 && Integer.parseInt(parts[1]) == userId) {
                        System.out.println("Cannot delete user with existing orders!");
                        return false;
                    }
                }
                users.remove(i);
                activityLog.add("Admin deleted User " + userId);
                System.out.println("User deleted successfully!");
                return true;
            }
        }
        System.out.println("User not found!");
        return false;
    }

    public User findUser(int userId) {
        for (User u : users) {
            if (u.getUserId() == userId) {
                return u;
            }
        }
        return null;
    }

    public boolean userExists(int userId, String role) {
        for (User u : users) {
            if (u.getUserId() == userId && u.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    // ========== PRODUCT MANAGEMENT ==========
    public boolean addProduct(int productId, String name, String description, double price, int quantity, int sellerId) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Product name cannot be empty!");
            return false;
        }
        if (price < 0) {
            System.out.println("Price cannot be negative!");
            return false;
        }
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return false;
        }
        if (!userExists(sellerId, "Seller")) {
            System.out.println("Seller does not exist!");
            return false;
        }
        for (Product p : products) {
            if (p.getProductId() == productId) {
                System.out.println("Product ID already exists!");
                return false;
            }
        }
        Product newProduct = new Product(productId, name, description, price, quantity, sellerId);
        products.add(newProduct);
        activityLog.add("Seller " + sellerId + " added Product " + productId);
        System.out.println("Product added successfully!");
        return true;
    }

    public void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product p : products) {
            System.out.println(p.toString());
            System.out.println("----------------------------");
        }
    }

    public void viewProductsBySeller(int sellerId) {
        boolean found = false;
        for (Product p : products) {
            if (p.getSellerId() == sellerId) {
                System.out.println(p.toString());
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No products found for this seller.");
        }
    }

    public void searchProductsByName(String keyword) {
        boolean found = false;
        keyword = keyword.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword)) {
                System.out.println(p.toString());
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching products found.");
        }
    }

    public void searchProductsByDescription(String keyword) {
        boolean found = false;
        keyword = keyword.toLowerCase();
        for (Product p : products) {
            if (p.getDescription().toLowerCase().contains(keyword)) {
                System.out.println(p.toString());
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching products found.");
        }
    }

    public boolean updateProduct(int productId, String newName, String newDescription, Double newPrice, Integer newQuantity, int requesterId, String requesterRole) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                if (requesterRole.equals("Seller") && p.getSellerId() != requesterId) {
                    System.out.println("You can only update your own products!");
                    return false;
                }
                if (newName != null && !newName.trim().isEmpty()) {
                    p.setName(newName);
                }
                if (newDescription != null && !newDescription.trim().isEmpty()) {
                    p.setDescription(newDescription);
                }
                if (newPrice != null) {
                    if (newPrice >= 0) {
                        p.setPrice(newPrice);
                    } else {
                        System.out.println("Price cannot be negative!");
                        return false;
                    }
                }
                if (newQuantity != null) {
                    if (newQuantity >= 0) {
                        p.setQuantity(newQuantity);
                    } else {
                        System.out.println("Quantity cannot be negative!");
                        return false;
                    }
                }
                activityLog.add(requesterRole + " " + requesterId + " updated Product " + productId);
                System.out.println("Product updated successfully!");
                return true;
            }
        }
        System.out.println("Product not found!");
        return false;
    }

    public boolean deleteProduct(int productId, int requesterId, String requesterRole) {
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getProductId() == productId) {
                if (requesterRole.equals("Seller") && p.getSellerId() != requesterId) {
                    System.out.println("You can only delete your own products!");
                    return false;
                }
                // Check if product has active orders
                for (String order : orders) {
                    String[] parts = order.split("\\|");
                    if (parts.length >= 4 && Integer.parseInt(parts[3]) == productId) {
                        String status = parts[6];
                        if (!status.equals("Delivered") && !status.equals("Cancelled")) {
                            System.out.println("Cannot delete product with active orders!");
                            return false;
                        }
                    }
                }
                products.remove(i);
                activityLog.add(requesterRole + " " + requesterId + " deleted Product " + productId);
                System.out.println("Product deleted successfully!");
                return true;
            }
        }
        System.out.println("Product not found!");
        return false;
    }

    public Product findProduct(int productId) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }

    // ========== INVENTORY ==========
    public boolean updateInventory(int productId, int newQuantity, int sellerId) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                if (p.getSellerId() != sellerId) {
                    System.out.println("You can only update inventory for your own products!");
                    return false;
                }
                if (newQuantity < 0) {
                    System.out.println("Quantity cannot be negative!");
                    return false;
                }
                p.setQuantity(newQuantity);
                activityLog.add("Seller " + sellerId + " updated inventory for Product " + productId + " to " + newQuantity);
                System.out.println("Inventory updated successfully!");
                return true;
            }
        }
        System.out.println("Product not found!");
        return false;
    }

    public void viewInventory(int sellerId) {
        boolean found = false;
        for (Product p : products) {
            if (p.getSellerId() == sellerId) {
                System.out.println("Product ID: " + p.getProductId() + " | Name: " + p.getName() + " | Quantity: " + p.getQuantity());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No products found.");
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    // ========== ORDERS ==========
    public boolean placeOrder(int buyerId, int productId, int quantity) {
        User buyer = findUser(buyerId);
        if (buyer == null || !buyer.getRole().equals("Buyer")) {
            System.out.println("Invalid buyer!");
            return false;
        }

        Product product = findProduct(productId);
        if (product == null) {
            System.out.println("Product not found!");
            return false;
        }

        if (product.getQuantity() <= 0) {
            System.out.println("Product out of stock!");
            return false;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return false;
        }

        if (quantity > product.getQuantity()) {
            System.out.println("Insufficient stock! Available: " + product.getQuantity());
            return false;
        }

        product.decreaseInventory(quantity);
        double total = product.getPrice() * quantity;
        orderCounter++;
        String orderId = String.valueOf(orderCounter);
        String order = orderId + "|" + buyerId + "|" + product.getSellerId() + "|" + productId + "|" + quantity + "|" + total + "|Placed";
        orders.add(order);
        activityLog.add("Buyer " + buyerId + " purchased Product " + productId);
        System.out.println("==================================");
        System.out.println("        ORDER CONFIRMED");
        System.out.println("==================================");
        System.out.println("Order ID: " + orderId);
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: ₹" + product.getPrice());
        System.out.println("Total: ₹" + total);
        System.out.println("Order Status: Placed");
        System.out.println("Purchase successful!");
        return true;
    }

    public void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        System.out.println("========== ALL ORDERS ==========");
        for (String order : orders) {
            displayOrderDetails(order);
        }
    }

    public void viewBuyerOrders(int buyerId) {
        boolean found = false;
        System.out.println("========== ORDER HISTORY ==========");
        for (String order : orders) {
            String[] parts = order.split("\\|");
            if (parts.length >= 7 && Integer.parseInt(parts[1]) == buyerId) {
                displayOrderDetails(order);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders found.");
        }
    }

    public void viewSellerOrders(int sellerId) {
        boolean found = false;
        System.out.println("========== MY ORDERS ==========");
        for (String order : orders) {
            String[] parts = order.split("\\|");
            if (parts.length >= 7 && Integer.parseInt(parts[2]) == sellerId) {
                displayOrderDetails(order);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders found.");
        }
    }

    public void trackOrder(int orderId, int buyerId) {
        for (String order : orders) {
            String[] parts = order.split("\\|");
            if (parts.length >= 7 && Integer.parseInt(parts[0]) == orderId) {
                if (Integer.parseInt(parts[1]) != buyerId) {
                    System.out.println("You can only track your own orders!");
                    return;
                }
                System.out.println("========== ORDER STATUS ==========");
                displayOrderDetails(order);
                return;
            }
        }
        System.out.println("Order not found!");
    }

    public boolean updateOrderStatus(int orderId, String newStatus, int requesterId, String requesterRole) {
        for (int i = 0; i < orders.size(); i++) {
            String order = orders.get(i);
            String[] parts = order.split("\\|");
            if (parts.length >= 7 && Integer.parseInt(parts[0]) == orderId) {
                if (requesterRole.equals("Seller") && Integer.parseInt(parts[2]) != requesterId) {
                    System.out.println("You can only update orders for your own products!");
                    return false;
                }
                if (!isValidStatus(newStatus)) {
                    System.out.println("Invalid status! Valid: Placed, Processing, Shipped, Delivered, Cancelled");
                    return false;
                }
                parts[6] = newStatus;
                orders.set(i, String.join("|", parts));
                activityLog.add(requesterRole + " " + requesterId + " updated Order " + orderId + " to " + newStatus);
                System.out.println("Order status updated successfully!");
                return true;
            }
        }
        System.out.println("Order not found!");
        return false;
    }

    private boolean isValidStatus(String status) {
        return status.equals("Placed") || status.equals("Processing") || 
               status.equals("Shipped") || status.equals("Delivered") || status.equals("Cancelled");
    }

    private void displayOrderDetails(String order) {
        String[] parts = order.split("\\|");
        if (parts.length >= 7) {
            Product p = findProduct(Integer.parseInt(parts[3]));
            String productName = (p != null) ? p.getName() : "Unknown";
            System.out.println("Order ID: " + parts[0]);
            System.out.println("Product: " + productName);
            System.out.println("Quantity: " + parts[4]);
            System.out.println("Total: ₹" + parts[5]);
            System.out.println("Status: " + parts[6]);
            System.out.println("----------------------------");
        }
    }

    // ========== WISHLIST ==========
    public void addToWishlist(int buyerId, int productId) {
        Product p = findProduct(productId);
        if (p == null) {
            System.out.println("Product not found!");
            return;
        }
        String item = buyerId + "|" + productId;
        for (String w : wishlist) {
            if (w.equals(item)) {
                System.out.println("Product already in wishlist!");
                return;
            }
        }
        wishlist.add(item);
        System.out.println("Added to wishlist!");
    }

    public void removeFromWishlist(int buyerId, int productId) {
        String item = buyerId + "|" + productId;
        if (wishlist.remove(item)) {
            System.out.println("Removed from wishlist!");
        } else {
            System.out.println("Product not in wishlist!");
        }
    }

    public void viewWishlist(int buyerId) {
        System.out.println("========== WISHLIST ==========");
        boolean found = false;
        for (String w : wishlist) {
            String[] parts = w.split("\\|");
            if (parts.length >= 2 && Integer.parseInt(parts[0]) == buyerId) {
                int pid = Integer.parseInt(parts[1]);
                Product p = findProduct(pid);
                if (p != null) {
                    System.out.println("Product ID: " + p.getProductId());
                    System.out.println(p.getName());
                    System.out.println("₹" + p.getPrice());
                    System.out.println("----------------------------");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("Wishlist is empty.");
        }
    }

    // ========== REPORTS ==========
    public void generateInventoryReport() {
        int totalProducts = products.size();
        int totalStock = 0;
        int outOfStock = 0;
        int lowStock = 0;

        for (Product p : products) {
            totalStock += p.getQuantity();
            if (p.getQuantity() == 0) outOfStock++;
            if (p.getQuantity() <= 5 && p.getQuantity() > 0) lowStock++;
        }

        System.out.println("====================================");
        System.out.println("       INVENTORY REPORT");
        System.out.println("====================================");
        System.out.println("Total Products       : " + totalProducts);
        System.out.println("Total Stock          : " + totalStock);
        System.out.println("Out of Stock         : " + outOfStock);
        System.out.println("Low Stock Products   : " + lowStock);
    }

    public void generateSalesReport() {
        int totalOrders = orders.size();
        int completed = 0;
        int cancelled = 0;
        double totalSales = 0.0;

        for (String order : orders) {
            String[] parts = order.split("\\|");
            if (parts.length >= 7) {
                String status = parts[6];
                double total = Double.parseDouble(parts[5]);
                if (status.equals("Delivered")) {
                    completed++;
                    totalSales += total;
                } else if (status.equals("Cancelled")) {
                    cancelled++;
                }
            }
        }

        System.out.println("====================================");
        System.out.println("        SALES REPORT");
        System.out.println("====================================");
        System.out.println("Total Orders        : " + totalOrders);
        System.out.println("Completed Orders    : " + completed);
        System.out.println("Cancelled Orders    : " + cancelled);
        System.out.println("Total Sales         : ₹" + totalSales);
    }

    public void generateSellerSalesReport(int sellerId) {
        int totalOrders = 0;
        int completed = 0;
        int cancelled = 0;
        double totalSales = 0.0;

        for (String order : orders) {
            String[] parts = order.split("\\|");
            if (parts.length >= 7 && Integer.parseInt(parts[2]) == sellerId) {
                totalOrders++;
                String status = parts[6];
                double total = Double.parseDouble(parts[5]);
                if (status.equals("Delivered")) {
                    completed++;
                    totalSales += total;
                } else if (status.equals("Cancelled")) {
                    cancelled++;
                }
            }
        }

        System.out.println("====================================");
        System.out.println("        SALES REPORT");
        System.out.println("====================================");
        System.out.println("Total Orders        : " + totalOrders);
        System.out.println("Completed Orders    : " + completed);
        System.out.println("Cancelled Orders    : " + cancelled);
        System.out.println("Total Sales         : ₹" + totalSales);
    }

    // ========== PROFILE ==========
    public void viewProfile(int userId) {
        User u = findUser(userId);
        if (u != null) {
            System.out.println("========== PROFILE ==========");
            System.out.println(u.toString());
        } else {
            System.out.println("User not found!");
        }
    }

    public boolean updateProfile(int userId, String newName, String newEmail) {
        User u = findUser(userId);
        if (u == null) {
            System.out.println("User not found!");
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            u.setName(newName);
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            if (newEmail.contains("@")) {
                u.setEmail(newEmail);
            } else {
                System.out.println("Invalid email format!");
                return false;
            }
        }
        System.out.println("Profile updated successfully!");
        return true;
    }

    // ========== ACTIVITY LOG ==========
    public void viewActivityLog() {
        System.out.println("========== SYSTEM ACTIVITY ==========");
        if (activityLog.isEmpty()) {
            System.out.println("No activity recorded.");
            return;
        }
        int count = 1;
        for (String activity : activityLog) {
            System.out.println(count + ". " + activity);
            count++;
        }
    }
}