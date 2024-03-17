package com.mnrclara.api.cg.transaction.controller;

//import com.mnrclara.api.cg.transaction.batch.scheduler.BatchJobScheduler;
import com.mnrclara.api.cg.transaction.model.storepartnerlisting.*;
import com.mnrclara.api.cg.transaction.service.StorePartnerListingService;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"StorePartnerListing"}, value = "StorePartnerListing Operations related to StorePartnerListingController")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "StorePartnerListing", description = "Operations related to StorePartnerListing")})
@RequestMapping("/storepartnerlisting")
@RestController
public class StorePartnerListingController {

    @Autowired
    private StorePartnerListingService storePartnerListingService;

//    @Autowired
//    BatchJobScheduler batchJobScheduler;

    // GET ALL
    @ApiOperation(response = StorePartnerListing.class, value = "Get all StorePartnerListing details")// label for swagger
    @GetMapping("")
    public ResponseEntity<?> getALl() {

        List<StorePartnerListing> storePartnerListingList =
                storePartnerListingService.getAllStorePartnerListing();
        return new ResponseEntity<>(storePartnerListingList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = StorePartnerListing.class, value = "Get a StorePartnerListing") // label for swagger
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStorePartnerListing(@PathVariable String storeId, @RequestParam Long versionNumber,
                                                    @RequestParam String languageId, @RequestParam String companyId) {

        StorePartnerListing storePartnerListing =
                storePartnerListingService.getStorePartnerListing(versionNumber, storeId, companyId, languageId);
        log.info("StorePartnerListing : " + storePartnerListing);
        return new ResponseEntity<>(storePartnerListing, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = StorePartnerListing.class, value = "Create StorePartnerListing") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addStorePartnerListing(@Valid @RequestBody AddStorePartnerListing newStorePartnerListing,
                                                    @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing createStorePartnerListing =
                storePartnerListingService.createStorePartnerListing(newStorePartnerListing, loginUserID);
        return new ResponseEntity<>(createStorePartnerListing, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = StorePartnerListing.class, value = "Update StorePartnerListing") // label for swagger
    @PatchMapping("/{storeId}")
    public ResponseEntity<?> patchStorePartnerListing(@PathVariable String storeId, @RequestParam Long versionNumber,
                                                      @RequestParam String languageId, @RequestParam String loginUserID,
                                                      @RequestBody UpdateStorePartnerListing updateStorePartnerListing,
                                                      @RequestParam String companyId)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing dbStorePartnerListing =
                storePartnerListingService.updateStorepartnerListing(
                        versionNumber, storeId, languageId, companyId, loginUserID, updateStorePartnerListing);
        return new ResponseEntity<>(dbStorePartnerListing, HttpStatus.OK);
    }

    // UPDATE BATCH
    @ApiOperation(response = StorePartnerListing.class, value = "Batch Update StorePartnerListing") // label for swagger
    @PatchMapping("/batchUpdate")
    public ResponseEntity<?> batchUpdateStorePartnerListing(@RequestParam String loginUserID,
                                                      @RequestBody List<UpdateStorePartnerListing> updateStorePartnerListing)
            throws IllegalAccessException, InvocationTargetException {

        List<StorePartnerListing> dbStorePartnerListing =
                storePartnerListingService.batchUpdateStorePartner(loginUserID, updateStorePartnerListing);
        return new ResponseEntity<>(dbStorePartnerListing, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = StorePartnerListing.class, value = "Delete StorePartnerListing") // label for swagger
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStoreId(@PathVariable String storeId, @RequestParam Long versionNumber,
                                           @RequestParam String companyId, @RequestParam String languageId,
                                           @RequestParam String loginUserID) {

        storePartnerListingService.deleteStorePartnerListingService(versionNumber, storeId, companyId, languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = StorePartnerListing.class, value = "Find StorePartnerListing") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findStorePartnerListing(@Valid @RequestBody FindStorePartnerListing findStorePartnerListing)
            throws Exception {
        List<StorePartnerListing> createStorePartnerListing =
                storePartnerListingService.findStorePartnerListing(findStorePartnerListing);
        return new ResponseEntity<>(createStorePartnerListing, HttpStatus.OK);
    }

    // SEARCH
    @ApiOperation(response = StorePartnerListingImpl.class, value = "Find StorePartnerListing by Version") // label for swagger
    @PostMapping("/find/version")
    public ResponseEntity<?> findStorePartnerListingByVersion(@Valid @RequestBody FindStorePartnerListing findStorePartnerListing)
            throws Exception {
        List<StorePartnerListingImpl> findStorePartnerListingByVersion =
                storePartnerListingService.findStorePartnerListingByVersion(findStorePartnerListing);
        return new ResponseEntity<>(findStorePartnerListingByVersion, HttpStatus.OK);
    }

    @ApiOperation(response = LikeMatchResult.class, value = "Find CoOwner")
    @PostMapping("/findMatchResult")
    public ResponseEntity<?> findCoOwner(@Valid @RequestBody FindMatchResult findMatchResult) throws Exception {
        List<MatchResult> createLikeMatchResult =
                storePartnerListingService.findMatchResult(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    @ApiOperation(response = Group.class, value = "Find Group")
    @PostMapping("/findGroup")
    public ResponseEntity<?> findGroup(@Valid @RequestBody FindMatchResult findMatchResult) throws Exception {
        Group createLikeMatchResult =
                storePartnerListingService.findGroup(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    @ApiOperation(response = Group.class, value = "Find Group Count")
    @PostMapping("/findGroup/count")
    public ResponseEntity<?> findGroupCount(@Valid @RequestBody FindMatchResult findMatchResult) throws Exception {
        Group createLikeMatchResult =
                storePartnerListingService.findGroupCount(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    @ApiOperation(response = Group.class, value = "Find Store Entity")
    @PostMapping("/findStore/entity")
    public ResponseEntity<?> findStoreEntity(@Valid @RequestBody FindMatchResult findMatchResult) throws Exception {
        Group createLikeMatchResult =
                storePartnerListingService.findEntity(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    @ApiOperation(response = GroupStoreList.class,value = "Find All GroupStoreList")
    @GetMapping("/groupStore")
    public ResponseEntity<?> getAllGroupStoreList(){
        GroupStoreList groupStoreList = storePartnerListingService.getAllGroupStore();
        return new ResponseEntity<>(groupStoreList, HttpStatus.OK);
    }

    @ApiOperation(response = Group.class, value = "Find Brother Sister Group")
    @PostMapping("/findBrotherSister")
    public ResponseEntity<?> findGroupList(@Valid @RequestBody FindMatchResults findMatchResult) throws Exception {
        BrotherSisterResult createLikeMatchResult =
                storePartnerListingService.findStore(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    //upload csv insert
//    @ApiOperation(response = UploadFileResponse.class, value = "Upload File - StorePartnerListing") // label for swagger
//    @PostMapping("/batchupload")
//    public ResponseEntity<?> batchUploadStorePartnerListing(@RequestParam("file") MultipartFile file)
//            throws Exception {
//        batchJobScheduler.runJobStorePartnerListing();
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    // UPDATE
//    @ApiOperation(response = StorePartnerListing.class, value = "Update StorePartner") // label for swagger
//    @PatchMapping("/updateStorePartner")
//    public ResponseEntity<?> updateStorePartner(@RequestParam Long versionNumber, @RequestParam String languageId,@RequestParam String storeId,
//                                                    @RequestParam String loginUserID, @RequestParam String companyId,
//                                                    @RequestBody AddStorePartnerListing addStorePartnerListing)
//            throws IllegalAccessException, InvocationTargetException {
//
//        StorePartnerListing dbStorePartnerListing =
//                storePartnerListingService.updateStorePartner(versionNumber,storeId,languageId,companyId,loginUserID,addStorePartnerListing);
//        return new ResponseEntity<>(dbStorePartnerListing, HttpStatus.OK);
//    }

    //
@ApiOperation(response = ResponceObject.class, value = "Find Responce")
@PostMapping("/findResponse")
public ResponseEntity<?> findResponse(@Valid @RequestBody FindMatchResult findMatchResult) throws Exception {
    ResponceObject createLikeMatchResult =
            storePartnerListingService.findResponseObject(findMatchResult);
    return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
}

    @ApiOperation(response = MatchResultResponse.class, value = "Find MatchResultResponse")
    @PostMapping("/findMatchResultResponse")
    public ResponseEntity<?> findMatchResultResponse(@Valid @RequestBody List<MatchResultResponse> findMatchResult) throws Exception {
        List<MatchResult> createLikeMatchResult =
                storePartnerListingService.findMatchResultResponse(findMatchResult);
        return new ResponseEntity<>(createLikeMatchResult, HttpStatus.OK);
    }

    @ApiOperation(response = StorePartnerListing.class, value = "Upload StorePartnerListing") // label for swagger
    @PostMapping("/batchupload")
    public ResponseEntity<?> postSPUploadV2(@Valid @RequestBody List<StorePartnerListing> storePartnerListings)
            throws IllegalAccessException, InvocationTargetException {
        try {
            List<WarehouseApiResponse> responseList = new ArrayList<>();
            for (StorePartnerListing listing : storePartnerListings) {
                StorePartnerListing createdStorePartnerListing =
                        storePartnerListingService.createStorePartnerListing(listing);
                if (createdStorePartnerListing != null) {
                    WarehouseApiResponse response = new WarehouseApiResponse();
                    response.setStatusCode("200");
                    response.setMessage("Success");
                    responseList.add(response);
                }
            }
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            log.info("interWarehouseTransfer order Error: " + e);
            e.printStackTrace();
            WarehouseApiResponse response = new WarehouseApiResponse();
            response.setStatusCode("1400");
            response.setMessage("Not Success: " + e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }


}
