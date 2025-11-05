package com.gabriel.serviceimpl;

import com.gabriel.entity.ProductData;
import com.gabriel.model.Product;
import com.gabriel.model.ProductCategory;
import com.gabriel.repository.ProductDataRepository;
import com.gabriel.service.ProductService;
import com.gabriel.util.Transform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDataRepository productDataRepository;

    Transform< ProductData, Product> transformProductData = new Transform<>(Product.class);

    Transform<Product, ProductData> transformProduct = new Transform<>(ProductData.class);


    public List<Product> getAllProducts() {
        List<ProductData>productDataRecords = new ArrayList<>();
        List<Product> products =  new ArrayList<>();

        productDataRepository.findAll().forEach(productDataRecords::add);
        Iterator<ProductData> it = productDataRecords.iterator();

        while(it.hasNext()) {
            Product product = new Product();
            ProductData productData = it.next();
            product = transformProductData.transform(productData);
            products.add(product);
        }
        return products;
    }
    @Override
    public List<ProductCategory> listProductCategories()
    {
        Map<String,List<Product>> mappedProduct = getCategoryMappedProducts();
        List<ProductCategory> productCategories = new ArrayList<>();
        for(String categoryName: mappedProduct.keySet()){
            ProductCategory productCategory =  new ProductCategory();
            productCategory.setCategoryName(categoryName);
            productCategory.setProducts(mappedProduct.get(categoryName));
            productCategories.add(productCategory);
        }
        return productCategories;
    }
    @Override
    public Map<String,List<Product>> getCategoryMappedProducts()
    {
        Map<String,List<Product>> mapProducts = new HashMap<String,List<Product>>();

        List<ProductData>productDataRecords = new ArrayList<>();
        List<Product> products;

        productDataRepository.findAll().forEach(productDataRecords::add);
        Iterator<ProductData> it = productDataRecords.iterator();

        while(it.hasNext()) {
            Product product = new Product();
            ProductData productData = it.next();

            if(mapProducts.containsKey(productData.getCategoryName())){
                products = mapProducts.get(productData.getCategoryName());
            }
            else {
                products = new ArrayList<Product>();
                mapProducts.put(productData.getCategoryName(), products);
            }
            product = transformProductData.transform(productData);
            products.add(product);
        }
        return mapProducts;
    }

    @Override
    public Product[] getAll() {
            List<ProductData> productsData = new ArrayList<>();
            List<Product> products = new ArrayList<>();
            productDataRepository.findAll().forEach(productsData::add);
            Iterator<ProductData> it = productsData.iterator();
            while(it.hasNext()) {
                ProductData productData = it.next();
                Product product =  transformProductData.transform(productData);
                products.add(product);
            }
            Product[] array = new Product[products.size()];
            for  (int i=0; i<products.size(); i++){
                array[i] = products.get(i);
            }
            return array;
        }
    @Override
    public Product get(Integer id) {
        log.info(" Input id >> "+  Integer.toString(id) );
        Product product = null;
        Optional<ProductData> optional = productDataRepository.findById(id);
        if(optional.isPresent()) {
            log.info(" Is present >> ");
            product = transformProductData.transform(optional.get());
        }
        else {
            log.info(" Failed >> unable to locate id: " +  Integer.toString(id)  );
        }
        return product;
    }
        @Override
        public Product create(Product product) {
            log.info(" add:Input " + product.toString());
            ProductData productData = transformProduct.transform(product);
            ProductData updatedProductData = productDataRepository.save(productData);
            log.info(" add:Input {}", productData.toString());
            return  transformProductData.transform(updatedProductData);
        }

        @Override
        public Product update(Product product) {
            Optional<ProductData> optional  = productDataRepository.findById(product.getId());
            if(optional.isPresent()){
                ProductData productData = transformProduct.transform(product);
                ProductData updaatedProductData = productDataRepository.save( productData);
                return transformProductData.transform(updaatedProductData);
            }
            else {
                log.error("Product record with id: {} do not exist",product.getId());
            }
            return null;
        }
    @Override
    public void delete(Integer id) {
        log.info(" Input >> {}",id);
        Optional<ProductData> optional = productDataRepository.findById(id);
        if( optional.isPresent()) {
            ProductData productDatum = optional.get();
            productDataRepository.delete(optional.get());
            log.info(" Successfully deleted Product record with id: {}",id);
        }
        else {
            log.error(" Unable to locate product with id: {}", id);
        }
    }
    
    @Override
    public List<Product> getVehiclesWithFilters(
            String make, String model, Integer yearMin, Integer yearMax,
            Double priceMin, Double priceMax, Integer mileageMin, Integer mileageMax,
            String fuelType, String transmission, String status) {
        
        List<ProductData> vehicleDataList = productDataRepository.findVehiclesWithFilters(
            make, model, yearMin, yearMax,
            mileageMin, mileageMax, fuelType, transmission, status
        );
        
        List<Product> vehicles = new ArrayList<>();
        for (ProductData vehicleData : vehicleDataList) {
            // Filter by price in Java since price is stored as String
            if (priceMin != null || priceMax != null) {
                try {
                    double vehiclePrice = Double.parseDouble(vehicleData.getPrice() != null ? vehicleData.getPrice().replace("$", "").replace(",", "") : "0");
                    if (priceMin != null && vehiclePrice < priceMin) {
                        continue;
                    }
                    if (priceMax != null && vehiclePrice > priceMax) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    log.warn("Could not parse price for vehicle {}: {}", vehicleData.getId(), vehicleData.getPrice());
                }
            }
            
            Product vehicle = transformProductData.transform(vehicleData);
            vehicles.add(vehicle);
        }
        
        return vehicles;
    }
}
