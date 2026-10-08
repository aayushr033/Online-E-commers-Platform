public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int sellerId;

    public Product(int productId, String name, String description, double price, int quantity, int sellerId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public void increaseInventory(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public boolean decreaseInventory(int amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ID: " + productId +
               "\nName: " + name +
               "\nDescription: " + description +
               "\nPrice: ₹" + price +
               "\nQuantity: " + quantity +
               "\nSeller ID: " + sellerId;
    }
}