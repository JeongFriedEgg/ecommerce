package com.market.ecommerce.domain.cart.mapper;

import com.market.ecommerce.domain.cart.dto.AddProductToCart;
import com.market.ecommerce.domain.cart.entity.Cart;
import org.springframework.stereotype.Component;

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
}
