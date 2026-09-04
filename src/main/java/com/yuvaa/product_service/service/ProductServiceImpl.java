package com.yuvaa.product_service.service;

import com.yuvaa.product_service.dto.ItemResponse;
import com.yuvaa.product_service.dto.ProductRequest;
import com.yuvaa.product_service.dto.ProductResponse;
import com.yuvaa.product_service.entity.Item;
import com.yuvaa.product_service.entity.Product;
import com.yuvaa.product_service.exception.ResourceNotFoundException;
import com.yuvaa.product_service.repository.ItemRepository;
import com.yuvaa.product_service.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    public ProductServiceImpl(ProductRepository productRepository, ItemRepository itemRepository) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .productName(request.productName())
                .build();

        if (request.items() != null && !request.items().isEmpty()) {
            request.items().forEach(itemReq -> {
                Item item = Item.builder()
                        .quantity(itemReq.quantity())
                        .build();
                product.addItem(item);
            });
        }

        Product saved = productRepository.save(product);
        return mapToProductResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setProductName(request.productName());

        if (request.items() != null) {
            product.getItems().clear();
            request.items().forEach(itemReq -> {
                Item item = Item.builder()
                        .quantity(itemReq.quantity())
                        .build();
                product.addItem(item);
            });
        }

        Product updated = productRepository.save(product);
        return mapToProductResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return itemRepository.findByProductId(productId).stream()
                .map(item -> new ItemResponse(item.getId(), item.getProduct().getId(), item.getQuantity()))
                .toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        List<ItemResponse> itemResponses = product.getItems() != null
                ? product.getItems().stream()
                .map(item -> new ItemResponse(item.getId(), product.getId(), item.getQuantity()))
                .toList()
                : Collections.emptyList();

        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getCreatedBy(),
                product.getCreatedOn(),
                product.getModifiedBy(),
                product.getModifiedOn(),
                itemResponses
        );
    }
}