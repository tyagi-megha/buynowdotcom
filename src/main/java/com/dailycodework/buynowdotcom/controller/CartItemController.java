package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.model.Cart;
import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.cart.ICartItemService;
import com.dailycodework.buynowdotcom.service.cart.ICartService;
import com.dailycodework.buynowdotcom.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    //since userID was to be retreived from login suystem which is not implmeneted yet
    public ResponseEntity<ApiResponse>  addItemToCart(//Long userId,
                                                      @RequestParam Long productId,@RequestParam int quantity) {
        User user = userService.getAuthenticatedUSer();
        Cart userCart= cartService.initializeNewCartForUser(user);
        cartItemService.addItemToCart(userCart.getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Cart Item added successfully", null));
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        cartItemService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok(new ApiResponse("Cart Item deleted successfully", null));
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateCartItem( @PathVariable Long cartId,@PathVariable Long productId,
                                                       @RequestParam int quantity) {
        cartItemService.updateItemQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Cart Item updated successfully", null));
    }


}
