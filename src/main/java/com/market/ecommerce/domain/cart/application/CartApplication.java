package com.market.ecommerce.domain.cart.application;

import com.market.ecommerce.domain.cart.dto.AddProductToCart;
import com.market.ecommerce.domain.cart.dto.GetCart;
import com.market.ecommerce.domain.cart.dto.RemoveProductFromCart;
import com.market.ecommerce.domain.cart.entity.Cart;
import com.market.ecommerce.domain.cart.exception.CartException;
import com.market.ecommerce.domain.cart.service.CartService;
import com.market.ecommerce.domain.product.entity.Product;
import com.market.ecommerce.domain.product.service.ProductService;
import com.market.ecommerce.domain.product.type.ProductType;
import com.market.ecommerce.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.market.ecommerce.domain.cart.exception.CartErrorCode.CART_IS_EMPTY;
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

    public GetCart.Response getCart(String username) {
        customerService.findCustomerByUsername(username);

        Cart cart = cartService.getCart(username);

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new CartException(CART_IS_EMPTY);
        }

        // 장바구니 객체가 비어있지 않은 경우, 장바구니 내의 상품 정보에 대한 업데이트 진행

        // 장바구니에 있는 모든 productId 추출
        // 장바구니에 있는 CartItem 의 productId 로 Product 객체들을 조회
        Set<Long> productIds = cart.getCartItems().stream()
                .map(Cart.CartItem::getProductId)
                .collect(Collectors.toSet());

        // 한 번의 쿼리로 모든 Product 조회
        List<Product> products = productService.findAllById(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // 응답에 대한 CartItems, messages 객체 생성
        List<GetCart.Response.CartItem> cartItems = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        Set<Cart.CartItem> updatedCartItems = new HashSet<>();

        for (Cart.CartItem cartItem : cart.getCartItems()) {
            Long productId = cartItem.getProductId();
            Product product = productMap.get(productId);

            // productId 로 조회했을 때, 상품 데이터가 없는 경우에는 상품 아이디를 상품이 없어졌다는 메시지와 함께 담음.
            if (product == null) {
                messages.add("상품 ID [" + productId + "]는 더 이상 판매자에 의해 삭제되었습니다.");
                continue;
            }

            // Cart 에 담겨진 CartItem 과 Product 객체의 데이터를 비교하여, 데이터가 변경된 경우, 변경되었다는 메세지를 담음.
            // 상품 가격이 변경된 경우에만 메세지를 담음.
            if (product.getPrice() != cartItem.getPrice()) {
                messages.add("상품 [" + product.getTitle() + "]의 가격이 변경되었습니다. 기존: " +
                        cartItem.getPrice() + "원, 현재: " + product.getPrice() + "원");
            }

            // 상품이 판매가능한 상태인지 확인
            if (product.getStatus().equals(ProductType.OUT_OF_STOCK)) {
                messages.add("상품 [" + product.getTitle() + "]은 일시 품절 상태입니다.");
            } else if (product.getStatus().equals(ProductType.SOLD_OUT)) {
                messages.add("상품 [" + product.getTitle() + "]은 판매 종료되었습니다.");
            }

            // 상품 재고가 장바구니의 수량보다 작은 경우, 재고가 부족하다는 메세지를 담음.
            if (product.getStock() < cartItem.getQuantity()) {
                messages.add("상품 [" + product.getTitle() + "]의 재고가 부족합니다. 현재 재고: " +
                        product.getStock());
            }

            // 업데이트할 장바구니 아이템 객체 생성
            Cart.CartItem updatedCartItem = Cart.CartItem.builder()
                        .productId(product.getId())
                        .title(product.getTitle())
                        .mainImageUrl(
                                getMainImageUrl(product)
                        )
                        .price(product.getPrice())
                        .quantity(cartItem.getQuantity())
                        .build();

            // 업데이트할 장바구니 아이템 리스트에 객체 추가
            updatedCartItems.add(updatedCartItem);

            // 응답에 사용하되는 장바구니 아이템 리스트 객체에 데이터 담음
            cartItems.add(
                    GetCart.Response.CartItem.builder()
                            .productId(product.getId())
                            .title(product.getTitle())
                            .mainImageUrl(
                                    getMainImageUrl(product)
                            )
                            .price(product.getPrice())
                            .quantity(cartItem.getQuantity())
                            .build()
            );
        }

        // 업데이트된 장바구니 아이템들을 다시 레디스에 저장
        cart.setCartItems(updatedCartItems);
        cartService.putCart(cart);

        // 응답 객체 반환
        return GetCart.Response.builder()
                .cartItems(cartItems)
                .messages(messages)
                .build();
    }

    private String getMainImageUrl(Product product) {
        return product.getProductImages().isEmpty() ?
                null : product.getProductImages().get(0).getImageUrl();
    }

    public void clearCart(String username) {
        customerService.findCustomerByUsername(username);
        cartService.clearCart(username);
    }
}
