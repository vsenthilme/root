package com.iwmvp.core.controller;

import com.iwmvp.core.exception.BadRequestException;
import com.iwmvp.core.model.auth.AuthToken;
import com.iwmvp.core.model.auth.AuthTokenRequest;
import com.iwmvp.core.model.master.*;
import com.iwmvp.core.service.AuthTokenService;
import com.iwmvp.core.service.MastersService;
import com.iwmvp.core.util.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*")
@Api(tags = {"Master Service"}, value = "Master Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Masters Modules")})
@RequestMapping("/mv-master-service")
@RestController
public class MastersServiceController {
    @Autowired
    private MastersService mastersService;
    @Autowired
    AuthTokenService authTokenService;

    //-----------------------------------auth token------------------------------------------------------------
    @ApiOperation(response = Optional.class, value = "OAuth Token") // label for swagger
    @PostMapping("/auth-token")
    public ResponseEntity<?> authToken(@Valid @RequestBody AuthTokenRequest authTokenRequest) {
        AuthToken authToken = authTokenService.getAuthToken(authTokenRequest);
        return new ResponseEntity<>(authToken, HttpStatus.OK);
    }

    /*
     * --------------------------------Customer---------------------------------
     */
    @ApiOperation(response = Customer[].class, value = "Get all Customer details") // label for swagger
    @GetMapping("/customer")
    public ResponseEntity<?> getCustomers(@RequestParam String authToken) {
        Customer[] userCustomer = mastersService.getCustomers(authToken);
        return new ResponseEntity<>(userCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Get a Customer") // label for swagger
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?>  getCustomer( @PathVariable Long customerId,@RequestParam String companyId,@RequestParam String languageId,
                                              @RequestParam String authToken) {
        Customer dbCustomer = mastersService.getCustomer(customerId,companyId,languageId,authToken);
        return new ResponseEntity<>(dbCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Create Customer") // label for swagger
    @PostMapping("/customer")
    public ResponseEntity<?> PostCustomer(@Valid @RequestBody AddCustomer newCustomer,
                                            @RequestParam String loginUserID, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        Customer createCustomer = mastersService.createCustomer(newCustomer,loginUserID,authToken);
        return new ResponseEntity<>(createCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Update Customer") // label for swagger
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.PATCH)
    public ResponseEntity<?>updateCustomer(@PathVariable Long customerId,@RequestParam String companyId,@RequestParam String languageId,@RequestParam String authToken,
                                           @Valid @RequestBody UpdateCustomer updateCustomer, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        Customer UpdateCustomer =
                mastersService.updateCustomer(customerId,companyId,languageId,loginUserID,updateCustomer,authToken);
        return new ResponseEntity<>(UpdateCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Approve Customer") // label for swagger
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.POST)
    public ResponseEntity<?>approveCustomer(@PathVariable Long customerId,@RequestParam String loginUserID, @RequestParam String authToken )
                                            throws IllegalAccessException, InvocationTargetException {
        Customer UpdateCustomer =
                mastersService.approveCustomer(customerId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Delete Customer") // label for swagger
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteCustomer( @PathVariable Long customerId,@RequestParam String companyId,@RequestParam String languageId,
                                             @RequestParam String loginUserID, @RequestParam String authToken) {
        mastersService.deleteCustomer(customerId,companyId,languageId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //FIND
    @ApiOperation(response = Customer[].class,value = "Find Customer")//label for Swagger
    @PostMapping("/customer/findCustomer")
    public Customer[] findCustomer(@RequestBody FindCustomer findCustomer,
                                   @RequestParam String authToken)throws Exception{
        return mastersService.findCustomer(findCustomer,authToken);
    }
    /*
     * --------------------------------LoyaltyCategory---------------------------------
     */
    @ApiOperation(response = LoyaltyCategory[].class, value = "Get all LoyaltyCategory details") // label for swagger
    @GetMapping("/loyaltycategory")
    public ResponseEntity<?> getAllLoyaltyCategory(@RequestParam String authToken) {
        LoyaltyCategory[] userLoyaltyCategory = mastersService.getAllLoyaltyCategory(authToken);
        return new ResponseEntity<>(userLoyaltyCategory, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Get a LoyaltyCategory") // label for swagger
    @GetMapping("/loyaltycategory/{rangeId}")
    public ResponseEntity<?>  getLoyaltyCategory( @PathVariable Long rangeId,
            @RequestParam String authToken) {
        LoyaltyCategory dbLoyaltyCategory = mastersService.getLoyaltyCategory(rangeId,authToken);
        return new ResponseEntity<>(dbLoyaltyCategory, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Create LoyaltyCategory") // label for swagger
    @PostMapping("/loyaltycategory")
    public ResponseEntity<?> PostLoyaltyCategory(@Valid @RequestBody AddLoyaltyCategory newLoyaltyCategory,
                                          @RequestParam String loginUserID, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory createLoyaltyCategory = mastersService.createLoyaltyCategory(newLoyaltyCategory,loginUserID,authToken);
        return new ResponseEntity<>(createLoyaltyCategory, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Update LoyaltyCategory") // label for swagger
    @RequestMapping(value = "/loyaltycategory/{rangeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?>updateLoyaltyCategory(@PathVariable Long rangeId,@RequestParam String authToken,
                                                  @Valid @RequestBody UpdateLoyaltyCategory updateLoyaltyCategory, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory UpdateLoyaltyCategory =
                mastersService.updateLoyaltyCategory(rangeId,loginUserID,updateLoyaltyCategory,authToken);
        return new ResponseEntity<>(UpdateLoyaltyCategory,HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Delete LoyaltyCategory") // label for swagger
    @DeleteMapping("/loyaltycategory/{rangeId}")
    public ResponseEntity<?> deleteLoyaltyCategory( @PathVariable Long rangeId,
                                                    @RequestParam String loginUserID, @RequestParam String authToken) {
        mastersService.deleteLoyaltyCategory(rangeId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //FIND
    @ApiOperation(response = LoyaltyCategory[].class, value = "Find LoyaltyCategory")//label for swagger
    @PostMapping("/loyaltycategory/findLoyaltyCategory")
    public LoyaltyCategory[] findLoyaltyCategory(@RequestBody FindLoyaltyCategory findLoyaltyCategory,
                                                 @RequestParam String authToken)throws Exception{
        return mastersService.findLoyaltyCategory(findLoyaltyCategory,authToken);
    }
    /*
     * --------------------------------LoyaltySetup---------------------------------
     */
    @ApiOperation(response = LoyaltySetup[].class, value = "Get all LoyaltySetup details") // label for swagger
    @GetMapping("/loyaltysetup")
    public ResponseEntity<?> getAllLoyaltySetup(@RequestParam String authToken) {
        LoyaltySetup[] userLoyaltySetup = mastersService.getAllLoyaltySetup(authToken);
        return new ResponseEntity<>(userLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Get a LoyaltySetup") // label for swagger
    @GetMapping("/loyaltysetup/{loyaltyId}")
    public ResponseEntity<?>  getLoyaltySetup(@PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId,
            @RequestParam String authToken) {
        LoyaltySetup dbLoyaltySetup = mastersService.getLoyaltySetup(loyaltyId,categoryId,companyId,languageId,authToken);
        return new ResponseEntity<>(dbLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Create LoyaltySetup") // label for swagger
    @PostMapping("/loyaltysetup")
    public ResponseEntity<?> PostLoyaltySetup(@Valid @RequestBody AddLoyaltySetup newLoyaltySetup,
                                          @RequestParam String loginUserID, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup createLoyaltySetup = mastersService.createLoyaltySetup(newLoyaltySetup,loginUserID,authToken);
        return new ResponseEntity<>(createLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Update LoyaltySetup") // label for swagger
    @RequestMapping(value = "/loyaltysetup/{loyaltyId}", method = RequestMethod.PATCH)
    public ResponseEntity<?>updateLoyaltySetup(@PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId,@RequestParam String authToken,
                                               @Valid @RequestBody UpdateLoyaltySetup updateLoyaltySetup, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup UpdateLoyaltySetup =
                mastersService.updateLoyaltySetup(loyaltyId,categoryId,companyId,languageId,loginUserID,updateLoyaltySetup,authToken);
        return new ResponseEntity<>(UpdateLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Delete LoyaltySetup") // label for swagger
    @DeleteMapping("/loyaltysetup/{loyaltyId}")
    public ResponseEntity<?> deleteLoyaltySetup( @PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId,
                                                 @RequestParam String loginUserID, @RequestParam String authToken) {
        mastersService.deleteLoyaltySetup(loyaltyId,categoryId,companyId,languageId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //FIND
    @ApiOperation(response = LoyaltySetup[].class,value = "Find LoyaltySetup")//label for swagger
    @PostMapping("/loyaltysetup/findLoyaltySetup")
    public LoyaltySetup[] findLoyaltySetup(@RequestBody FindLoyaltySetup findLoyaltySetup,
                                           @RequestParam String authToken)throws Exception{
        return mastersService.findLoyaltySetup(findLoyaltySetup,authToken);
    }
    /*
     * --------------------------------OrderDetailsHeader---------------------------------
     */
    @ApiOperation(response = OrderDetailsHeader[].class, value = "Get all OrderDetailsHeader details") // label for swagger
    @GetMapping("/orderdetailsheader")
    public ResponseEntity<?> getAllOrderDetailsHeader(@RequestParam String authToken) {
        OrderDetailsHeaderOutput[] OrderDetailsHeader = mastersService.getAllOrderDetailsHeader(authToken);
        return new ResponseEntity<>(OrderDetailsHeader, HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Get a OrderDetailsHeader") // label for swagger
    @GetMapping("/orderdetailsheader/{orderId}")
    public ResponseEntity<?>  getOrderDetailsHeader( @PathVariable Long orderId,@RequestParam String authToken) {
        OrderDetailsHeaderOutput dbOrderDetailsHeader = mastersService.getOrderDetailsHeader(orderId,authToken);
        return new ResponseEntity<>(dbOrderDetailsHeader, HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Create OrderDetailsHeader") // label for swagger
    @PostMapping("/orderdetailsheader")
    public ResponseEntity<?> PostOrderDetailsHeader(@Valid @RequestBody AddOrderDetailsHeader newOrderDetailsHeader,
                                          @RequestParam String loginUserID, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeaderOutput createOrderDetailsHeader = mastersService.createOrderDetailsHeader(newOrderDetailsHeader,loginUserID,authToken);
        return new ResponseEntity<>(createOrderDetailsHeader, HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Update OrderDetailsHeader") // label for swagger
    @RequestMapping(value = "/orderdetailsheader/{orderId}", method = RequestMethod.PATCH)
    public ResponseEntity<?>updateOrderDetailsHeader(@PathVariable Long orderId, @RequestParam String authToken,
                                               @Valid @RequestBody AddOrderDetailsHeader updateOrderDetailsHeader, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeaderOutput UpdateOrderDetailsHeader =
                mastersService.updateOrderDetailsHeader(orderId,loginUserID,updateOrderDetailsHeader,authToken);
        return new ResponseEntity<>(UpdateOrderDetailsHeader, HttpStatus.OK);
    }

    @ApiOperation(response = OrderDetailsHeader.class, value = "Approve OrderDetailsHeader to B2b") // label for swagger
    @RequestMapping(value = "/orderdetailsheader/{orderId}", method = RequestMethod.POST)
    public ResponseEntity<?>approveOrderDetailsHeader(@PathVariable Long orderId,@RequestParam String loginUserID, @RequestParam String authToken )
            throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeaderOutput approveOrderDetailsHeader =
                mastersService.approveOrderDetailsHeader(orderId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(response = OrderDetailsHeader.class, value = "Delete OrderDetailsHeader") // label for swagger
    @DeleteMapping("/orderdetailsheader/{orderId}")
    public ResponseEntity<?> deleteOrderDetailsHeader( @PathVariable Long orderId,
                                                 @RequestParam String loginUserID, @RequestParam String authToken) {
        mastersService.deleteOrderDetailsHeader(orderId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //FIND
    @ApiOperation(response = AddOrderDetailsHeader[].class,value = "Find OrderDetailsHeader")//label for swagger
    @PostMapping("/orderdetailsheader/findOrderDetailsHeader")
    public OrderDetailsHeaderOutput[] findOrderDetailsHeader(@RequestBody FindOrderDetailsHeader findOrderDetailsHeader,
                                                       @RequestParam String authToken)throws Exception{
        return mastersService.findOrderDetailsHeader(findOrderDetailsHeader,authToken);
    }

    /*
     * --------------------------------NumberRange---------------------------------
     */
    @ApiOperation(response = NumberRange[].class, value = "Get all NumberRange details") // label for swagger
    @GetMapping("/numberrange")
    public ResponseEntity<?> getAllNumberRange(@RequestParam String authToken) {
        NumberRange[] NumberRange = mastersService.getAllNumberRange(authToken);
        return new ResponseEntity<>(NumberRange, HttpStatus.OK);
    }
    @ApiOperation(response = NumberRange.class, value = "Get a NumberRange") // label for swagger
    @GetMapping("/numberrange/{numberRangeCode}")
    public ResponseEntity<?>  getNumberRange( @PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,@RequestParam String companyId,@RequestParam String languageId,@RequestParam String authToken) {
        NumberRange dbNumberRange = mastersService.getNumberRange(numberRangeCode,numberRangeObject,companyId,languageId,authToken);
        return new ResponseEntity<>(dbNumberRange, HttpStatus.OK);
    }
    @ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
    @PostMapping("/numberrange")
    public ResponseEntity<?> PostNumberRange(@Valid @RequestBody AddNumberRange newNumberRange,
                                              @RequestParam String loginUserID, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        NumberRange createNumberRange = mastersService.createNumberRange(newNumberRange,loginUserID,authToken);
        return new ResponseEntity<>(createNumberRange, HttpStatus.OK);
    }
    @ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
    @RequestMapping(value = "/numberrange/{numberRangeCode}", method = RequestMethod.PATCH)
    public ResponseEntity<?>updateNumberRange(@PathVariable Long numberRangeCode,@RequestParam String  numberRangeObject,@RequestParam String companyId,@RequestParam String languageId,@RequestParam String authToken,
                                               @Valid @RequestBody UpdateNumberRange updateNumberRange, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        NumberRange UpdateNumberRange =
                mastersService.updateNumberRange(numberRangeCode,numberRangeObject,companyId,languageId,loginUserID,updateNumberRange,authToken);
        return new ResponseEntity<>(UpdateNumberRange, HttpStatus.OK);
    }
    @ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
    @DeleteMapping("/numberrange/{numberRangeCode}")
    public ResponseEntity<?> deleteNumberRange(@PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,@RequestParam String companyId,@RequestParam String languageId,
                                                 @RequestParam String loginUserID, @RequestParam String authToken) {
        mastersService.deleteNumberRange(numberRangeCode,numberRangeObject,companyId,languageId,loginUserID,authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //FIND
    @ApiOperation(response = NumberRange[].class,value = "Find NumberRange")//label for swagger
    @PostMapping("/numberrange/findNumberRange")
    public NumberRange[] findNumberRange(@RequestBody FindNumberRange findNumberRange,
                                         @RequestParam String authToken)throws Exception{
        return mastersService.findNumberRange(findNumberRange,authToken);
    }
    //-----------------------------------User------------------------------------------------------------
    @ApiOperation(response = User.class, value = "Get all User details") // label for swagger
    @GetMapping("/login/users")
    public ResponseEntity<?> getAllUser(@RequestParam String authToken) {
        User[] userList = mastersService.getAllUser(authToken);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @ApiOperation(response = User.class, value = "Get a User") // label for swagger
    @GetMapping("/login/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id, @RequestParam String authToken) {
        User dbUser = mastersService.getUser(id, authToken);
        return new ResponseEntity<>(dbUser, HttpStatus.OK);
    }

    @ApiOperation(response = User.class, value = "Create User") // label for swagger
    @PostMapping("/login/user")
    public ResponseEntity<?> postUser(@Valid @RequestBody AddUser newUser,
                                      @RequestParam String loginUserID, @RequestParam String authToken) throws Exception {
        User createdUser = mastersService.createUser(newUser, loginUserID, authToken);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @ApiOperation(response = User.class, value = "Update User") // label for swagger
    @PatchMapping("/login/user/{id}")
    public ResponseEntity<?> patchUser(@PathVariable String id,
                                       @RequestParam String loginUserID,
                                       @Valid @RequestBody UpdateUser updateUser,
                                       @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {
        User updatedUser = mastersService.updateUser(id, loginUserID,
                updateUser, authToken);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @ApiOperation(response = User.class, value = "Delete User") // label for swagger
    @DeleteMapping("/login/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id, @RequestParam String loginUserID,
                                        @RequestParam String authToken) {
        mastersService.deleteUser(id, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * This end point will be called for Web App User Login
     *
     * @param userID
     * @param password
     * @return
     */
    @ApiOperation(response = Optional.class, value = "Validate Login User") // label for swagger
    @RequestMapping(value = "/login/validate", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> loginUser(@RequestParam String userID, @RequestParam String password,
                                       @RequestParam String authToken) {
        try {
            User loggedInUser = mastersService.validateUser(userID, password, authToken);
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (BadRequestException e) {
            e.printStackTrace();
            CommonUtils commonUtils = new CommonUtils();
            Map<String, String> mapError = commonUtils.prepareErrorResponse(e.getLocalizedMessage());
            return new ResponseEntity<>(mapError, HttpStatus.UNAUTHORIZED);
        }
    }
}
