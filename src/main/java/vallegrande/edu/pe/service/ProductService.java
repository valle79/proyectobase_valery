package vallegrande.edu.pe.service;

 import org.springframework.stereotype.Service;
 import vallegrande.edu.pe.model.Product;

 import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(1, "Laptop", 2500));
        products.add(new Product(2, "Mouse", 50));
    }

    // ❌ Método con lógica innecesaria
    public List<Product> getAll() {
        List<Product> tempList = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            tempList.add(products.get(i)); // duplicación innecesaria
        }

        return tempList;
    }

    // ❌ Código duplicado
    public List<Product> getAllAgain() {
        List<Product> tempList = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            tempList.add(products.get(i));
        }

        return tempList;
    }
}