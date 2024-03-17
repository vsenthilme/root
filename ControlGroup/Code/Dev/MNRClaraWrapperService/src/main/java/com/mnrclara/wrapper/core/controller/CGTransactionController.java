package com.mnrclara.wrapper.core.controller;

import com.mnrclara.wrapper.core.model.cgtransaction.*;
import com.mnrclara.wrapper.core.service.CGTransactionService;
import com.mnrclara.wrapper.core.service.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-cg-transaction-service")
@Api(tags = {"CGTransaction Services"}, value = "Control Group Transaction Services") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CG Transaction", description = "Operations related to TransactionService")})
public class CGTransactionController {

    @Autowired
    private CGTransactionService cgTransactionService;

    @Autowired
    private FileStorageService fileStorageService;

    /*
     * --------------------------------OwnerShipRequest---------------------------------
     */
    @ApiOperation(response = OwnerShipRequest[].class, value = "Get all OwnerShipRequest details") // label for swagger
    @GetMapping("/ownershiprequest")
    public ResponseEntity<?> getAllOwnerShipRequest(@RequestParam String authToken) {
        OwnerShipRequest[] dbOwnerShipRequest = cgTransactionService.getAllOwnerShipRequest(authToken);
        return new ResponseEntity<>(dbOwnerShipRequest, HttpStatus.OK);
    }

    @ApiOperation(response = OwnerShipRequest.class, value = "Get a OwnerShipRequest") // label for swagger
    @GetMapping("/ownershiprequest/{requestId}")
    public ResponseEntity<?> getOwnerShipRequest(@PathVariable Long requestId, @RequestParam String companyId,
                                                 @RequestParam String languageId, @RequestParam String authToken) {

        OwnerShipRequest dbOwnerShipRequest =
                cgTransactionService.getOwnerShipRequest(languageId, companyId, requestId, authToken);
        return new ResponseEntity<>(dbOwnerShipRequest, HttpStatus.OK);
    }

    @ApiOperation(response = OwnerShipRequest.class, value = "Create a OwnerShipRequest") // label for swagger
    @PostMapping("/ownershiprequest")
    public ResponseEntity<?> postOwnerShipRequest(@Valid @RequestBody OwnerShipRequest newOwnerShipRequest,
                                                  @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        OwnerShipRequest createOwnerShipRequest =
                cgTransactionService.createOwnerShipRequest(newOwnerShipRequest, loginUserID, authToken);
        return new ResponseEntity<>(createOwnerShipRequest, HttpStatus.OK);
    }

    @ApiOperation(response = OwnerShipRequest.class, value = "Update OwnerShipRequest") // label for swagger
    @RequestMapping(value = "/ownershiprequest/{requestId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateOwnerShipRequest(@PathVariable Long requestId, @RequestParam String loginUserID,
                                                    @RequestParam String companyId, @RequestParam String languageId,
                                                    @Valid @RequestBody OwnerShipRequest updateOwnerShipRequest,
                                                    @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {

        OwnerShipRequest updateOwnerRequest =
                cgTransactionService.updateOwnerShipRequest(languageId, requestId, companyId,
                        loginUserID, updateOwnerShipRequest, authToken);
        return new ResponseEntity<>(updateOwnerRequest, HttpStatus.OK);
    }

    @ApiOperation(response = OwnerShipRequest.class, value = "Delete OwnerShipRequest") // label for swagger
    @DeleteMapping("/ownershiprequest/{requestId}")
    public ResponseEntity<?> deleteLanguageId(@PathVariable Long requestId, @RequestParam String companyId,
                                              @RequestParam String languageId, @RequestParam String loginUserID,
                                              @RequestParam String authToken) {

        cgTransactionService.deleteOwnerShipRequest(languageId, companyId, requestId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = OwnerShipRequest[].class, value = "Find OwnerShipRequest")//label for swagger
    @PostMapping("/ownershiprequest/findownershiprequest")
    public OwnerShipRequest[] findOwnerShipRequest(@RequestBody FindOwnerShipRequest findOwnerShipRequest,
                                                   @RequestParam String authToken) throws Exception {
        return cgTransactionService.findOwnerShipRequest(findOwnerShipRequest, authToken);
    }

//    @ApiOperation(response = OwnerShipRequest.class, value = "Update RequestId") // label for swagger
//    @RequestMapping(value = "/ownershiprequest/updateRequestId", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateRequestId(@RequestParam Long requestId, @RequestParam String loginUserID,
//                                                    @RequestParam String companyId, @RequestParam String languageId,
//                                                    @Valid @RequestBody OwnerShipRequest updateOwnerShipRequest,
//                                                    @RequestParam String authToken)
//            throws IllegalAccessException, InvocationTargetException {
//
//        OwnerShipRequest updateOwnerRequest =
//                cgTransactionService.updateRequestId(languageId, requestId, companyId,
//                        loginUserID, updateOwnerShipRequest, authToken);
//        return new ResponseEntity<>(updateOwnerRequest, HttpStatus.OK);
//    }

    /*
     * --------------------------------StorePartnerListing---------------------------------
     */
    @ApiOperation(response = StorePartnerListing[].class, value = "Get all StorePartnerListing details")
    // label for swagger
    @GetMapping("/storepartnerlisting")
    public ResponseEntity<?> getAllStorePartnerListing(@RequestParam String authToken) {
        StorePartnerListing[] dbStorePartnerListing = cgTransactionService.getAllStorePartnerListing(authToken);
        return new ResponseEntity<>(dbStorePartnerListing, HttpStatus.OK);
    }

    @ApiOperation(response = StorePartnerListing.class, value = "Get a StorePartnerListing") // label for swagger
    @GetMapping("/storepartnerlisting/{storeId}")
    public ResponseEntity<?> getStorePartnerListing(@PathVariable String storeId, @RequestParam Long versionNumber,
                                                    @RequestParam String companyId, @RequestParam String languageId,
                                                    @RequestParam String authToken) {

        StorePartnerListing dbStorePartnerListing =
                cgTransactionService.getStorePartnerListing(languageId, versionNumber, storeId, companyId, authToken);
        return new ResponseEntity<>(dbStorePartnerListing, HttpStatus.OK);
    }

    @ApiOperation(response = StorePartnerListing.class, value = "Create a StorePartnerListing") // label for swagger
    @PostMapping("/storepartnerlisting")
    public ResponseEntity<?> postStorePartnerListing(@Valid @RequestBody StorePartnerListing newStorePartnerListing,
                                                     @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing createStorePartner =
                cgTransactionService.createStorePartnerListing(newStorePartnerListing, loginUserID, authToken);
        return new ResponseEntity<>(createStorePartner, HttpStatus.OK);
    }

    @ApiOperation(response = StorePartnerListing.class, value = "Update StorePartnerListing") // label for swagger
    @RequestMapping(value = "/storepartnerlisting/{storeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStorePartnerListing(@PathVariable String storeId, @RequestParam Long versionNumber,
                                                       @RequestParam String loginUserID, @RequestParam String companyId,
                                                       @RequestParam String languageId, @RequestParam String authToken,
                                                       @Valid @RequestBody StorePartnerListing storePartnerListing)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing updateStorePartnerListing =
                cgTransactionService.updateStorePartnerListing(languageId, versionNumber, storeId, companyId,
                        loginUserID, storePartnerListing, authToken);
        return new ResponseEntity<>(updateStorePartnerListing, HttpStatus.OK);
    }


    @ApiOperation(response = StorePartnerListing.class, value = "Batch Update StorePartnerListing") // label for swagger
    @RequestMapping(value = "/storepartnerlisting/batchUpdate", method = RequestMethod.PATCH)
    public ResponseEntity<?> batchUpdateStorePartnerListing(@RequestParam String loginUserID, @RequestParam String authToken,
                                                       @Valid @RequestBody List<StorePartnerListing> storePartnerListing)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing[] updateStorePartnerListing =
                cgTransactionService.batchUpdateStorePartnerListing(loginUserID, storePartnerListing, authToken);
        return new ResponseEntity<>(updateStorePartnerListing, HttpStatus.OK);
    }


    @ApiOperation(response = StorePartnerListing.class, value = "Delete StorePartnerListing") // label for swagger
    @DeleteMapping("/storepartnerlisting/{storeId}")
    public ResponseEntity<?> deleteStorePartnerListing(@PathVariable String storeId, @RequestParam Long versionNumber,
                                                       @RequestParam String companyId, @RequestParam String languageId,
                                                       @RequestParam String loginUserID, @RequestParam String authToken) {

        cgTransactionService.deleteStorePartnerListing(languageId, companyId, storeId, versionNumber, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = StorePartnerListing[].class, value = "Find StorePartnerListing")//label for swagger
    @PostMapping("/storepartnerlisting/findStorePartnerListing")
    public StorePartnerListing[] findStorePartnerListing(@RequestBody FindStorePartnerListing findStorePartnerListing,
                                                         @RequestParam String authToken) throws Exception {
        return cgTransactionService.findStorePartnerListing(findStorePartnerListing, authToken);
    }

    //FIND by Version
    @ApiOperation(response = StorePartnerListing[].class, value = "Find StorePartnerListing By Version")
//label for swagger
    @PostMapping("/storepartnerlisting/findStorePartnerListingByVersion")
    public StorePartnerListing[] findStorePartnerListingByVersion(@RequestBody FindStorePartnerListing findStorePartnerListing,
                                                                  @RequestParam String authToken) throws Exception {
        return cgTransactionService.findStorePartnerListingByVersion(findStorePartnerListing, authToken);
    }

    // FIND MATCH RESULT
    @ApiOperation(response = MatchResult[].class, value = "Find MatchResult")// label for swagger
    @PostMapping("/storepartnerlisting/findMatchResult")
    public MatchResult[] findCoOwner(@RequestBody FindMatchResult findMatchResult,
                                     @RequestParam String authToken) throws Exception {
        return cgTransactionService.findMatchResult(findMatchResult, authToken);
    }

    // Find GROUP
    @ApiOperation(response = Group.class, value = "Find Group")// label for swagger
    @PostMapping("/storepartnerlisting/findGroup")
    public Group findGroup(@RequestBody FindMatchResult findMatchResult,
                           @RequestParam String authToken) throws Exception {
        return cgTransactionService.findGroup(findMatchResult, authToken);
    }

    @ApiOperation(response = Group.class, value = "Find Group Count")// label for swagger
    @PostMapping("/storepartnerlisting/findGroup/count")
    public Group findGroupCount(@RequestBody FindMatchResult findMatchResult,
                           @RequestParam String authToken) throws Exception {
        return cgTransactionService.findGroupCount(findMatchResult, authToken);
    }

    @ApiOperation(response = Group.class, value = "Find Group Entity")// label for swagger
    @PostMapping("/storepartnerlisting/findStore/entity")
    public Group findGroupEntity(@RequestBody FindMatchResult findMatchResult,
                           @RequestParam String authToken) throws Exception {
        return cgTransactionService.findStoreEntity(findMatchResult, authToken);
    }

    @ApiOperation(response = GroupStoreList.class, value = "Find All GroupStoreList") // label for swagger
    @GetMapping("/storepartnerlisting/findGroupStore")
    public ResponseEntity<?> findGroupStore(@RequestParam String authToken) {

        GroupStoreList dbGroupStoreList = cgTransactionService.getAllGroupStore(authToken);
        return new ResponseEntity<>(dbGroupStoreList, HttpStatus.OK);

    }

    // FIND MATCH RESULT
    @ApiOperation(response = MatchResult[].class, value = "Find BrotherSister")// label for swagger
    @PostMapping("/storepartnerlisting/findBrotherSister")
    public BrotherSisterResult findBrotherSister(@RequestBody FindMatchResults findMatchResult,
                                                 @RequestParam String authToken) throws Exception {
        return cgTransactionService.findBrotherSisterGroup(findMatchResult, authToken);
    }

//    @ApiOperation(response = StorePartnerListing.class, value = "Update StorePartnerListing") // label for swagger
//    @RequestMapping(value = "/storepartnerlisting", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateStorePartner(@RequestParam String storeId, @RequestParam Long versionNumber,
//                                                       @RequestParam String loginUserID, @RequestParam String companyId,
//                                                       @RequestParam String languageId, @RequestParam String authToken,
//                                                       @Valid @RequestBody StorePartnerListing storePartnerListing)
//            throws IllegalAccessException, InvocationTargetException {
//
//        StorePartnerListing updateStorePartnerListing =
//                cgTransactionService.updateStorePartner(versionNumber,languageId,storeId,companyId,loginUserID,storePartnerListing,authToken);
//        return new ResponseEntity<>(updateStorePartnerListing, HttpStatus.OK);
//    }

    // FIND MATCH RESULT
    @ApiOperation(response = ResponceObject[].class, value = "Find ReponseModel")// label for swagger
    @PostMapping("/storepartnerlisting/findResponse")
    public ResponceObject findReponse(@RequestBody FindMatchResult findMatchResult,
                                      @RequestParam String authToken) throws Exception {
        return cgTransactionService.findResponse(findMatchResult, authToken);
    }

    // FIND MATCH RESULT RESPONSE
    @ApiOperation(response = MatchResultResponse[].class, value = "Find Match Result Response")// label for swagger
    @PostMapping("/storepartnerlisting/findMatchResultResponse")
    public MatchResult[] findReponse(@RequestBody MatchResultResponse[] findMatchResult,
                                     @RequestParam String authToken) throws Exception {
        return cgTransactionService.findMatchResponse(findMatchResult, authToken);
    }

    /*
     * --------------------------------BSEffectiveControl---------------------------------
     */

    @ApiOperation(response = BSEffectiveControl[].class, value = "Get all BSEffectiveControl details")
    // label for swagger
    @GetMapping("/bseffectivecontrol")
    public ResponseEntity<?> getAllBSEffectiveControl(@RequestParam String authToken) {
        BSEffectiveControl[] dbBSEffectiveControl = cgTransactionService.getAllBSEffectiveControl(authToken);
        return new ResponseEntity<>(dbBSEffectiveControl, HttpStatus.OK);
    }

    @ApiOperation(response = BSEffectiveControl.class, value = "Get a BSEffectiveControl") // label for swagger
    @GetMapping("/bseffectivecontrol/{validationId}")
    public ResponseEntity<?> getBSEffectiveControl(@PathVariable Long validationId, @RequestParam String companyId,
                                                   @RequestParam String languageId, @RequestParam String authToken) {

        BSEffectiveControl dbBSEffectiveControl =
                cgTransactionService.getBSEffectiveControl(languageId, validationId, companyId, authToken);
        return new ResponseEntity<>(dbBSEffectiveControl, HttpStatus.OK);
    }

    @ApiOperation(response = BSEffectiveControl.class, value = "Create a BSEffectiveControl") // label for swagger
    @PostMapping("/bseffectivecontrol")
    public ResponseEntity<?> postBSEffectiveControl(@Valid @RequestBody BSEffectiveControl newBSEffectiveControl,
                                                    @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        BSEffectiveControl createBSEffectiveControl =
                cgTransactionService.createBSEffectiveControl(newBSEffectiveControl, loginUserID, authToken);
        return new ResponseEntity<>(createBSEffectiveControl, HttpStatus.OK);
    }

    @ApiOperation(response = BSEffectiveControl.class, value = "Update BSEffectiveControl") // label for swagger
    @RequestMapping(value = "/bseffectivecontrol/{validationId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateBSEffectiveControl(@PathVariable Long validationId, @RequestParam String loginUserID,
                                                      @RequestParam String companyId, @RequestParam String languageId,
                                                      @RequestParam String authToken, @Valid @RequestBody BSEffectiveControl bsEffectiveControl)
            throws IllegalAccessException, InvocationTargetException {

        BSEffectiveControl updateBSEffectiveControl =
                cgTransactionService.updateBsEffectiveControl(languageId, validationId, companyId,
                        loginUserID, bsEffectiveControl, authToken);
        return new ResponseEntity<>(updateBSEffectiveControl, HttpStatus.OK);
    }

    @ApiOperation(response = BSEffectiveControl.class, value = "Delete BSEffectiveControl") // label for swagger
    @DeleteMapping("/bseffectivecontrol/{validationId}")
    public ResponseEntity<?> deleteBSEffectiveControl(@PathVariable Long validationId, @RequestParam String companyId,
                                                      @RequestParam String languageId, @RequestParam String loginUserID,
                                                      @RequestParam String authToken) {

        cgTransactionService.deleteBSEffectiveControl(languageId, companyId, validationId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = BSEffectiveControl[].class, value = "Find BSEffectiveControl")//label for swagger
    @PostMapping("/bseffectivecontrol/findBSEffectiveControl")
    public BSEffectiveControl[] findBSEffectiveControl(@RequestBody FindBSEffectiveControl findBSEffectiveControl,
                                                       @RequestParam String authToken) throws Exception {
        return cgTransactionService.findBSEffectiveControl(findBSEffectiveControl, authToken);
    }


    /*
     * --------------------------------BSControllingInterest---------------------------------
     */
    @ApiOperation(response = BSControllingInterest[].class, value = "Get all BSControllingInterest details")
    // label for swagger
    @GetMapping("/bscontrollinginterest")
    public ResponseEntity<?> getAllControllingInterest(@RequestParam String authToken) {

        BSControllingInterest[] dbBSControllingInterest =
                cgTransactionService.getAllBSControllingInterest(authToken);
        return new ResponseEntity<>(dbBSControllingInterest, HttpStatus.OK);
    }

    @ApiOperation(response = BSControllingInterest.class, value = "Get a BSControllingInterest") // label for swagger
    @GetMapping("/bscontrollinginterest/{validationId}")
    public ResponseEntity<?> getBSControllingInterest(@PathVariable Long validationId, @RequestParam String companyId,
                                                      @RequestParam String languageId, @RequestParam String authToken) {

        BSControllingInterest dbBSControllingInterest =
                cgTransactionService.getBSControllingInterest(languageId, validationId, companyId, authToken);
        return new ResponseEntity<>(dbBSControllingInterest, HttpStatus.OK);
    }

    @ApiOperation(response = BSControllingInterest.class, value = "Create a BSControllingInterest") // label for swagger
    @PostMapping("/bscontrollinginterest")
    public ResponseEntity<?> postBSControllingInterest(@Valid @RequestBody BSControllingInterest newBSControllingInterest,
                                                       @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        BSControllingInterest createBSControllingInterest =
                cgTransactionService.createBSControllingInterest(newBSControllingInterest, loginUserID, authToken);
        return new ResponseEntity<>(createBSControllingInterest, HttpStatus.OK);
    }

    @ApiOperation(response = BSControllingInterest.class, value = "Update BSControllingInterest") // label for swagger
    @RequestMapping(value = "/bscontrollinginterest/{validationId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateBSControllingInterest(@PathVariable Long validationId, @RequestParam String loginUserID,
                                                         @RequestParam String companyId, @RequestParam String languageId,
                                                         @RequestParam String authToken, @Valid @RequestBody BSControllingInterest bsControllingInterest)
            throws IllegalAccessException, InvocationTargetException {

        BSControllingInterest updateBSControllingInterest =
                cgTransactionService.updateBSControllingInterest(languageId, validationId, companyId,
                        loginUserID, bsControllingInterest, authToken);
        return new ResponseEntity<>(updateBSControllingInterest, HttpStatus.OK);
    }

    @ApiOperation(response = BSControllingInterest.class, value = "Delete BSControllingInterest") // label for swagger
    @DeleteMapping("/bscontrollinginterest/{validationId}")
    public ResponseEntity<?> deleteBSControllingInterest(@PathVariable Long validationId, @RequestParam String companyId,
                                                         @RequestParam String languageId, @RequestParam String loginUserID,
                                                         @RequestParam String authToken) {

        cgTransactionService.deleteBSControllingInterest(languageId, companyId, validationId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = BSControllingInterest[].class, value = "Find BSControllingInterest")//label for swagger
    @PostMapping("/bscontrollinginterest/findBSControllingInterest")
    public BSControllingInterest[] findBSControllingInterest(@RequestBody FindBSControllingInterest findBSControllingInterest,
                                                             @RequestParam String authToken) throws Exception {
        return cgTransactionService.findBSControllingInterest(findBSControllingInterest, authToken);
    }


    /*
     * --------------------------------REQUEST_ID---------------------------------
     */

    @ApiOperation(response = RequestId[].class, value = "Get all RequestId")
    // label for swagger
    @GetMapping("/requestId")
    public ResponseEntity<?> getAllRequestId(@RequestParam String authToken) {
        RequestId[] dbRequestId = cgTransactionService.getAllRequestIds(authToken);
        return new ResponseEntity<>(dbRequestId, HttpStatus.OK);
    }

    @ApiOperation(response = RequestId.class, value = "Get a RequestId") // label for swagger
    @GetMapping("/requestId/{requestId}")
    public ResponseEntity<?> getRequestId(@PathVariable Long requestId, @RequestParam String authToken) {

        RequestId[] dbRequestId =
                cgTransactionService.getRequestId(requestId, authToken);
        return new ResponseEntity<>(dbRequestId, HttpStatus.OK);
    }

    @ApiOperation(response = RequestId.class, value = "Create a RequestId") // label for swagger
    @PostMapping("/requestId")
    public ResponseEntity<?> postRequestId(@Valid @RequestBody RequestId[] newRequestId,
                                           @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        RequestId[] createRequestId =
                cgTransactionService.createRequestId(newRequestId, loginUserID, authToken);
        return new ResponseEntity<>(createRequestId, HttpStatus.OK);
    }

    @ApiOperation(response = RequestId.class, value = "Update RequestId") // label for swagger
    @RequestMapping(value = "/requestId/{requestId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateRequestId(@PathVariable Long requestId, @RequestParam String loginUserID,
                                             @RequestParam String authToken, @Valid @RequestBody List<RequestId> updateRequestId)
            throws IllegalAccessException, InvocationTargetException {

        RequestId[] updateRequest =
                cgTransactionService.patchRequestId(requestId, loginUserID, updateRequestId, authToken);
        return new ResponseEntity<>(updateRequest, HttpStatus.OK);
    }

    @ApiOperation(response = RequestId.class, value = "Delete RequestId") // label for swagger
    @DeleteMapping("/requestId/{requestId}")
    public ResponseEntity<?> deleteRequestId(@PathVariable Long requestId, @RequestParam Long id, @RequestParam String loginUserID,
                                             @RequestParam String authToken) {

        cgTransactionService.deleteRequestId(id,requestId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = RequestId[].class, value = "Find RequestId")//label for swagger
    @PostMapping("/requestId/findRequestId")
    public RequestId[] findRequestId(@RequestBody FindRequestId findRequestId,
                                     @RequestParam String authToken) throws Exception {
        return cgTransactionService.findRequestId(findRequestId, authToken);
    }

    //ReturnPo V2 Upload
    @ApiOperation(response = StorePartnerListing.class, value = "Create StorePartnerListing Upload")
    @PostMapping("storePartnerListing/upload")
    public ResponseEntity<?> createStorePartnerListing(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> response = fileStorageService.processStorePartnerListProcess(file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
