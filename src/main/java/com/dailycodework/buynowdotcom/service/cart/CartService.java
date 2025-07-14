package com.dailycodework.buynowdotcom.service.cart;

import com.dailycodework.buynowdotcom.model.Cart;
import com.dailycodework.buynowdotcom.model.CartItem;
import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.repository.CartItemRepository;
import com.dailycodework.buynowdotcom.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Cart not found")
        );
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.clearCart();
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart initializeNewCartForUser(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(
                () -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                }
        );
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }
}
