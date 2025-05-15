package com.market.ecommerce.domain.cart.application;

import com.market.ecommerce.domain.cart.dto.AddProductToCart;
import com.market.ecommerce.domain.cart.dto.RemoveProductFromCart;
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

    public void removeProductFromCart(
            RemoveProductFromCart.Request req, String username
    ) {
        customerService.findCustomerByUsername(username);

        /* 상품 데이터가 판매자의 상품 삭제 요청으로 상품 테이블에서 이미 삭제되었을 수 있다.
        *  그러나 고객에 대한 장바구니 데이터에는 따로 업데이트를 하지 않기 때문에,
        *  요청받은 상품 아이디를 이용하여 레디스 저장소에서 삭제해주어야 한다.
        * */
        cartService.removeProductFromCart(req, username);
    }

    public void clearCart(String username) {
        customerService.findCustomerByUsername(username);
        cartService.clearCart(username);
    }
}
