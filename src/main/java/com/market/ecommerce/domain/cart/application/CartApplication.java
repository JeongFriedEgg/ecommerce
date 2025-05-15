package com.market.ecommerce.domain.cart.application;

import com.market.ecommerce.domain.cart.dto.AddProductToCart;
import com.market.ecommerce.domain.cart.entity.Cart;
import com.market.ecommerce.domain.cart.exception.CartException;
import com.market.ecommerce.domain.cart.service.CartService;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.service.ProductService;
import com.market.ecommerce.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.market.ecommerce.domain.cart.exception.CartErrorCode.OUT_OF_STOCK;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartApplication {
    private final CustomerService customerService;
    private final CartService cartService;
    private final ProductService productService;

    public AddProductToCart.Response addProductToCart(
            AddProductToCart.Request req, String username
    ) {
        // 사용자 검증
        customerService.findCustomerByUsername(username);

        // 상품 데이터 존재 여부 확인
        Product product = productService.validateProductExists(req.getProductId());

        // 상품 정상판매 여부 확인
        productService.validateProductAvailability(product);

        // 요청받은 장바구니 아이템 수량과 상품 재고수 비교
        int requestedQuantity = req.getQuantity();
        int stock = productService.getStock(req.getProductId());
        if (requestedQuantity > stock) {
            throw new CartException(OUT_OF_STOCK);
        }

        // 요청받은 상품 장바구니에 추가
        Cart cart = cartService.addProductToCart(req, username);

        return AddProductToCart.Response.from(cart);
    }

    public void clearCart(String username) {
        customerService.findCustomerByUsername(username);
        cartService.clearCart(username);
    }
}
