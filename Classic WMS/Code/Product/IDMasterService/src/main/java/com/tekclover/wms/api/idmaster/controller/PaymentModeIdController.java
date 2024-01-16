package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.threepl.billingmodeid.BillingModeId;
import com.tekclover.wms.api.idmaster.model.threepl.billingmodeid.FindBillingModeId;
import com.tekclover.wms.api.idmaster.model.threepl.paymentmodeid.AddPaymentModeId;
import com.tekclover.wms.api.idmaster.model.threepl.paymentmodeid.FindPaymentModeId;
import com.tekclover.wms.api.idmaster.model.threepl.paymentmodeid.PaymentModeId;
import com.tekclover.wms.api.idmaster.model.threepl.paymentmodeid.UpdatePaymentModeId;
import com.tekclover.wms.api.idmaster.service.PaymentModeIdService;
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
@Api(tags={"PaymentModeId"},value = "PaymentModeId Operations related to PaymentModeIdController")
@SwaggerDefinition(tags={@Tag(name="PaymentModeId",description = "Operations related to PaymentModeId")})
@RequestMapping("/paymentmodeid")
@RestController
public class PaymentModeIdController {
    @Autowired
    PaymentModeIdService paymentModeIdService;

    @ApiOperation(response = PaymentModeId.class, value = "Get all PaymentModeId details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<PaymentModeId> PaymentModeIdList = paymentModeIdService.getPaymentModeIds();
        return new ResponseEntity<>(PaymentModeIdList, HttpStatus.OK);
    }
    @ApiOperation(response = PaymentModeId.class, value = "Get a PaymentModeId") // label for swagger
    @GetMapping("/{paymentModeId}")
    public ResponseEntity<?> getPaymentModeId(@RequestParam String warehouseId, @PathVariable Long paymentModeId,@RequestParam String companyCodeId,
                                              @RequestParam String languageId,@RequestParam String plantId) {
        PaymentModeId PaymentModeId =
                paymentModeIdService.getPaymentModeId(warehouseId,paymentModeId,companyCodeId,languageId,plantId);
        log.info("PaymentModeId : " + PaymentModeId);
        return new ResponseEntity<>(PaymentModeId, HttpStatus.OK);
    }
    @ApiOperation(response = PaymentModeId.class, value = "Create PaymentModeId") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postPaymentModeId(@Valid @RequestBody AddPaymentModeId newPaymentModeId,
                                               @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        PaymentModeId createdPaymentModeId = paymentModeIdService.createPaymentModeId(newPaymentModeId, loginUserID);
        return new ResponseEntity<>(createdPaymentModeId , HttpStatus.OK);
    }
    @ApiOperation(response = PaymentModeId.class, value = "Update PaymentModeId") // label for swagger
    @PatchMapping("/{paymentModeId}")
    public ResponseEntity<?> patchPaymentModeId(@RequestParam String warehouseId, @PathVariable Long paymentModeId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String  plantId,
                                                @Valid @RequestBody UpdatePaymentModeId updatePaymentModeId, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PaymentModeId createdPaymentModeId =
                paymentModeIdService.updatePaymentModeId(warehouseId, paymentModeId,companyCodeId,languageId,plantId,loginUserID, updatePaymentModeId);
        return new ResponseEntity<>(createdPaymentModeId , HttpStatus.OK);
    }
    @ApiOperation(response = PaymentModeId.class, value = "Delete PaymentModeId") // label for swagger
    @DeleteMapping("/{paymentModeId}")
    public ResponseEntity<?> deletePaymentModeId(@PathVariable Long paymentModeId,
                                                 @RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,
                                                 @RequestParam String plantId,@RequestParam String loginUserID) {
        paymentModeIdService.deletePaymentModeId(warehouseId,paymentModeId,companyCodeId,languageId,plantId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //Search
    @ApiOperation(response = PaymentModeId.class, value = "Find PaymentModeId") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findPaymentModeId(@Valid @RequestBody FindPaymentModeId findPaymentModeId) throws Exception {
        List<PaymentModeId> createdPaymentModeId = paymentModeIdService.findPaymentModeId(findPaymentModeId);
        return new ResponseEntity<>(createdPaymentModeId, HttpStatus.OK);
    }
}
