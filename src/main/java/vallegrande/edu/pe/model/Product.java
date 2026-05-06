package vallegrande.edu.pe.model;

public class Product {

    private int id;
    private String name;
    private double price;

    // ❌ Constructor sin validaciones
    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // ❌ Falta encapsulación adecuada (sin setters)
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}