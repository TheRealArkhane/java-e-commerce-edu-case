package com.javaeducase.ecommerce.service.product.product;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminProductService adminProductService;

    private Product product;
    private Offer offer;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Category 1");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setIsDeleted(false);
        product.setCategory(category);

        offer = new Offer();
        offer.setId(1L);
        offer.setProduct(product);
        offer.setPrice(100);
        offer.setStockQuantity(10);
        offer.setIsAvailable(true);
        offer.setIsDeleted(false);
        offer.setAttributes(new ArrayList<Attribute>());
        product.setOffers(List.of(offer));
    }

    @Test
    void createProduct_Success() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setDescription("New Description");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        productDTO.setCategory(categoryDTO);

        Product newProduct = new Product();
        newProduct.setId(2L);
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setIsDeleted(false);
        newProduct.setCategory(category);
        newProduct.setOffers(new ArrayList<>());


        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(newProduct);

        ProductDTO result = adminProductService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(1L, result.getCategory().getId());
    }

    @Test
    void updateProduct_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setDescription("Updated Description");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        productDTO.setCategory(categoryDTO);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ProductDTO result = adminProductService.updateProduct(product.getId(), productDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(1L, result.getCategory().getId());
    }

    @Test
    void updateProduct_ProductIsDeleted() {
        product.setIsDeleted(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");

        ProductIsDeletedException exception = Assertions.assertThrows(
                ProductIsDeletedException.class,
                () -> adminProductService.updateProduct(1L, productDTO)
        );
        assertEquals("Product is deleted", exception.getMessage());
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(Mockito.any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        adminProductService.deleteProduct(1L);

        Assertions.assertTrue(product.getIsDeleted());
        Assertions.assertTrue(product.getOffers().stream().allMatch(Offer::getIsDeleted));
        verify(productRepository).save(product);
    }
}


