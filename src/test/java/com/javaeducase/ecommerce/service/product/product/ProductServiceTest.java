package com.javaeducase.ecommerce.service.product.product;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.exception.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product activeProduct;
    private Product deletedProduct;
    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    void setUp() {
        activeProduct = new Product();
        activeProduct.setId(1L);
        activeProduct.setName("Test Product");
        activeProduct.setDescription("Test Product Description");
        activeProduct.setIsDeleted(false);
        activeProduct.setOffers(new ArrayList<>());
        activeProduct.setCategory(new Category());

        deletedProduct = new Product();
        deletedProduct.setId(2L);
        deletedProduct.setName("Test Deleted Product");
        deletedProduct.setDescription("Test Deleted Product Description");
        deletedProduct.setIsDeleted(true);
        deletedProduct.setOffers(new ArrayList<>());
        deletedProduct.setCategory(new Category());

        offer1 = new Offer();
        offer1.setId(1L);
        offer1.setProduct(activeProduct);
        offer1.setPrice(100);
        offer1.setStockQuantity(10);
        offer1.setIsAvailable(true);
        offer1.setIsDeleted(false);
        offer1.setAttributes(new ArrayList<Attribute>());

        offer2 = new Offer();
        offer2.setId(2L);
        offer2.setProduct(deletedProduct);
        offer2.setPrice(200);
        offer2.setStockQuantity(20);
        offer2.setIsAvailable(true);
        offer2.setIsDeleted(false);
        offer2.setAttributes(new ArrayList<Attribute>());

        activeProduct.setOffers(List.of(offer1, offer2));
    }

    @Test
    void getAllProducts_Success() {
        List<Product> mockProducts = List.of(activeProduct, deletedProduct);
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertEquals("Test Deleted Product", result.get(1).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getAllActiveProducts_Success() {
        List<Product> mockProducts = List.of(activeProduct, deletedProduct);
        when(productRepository.findAll()).thenReturn(mockProducts);
        List<ProductDTO> result = productService.getAllActiveProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(activeProduct.getId())).thenReturn(Optional.of(activeProduct));

        ProductDTO result = productService.getProductById(activeProduct.getId());

        Assertions.assertNotNull(result);
        assertEquals(activeProduct.getName(), result.getName());
        assertEquals(activeProduct.getDescription(), result.getDescription());
        verify(productRepository).findById(activeProduct.getId());
    }

    @Test
    void getProductById_ProductNotFound() {
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = Assertions.assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(3L)
        );
        assertEquals("Product with id: 3 not found", exception.getMessage());
    }

    @Test
    void getProductById_ProductIsDeleted() {
        when(productRepository.findById(2L)).thenReturn(Optional.of(deletedProduct));

        ProductIsDeletedException exception = Assertions.assertThrows(
                ProductIsDeletedException.class,
                () -> productService.getProductById(2L)
        );
        assertEquals("Product is deleted", exception.getMessage());
    }

    @Test
    void getAllOffersOfProduct_Success() {
        when(productRepository.findById(activeProduct.getId())).thenReturn(Optional.of(activeProduct));

        List<OfferDTO> result = productService.getAllOffersOfProduct(activeProduct.getId());

        assertEquals(2, result.size());
        assertEquals(offer1.getId(), result.get(0).getId());
        assertEquals(offer1.getPrice(), result.get(0).getPrice());
        assertEquals(offer1.getStockQuantity(), result.get(0).getStockQuantity());
        assertEquals(offer2.getId(), result.get(1).getId());
        assertEquals(offer2.getPrice(), result.get(1).getPrice());
        assertEquals(offer2.getStockQuantity(), result.get(1).getStockQuantity());

        verify(productRepository).findById(1L);
    }
}
