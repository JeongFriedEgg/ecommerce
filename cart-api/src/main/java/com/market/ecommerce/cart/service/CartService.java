package com.market.ecommerce.cart.service;

import com.market.ecommerce.cart.dto.AddProductToCart;
import com.market.ecommerce.cart.dto.RemoveProductFromCart;
import com.market.ecommerce.cart.entity.Cart;
import com.market.ecommerce.common.redis.RedisClient;
import com.market.ecommerce.cart.mapper.CartMapper;
import com.market.ecommerce.cart.service.dto.PutCartCommand;
import com.market.ecommerce.cart.service.dto.CartResult;
import com.market.ecommerce.exception.domain.cart.CartException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.market.ecommerce.exception.domain.cart.CartErrorCode.CART_NOT_FOUND;
import static com.market.ecommerce.exception.domain.cart.CartErrorCode.PRODUCT_NOT_IN_CART;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisClient redisClient;
    private final CartMapper cartMapper;

    public CartResult getCart(String customerId) {
        Cart cart = redisClient.getValue(customerId, Cart.class);
        if (cart == null) {
            return CartResult.builder().cartItems(Collections.emptyList()).build();
        }
        List<CartResult.CartItem> resultItems = cart.getCartItems().stream()
                .map(cartMapper::mapToResultItem)
                .toList();

        return CartResult.builder()
                .cartItems(resultItems)
                .build();
    }

    public void putCart(String customerId, List<PutCartCommand> putCartCommands) {
        if (putCartCommands == null || putCartCommands.isEmpty()) {
            return;
        }
        Cart cart = cartMapper.convertToCart(customerId, putCartCommands);
        redisClient.putValue(customerId, cart);
    }

    public Cart addProductToCart(AddProductToCart.Request req, String customerId){
        // 레디스에 고객 아이디로 장바구니 조회
        Cart cart = redisClient.getValue(customerId, Cart.class);
        // 장바구니가 없으면 새로 생성
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        List<Cart.CartItem> cartItems = cart.getCartItems();

        // 요청받은 아이템이 장바구니에 존재하는지 확인
        Optional<Cart.CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(req.getProductId()))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            // 요청받은 아이템이 장바구니에 이미 존재하는 경우 quantity 값 업데이트
            Cart.CartItem existingItem = cartItemOptional.get();
            existingItem.setQuantity(req.getQuantity());
        }else {
            // 요청받은 아이템이 장바구니에 없는 경우 그냥 추가
            Cart.CartItem newCartItem = cartMapper.convertToCartItem(req);
            cartItems.add(newCartItem);
        }

        redisClient.putValue(customerId, cart);
        return cart;
    }

    public void removeProductFromCart(RemoveProductFromCart.Request req, String customerId) {
        Cart cart = redisClient.getValue(customerId, Cart.class);

        if (cart == null) {
            throw new CartException(CART_NOT_FOUND);
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new CartException(PRODUCT_NOT_IN_CART);
        }

        boolean removed = cart.getCartItems().removeIf(
                item -> item.getProductId().equals(req.getProductId())
        );

        if (removed) {
            redisClient.putValue(customerId, cart);
        }
    }

    public void clearCart(String customerId) {
        redisClient.deleteValue(customerId);
    }
}
