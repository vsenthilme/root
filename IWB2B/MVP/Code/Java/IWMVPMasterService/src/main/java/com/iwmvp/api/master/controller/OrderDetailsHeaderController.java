package com.iwmvp.api.master.controller;
import com.iwmvp.api.master.model.asyad.Consignment;
import com.iwmvp.api.master.model.asyad.ConsignmentResponse;
import com.iwmvp.api.master.model.customer.Customer;
import com.iwmvp.api.master.model.orderdetails.*;

import com.iwmvp.api.master.service.OrderDetailsHeaderService;
import com.iwmvp.api.master.controller.exception.*;
import com.iwmvp.api.master.service.SetupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@Api(tags = {"OrderDetailsHeader"},value = "OrderDetailsHeader Operations related to OrderDetailsHeaderController")//label for Swagger
@SwaggerDefinition(tags ={@Tag(name="OrderDetailsHeader",description = "Operations related to OrderDetailsHeader")})
@RequestMapping("/orderdetailsheader")
@RestController
public class OrderDetailsHeaderController {
    @Autowired
    private OrderDetailsHeaderService orderDetailsHeaderService;

    @Autowired
    private SetupService setupService;

    @ApiOperation(response = OrderDetailsHeader.class, value = "Get all OrderDetailsHeader") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        List<OrderDetailsHeaderOutput> OrderDetailsHeaderList=orderDetailsHeaderService.getAllOrderDetailsHeader();
        return new ResponseEntity<>(OrderDetailsHeaderList, HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class,value = "Get a OrderListDetails")
    @GetMapping("/{orderId}")
    public ResponseEntity<?>getOrderDetailsHeader(@PathVariable Long orderId){
        OrderDetailsHeaderOutput OrderDetailsHeaderList=orderDetailsHeaderService.getOrderDetailsHeader(orderId);
        log.info("OrderDetailsHeaderList : "+OrderDetailsHeaderList);
        return new ResponseEntity<>(OrderDetailsHeaderList,HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Create OrderDetailsHeader") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postOrderDetailsHeader(@Valid @RequestBody AddOrderDetailsHeader newOrderDetailsHeader,
                                          @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeaderOutput createdOrderDetailsHeader = orderDetailsHeaderService.createOrderDetailsHeader(newOrderDetailsHeader, loginUserID);
        return new ResponseEntity<>(createdOrderDetailsHeader, HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Update OrderDetailsHeader") // label for swagger
    @PatchMapping("/{orderId}")
    public ResponseEntity<?> patchOrderDetailsHeader(@PathVariable Long orderId,
                                               @Valid @RequestBody AddOrderDetailsHeader updateOrderDetailsHeader, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeaderOutput createdOrderDetailsHeader =
                orderDetailsHeaderService.updateOrderDetailsHeader(orderId,loginUserID, updateOrderDetailsHeader);
        return new ResponseEntity<>(createdOrderDetailsHeader, HttpStatus.OK);
    }
//    @ApiOperation(response = Optional.class, value = "SoftData Upload") // label for swagger
//    @PostMapping("/softdata/upload")
//    public ResponseEntity<?> softDataUpload (@RequestHeader(value="Authorization") String authorization,
//                                             @Valid @RequestBody Consignment newConsignment) throws Exception {
//        log.info("authorization passed: " + authorization);
//        log.info("newSoftDataUpload passed: " + newConsignment);
//
//        if (!authorization.startsWith("Basic")) {
//            throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
//        }
//
//        // Lifetoken : Basic $2a$10$qWHNeBhu4FCGoJfuv2XVbO9Yq4QBUwGSvNM0bGpYUVc3iY8jXsJwO
//
//        ConsignmentResponse consignmentResponse = setupService.createConsignment(newConsignment, "ASYAD");
//        log.info("Consignment Created: " + consignmentResponse);
//
//        return new ResponseEntity<>(consignmentResponse, HttpStatus.OK);
//    }
    @ApiOperation(response = Optional.class, value = "Approve Order Details") // label for swagger
    @PostMapping("/{orderId}")
    public ResponseEntity<?> approveOrderDetails(@PathVariable Long  orderId,
                                             @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        orderDetailsHeaderService.approveOrderDetailsHeader(orderId,loginUserID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(response = OrderDetailsHeader.class, value = "Delete OrderDetailsHeader") // label for swagger
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrderDetailsHeader(@PathVariable Long orderId,
                                            @RequestParam String loginUserID) {
        orderDetailsHeaderService.deleteOrderDetailsHeader(orderId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //Find
    @ApiOperation(response = OrderDetailsHeader.class, value = "Find OrderDetailsHeader") // label for swagger
    @PostMapping("/findOrderDetailsHeader")
    public ResponseEntity<?> findOrderDetailsHeader(@Valid @RequestBody FindOrderDetailsHeader findOrderDetailsHeader) throws Exception {
        List<OrderDetailsHeaderOutput> createdOrderDetailsHeader = orderDetailsHeaderService.findOrderDetailsHeader(findOrderDetailsHeader);
        return new ResponseEntity<>(createdOrderDetailsHeader, HttpStatus.OK);
    }
}
