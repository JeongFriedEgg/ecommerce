package com.market.ecommerce.cart.mapper;

import com.market.ecommerce.cart.client.dto.ProductValidationCommand;
import com.market.ecommerce.cart.client.dto.ProductValidationResult;
import com.market.ecommerce.cart.dto.AddProductToCart;
import com.market.ecommerce.cart.entity.Cart;
import com.market.ecommerce.cart.service.dto.PutCartCommand;
import com.market.ecommerce.cart.service.dto.CartResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public Cart.CartItem convertToCartItem(AddProductToCart.Request req) {
        return Cart.CartItem.builder()
                .productId(req.getProductId())
                .title(req.getTitle())
                .mainImageUrl(req.getMainImageUrl())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .build();
    }

    public CartResult.CartItem mapToResultItem(Cart.CartItem item) {
        return CartResult.CartItem.builder()
                .productId(item.getProductId())
                .title(item.getTitle())
                .mainImageUrl(item.getMainImageUrl())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .build();
    }

    public Cart convertToCart(String customerId, List<PutCartCommand> putCartCommands) {
        List<Cart.CartItem> cartItems = putCartCommands.stream()
                .map(cmd -> Cart.CartItem.builder()
                        .productId(cmd.getProductId())
                        .title(cmd.getTitle())
                        .mainImageUrl(cmd.getMainImageUrl())
                        .price(cmd.getPrice())
                        .quantity(cmd.getQuantity())
                        .status(cmd.getStatus())
                        .build())
                .toList();

        return Cart.builder()
                .customerId(customerId)
                .cartItems(cartItems)
                .build();
    }

    public PutCartCommand toPutCartCommand(ProductValidationResult.ProductItem item) {
        return PutCartCommand.builder()
                .productId(item.getProductId())
                .title(item.getTitle())
                .mainImageUrl(item.getMainImageUrl())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .build();
    }

    public List<PutCartCommand> toPutCartCommands(List<ProductValidationResult.ProductItem> items) {
        return items.stream()
                .map(this::toPutCartCommand)
                .toList();
    }

    public ProductValidationCommand toValidationCommand(List<CartResult.CartItem> cartItems) {
        List<ProductValidationCommand.ProductInfo> productInfos = cartItems.stream()
                .map(item -> ProductValidationCommand.ProductInfo.builder()
                        .productId(item.getProductId())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .status(item.getStatus())
                        .build()
                ).toList();

        return new ProductValidationCommand(productInfos);
    }
}
