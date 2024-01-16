package com.iwmvp.api.master.controller;

import com.iwmvp.api.master.model.customer.*;
import com.iwmvp.api.master.service.CustomerService;
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

@Slf4j
@Validated
@Api(tags = {"Customer"},value = "Customer Operations related to CustomerController")//label for Swagger
@SwaggerDefinition(tags ={@Tag(name="Customer",description = "Operations related to Customer")})
@RequestMapping("/customer")
@RestController
public class CustomerController{
    @Autowired
    private CustomerService customerService;

    @ApiOperation(response = Customer.class, value = "Get all Customer details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?>getAll(){
        List<Customer> customerList=customerService.getCustomers();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Get a Customer") // label for swagger
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomer( @PathVariable Long customerId,@RequestParam String companyId,@RequestParam String languageId) {
        Customer CustomerList =
                customerService.getCustomerById(customerId);
        return new ResponseEntity<>(CustomerList, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Create Customer") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postCustomer(@Valid @RequestBody AddCustomer newCustomer,
                                         @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        Customer createdCustomer = customerService.createCustomer(newCustomer, loginUserID);
        return new ResponseEntity<>(createdCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Update Customer") // label for swagger
    @PatchMapping("/{customerId}")
    public ResponseEntity<?> patchCustomer(@PathVariable Long  customerId,@RequestParam String companyId,@RequestParam String languageId,
                                           @Valid @RequestBody UpdateCustomer updateCustomer, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Customer createdCustomer =
                customerService.updateCustomer(customerId,companyId,languageId, loginUserID, updateCustomer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Approve Customer") // label for swagger
    @PostMapping("/{customerId}")
    public ResponseEntity<?> approveCustomer(@PathVariable Long  customerId,
                                           @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
            customerService.approveCustomer(customerId,loginUserID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(response = Customer.class, value = "Delete Customer") // label for swagger
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long  customerId,@RequestParam String companyId,@RequestParam String languageId,
                                           @RequestParam String loginUserID) {
        customerService.deleteCustomer(customerId,companyId,languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //Find
    @ApiOperation(response = Customer.class, value = "Find Customer") // label for swagger
    @PostMapping("/findCustomer")
    public ResponseEntity<?> findCustomer(@Valid @RequestBody FindCustomer findCustomer) throws Exception {
        List<Customer> createdCustomer = customerService.findCustomer(findCustomer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.OK);
    }
}
