package com.mnrclara.wrapper.core.controller;

import com.mnrclara.wrapper.core.model.cgsetup.*;
import com.mnrclara.wrapper.core.model.cgtransaction.StoreDropDown;
import com.mnrclara.wrapper.core.model.crm.EMail;
import com.mnrclara.wrapper.core.service.CGSetupService;
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
import java.util.Optional;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-cg-setup-service")
@Api(tags = {"CGSetup Services"}, value = "Control Group Setup Services") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CG Setup", description = "Operations related to CGSetupService")})
public class CGSetupController {

    @Autowired
    private CGSetupService cgSetupService;


    /*
     * --------------------------------LanguageId---------------------------------
     */
    @ApiOperation(response = LanguageId[].class, value = "Get all LanguageId details") // label for swagger
    @GetMapping("/languageid")
    public ResponseEntity<?> getLanguageIds(@RequestParam String authToken) {
        LanguageId[] userLanguageId = cgSetupService.getLanguageIds(authToken);
        return new ResponseEntity<>(userLanguageId, HttpStatus.OK);
    }

    @ApiOperation(response = LanguageId.class, value = "Get a LanguageId") // label for swagger
    @GetMapping("/languageid/{languageId}")
    public ResponseEntity<?> getLanguageId(@PathVariable String languageId, @RequestParam String authToken) {

        LanguageId dbLanguageId = cgSetupService.getLanguageId(languageId, authToken);
        return new ResponseEntity<>(dbLanguageId, HttpStatus.OK);
    }

    @ApiOperation(response = LanguageId.class, value = "Create a LanguageId") // label for swagger
    @PostMapping("/languageid")
    public ResponseEntity<?> PostLanguageId(@Valid @RequestBody LanguageId newLanguageId,
                                            @RequestParam String loginUserID, String authToken)
            throws IllegalAccessException, InvocationTargetException {

        LanguageId createLanguageId = cgSetupService.createLanguageId(newLanguageId, loginUserID, authToken);
        return new ResponseEntity<>(createLanguageId, HttpStatus.OK);
    }

    @ApiOperation(response = LanguageId.class, value = "Update LanguageId") // label for swagger
    @RequestMapping(value = "/languageid/{languageId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateLanguageId(@PathVariable String languageId, @RequestParam String loginUserID,
                                              @Valid @RequestBody LanguageId updateLanguageId, @RequestParam String authToken)
            throws IllegalAccessException, InvocationTargetException {

        LanguageId UpdateLanguageId =
                cgSetupService.updateLanguageId(languageId, loginUserID, updateLanguageId, authToken);
        return new ResponseEntity<>(UpdateLanguageId, HttpStatus.OK);
    }

    @ApiOperation(response = LanguageId.class, value = "Delete LanguageId") // label for swagger
    @DeleteMapping("/languageid/{languageId}")
    public ResponseEntity<?> deleteLanguageId(@PathVariable String languageId, @RequestParam String loginUserID,
                                              @RequestParam String authToken) {

        cgSetupService.deleteLanguageId(languageId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = LanguageId[].class, value = "Find LanguageId")//label for swagger
    @PostMapping("/languageid/findlanguageid")
    public LanguageId[] findLanguageId(@RequestBody FindLanguageId findLanguageId,
                                       @RequestParam String authToken) throws Exception {
        return cgSetupService.findLanguageId(findLanguageId, authToken);
    }

    /*
     * --------------------------------CompanyId---------------------------------
     */
    @ApiOperation(response = CompanyId.class, value = "Get all CompanyId details") // label for swagger
    @GetMapping("/companyid")
    public ResponseEntity<?> getCompanyIds(@RequestParam String authToken) {
        CompanyId[] companyIdList = cgSetupService.getCompanyIds(authToken);
        return new ResponseEntity<>(companyIdList, HttpStatus.OK);
    }

    @ApiOperation(response = CompanyId.class, value = "Get a CompanyId") // label for swagger
    @GetMapping("/companyid/{companyId}")
    public ResponseEntity<?> getCompanyId(@PathVariable String companyId, @RequestParam String languageId,
                                          @RequestParam String authToken) {

        CompanyId dbCompanyId = cgSetupService.getCompanyId(companyId, languageId, authToken);
        log.info("CompanyId : " + dbCompanyId);
        return new ResponseEntity<>(dbCompanyId, HttpStatus.OK);
    }

    @ApiOperation(response = CompanyId.class, value = "Create CompanyId") // label for swagger
    @PostMapping("/companyid")
    public ResponseEntity<?> postCompanyId(@Valid @RequestBody CompanyId newCompanyId, @RequestParam String loginUserID,
                                           @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {

        CompanyId createdCompanyId = cgSetupService.createCompanyId(newCompanyId, loginUserID, authToken);
        return new ResponseEntity<>(createdCompanyId, HttpStatus.OK);
    }

    @ApiOperation(response = CompanyId.class, value = "Update CompanyId") // label for swagger
    @RequestMapping(value = "/companyid/{companyId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> patchCompanyId(@PathVariable String companyId, @RequestParam String languageId,
                                            @RequestParam String loginUserID, @RequestParam String authToken,
                                            @Valid @RequestBody CompanyId updateCompanyId)
            throws IllegalAccessException, InvocationTargetException {

        CompanyId updatedCompanyId =
                cgSetupService.updateCompanyId(companyId, languageId, loginUserID, updateCompanyId, authToken);
        return new ResponseEntity<>(updatedCompanyId, HttpStatus.OK);
    }

    @ApiOperation(response = CompanyId.class, value = "Delete CompanyId") // label for swagger
    @DeleteMapping("/companyid/{companyId}")
    public ResponseEntity<?> deleteCompanyId(@PathVariable String companyId, @RequestParam String languageId,
                                             @RequestParam String loginUserID, @RequestParam String authToken) {
        cgSetupService.deleteCompanyId(companyId, languageId, loginUserID, authToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = CompanyId[].class, value = "Find CompanyId")//label for swagger
    @PostMapping("/companyid/findcompanyid")
    public CompanyId[] findCompanyId(@RequestBody FindCompanyId findCompanyId,
                                     @RequestParam String authToken) throws Exception {
        return cgSetupService.findCompanyId(findCompanyId, authToken);
    }


    /* --------------------------------City-------------------------------------------------------------------------*/

    // GET ALL
    @ApiOperation(response = Optional.class, value = "Get All Cities") // label for swagger
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public ResponseEntity<?> getCities(@RequestParam String authToken) {
        City[] city = cgSetupService.getCities(authToken);
        log.info("getCities::: " + city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = Optional.class, value = "Get City") // label for swagger
    @RequestMapping(value = "/city/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCity(@PathVariable String cityId, @RequestParam String stateId,
                                     @RequestParam String countryId, @RequestParam String companyId,
                                     @RequestParam String languageId, @RequestParam String authToken) {
        City city = cgSetupService.getCity(cityId, companyId, stateId, countryId, languageId, authToken);
        log.info("getCity::: " + city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = Optional.class, value = "Create new city") // label for swagger
    @RequestMapping(value = "/city", method = RequestMethod.POST)
    public ResponseEntity<?> createCity(@RequestBody City newCity, @RequestParam String loginUserID,
                                        @RequestParam String authToken) {
        City city = cgSetupService.addCity(newCity, loginUserID, authToken);
        log.info("createCity::: " + city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = Optional.class, value = "Update City") // label for swagger
    @RequestMapping(value = "/city/{cityId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateCity(@PathVariable String cityId, @RequestParam String stateId,
                                        @RequestParam String countryId, @RequestParam String languageId,
                                        @RequestParam String companyId, @RequestParam String loginUserID,
                                        @RequestBody City updatedCity, @RequestParam String authToken) {

        City city = cgSetupService.updateCity(cityId, stateId, countryId, languageId, companyId, loginUserID, updatedCity, authToken);
        log.info("updateCity::: " + city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = Optional.class, value = "Delete City") // label for swagger
    @RequestMapping(value = "/city/{cityId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCity(@PathVariable String cityId, @RequestParam String stateId,
                                        @RequestParam String companyId, @RequestParam String countryId,
                                        @RequestParam String languageId, @RequestParam String loginUserID,
                                        @RequestParam String authToken) {

        boolean isCityDeleted = cgSetupService.deleteCity(cityId, stateId, countryId, companyId, languageId, loginUserID, authToken);
        log.info("isCityDeleted::: " + isCityDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find City")//label for swagger
    @PostMapping("/city/findcity")
    public City[] findCity(@RequestBody FindCity findCity,
                           @RequestParam String authToken) throws Exception {
        return cgSetupService.findCity(findCity, authToken);
    }

    /* --------------------------------Country-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All Country") // label for swagger
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public ResponseEntity<?> getCountries(@RequestParam String authToken) {
        Country[] country = cgSetupService.getCountries(authToken);
        log.info("getCities::: " + country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get Country") // label for swagger
    @RequestMapping(value = "/country/{countryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCountry(@PathVariable String countryId, @RequestParam String languageId,
                                        @RequestParam String companyId, @RequestParam String authToken) {

        Country country = cgSetupService.getCountry(countryId, companyId, languageId, authToken);
        log.info("getCountry::: " + country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new Country") // label for swagger
    @RequestMapping(value = "/country", method = RequestMethod.POST)
    public ResponseEntity<?> createCountry(@RequestBody Country newCountry, @RequestParam String loginUserID,
                                           @RequestParam String authToken) {
        Country country = cgSetupService.addCountry(newCountry, loginUserID, authToken);
        log.info("createCountry::: " + country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update Country") // label for swagger
    @RequestMapping(value = "/country/{countryId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateCountry(@PathVariable String countryId, @RequestParam String languageId,
                                           @RequestParam String companyId, @RequestParam String loginUserID,
                                           @RequestBody Country updatedCountry, @RequestParam String authToken) {

        Country country = cgSetupService.updateCountry(countryId, languageId, loginUserID, companyId, updatedCountry, authToken);
        log.info("updateCountry::: " + country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete Country") // label for swagger
    @RequestMapping(value = "/country/{countryId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCountry(@PathVariable String countryId, @RequestParam String languageId, @RequestParam String loginUserID,
                                           @RequestParam String companyId, @RequestParam String authToken) {
        boolean isCountryDeleted = cgSetupService.deleteCountry(countryId, languageId, companyId, loginUserID, authToken);
        log.info("isCountryDeleted::: " + isCountryDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find Country")//label for swagger
    @PostMapping("/country/findcountry")
    public Country[] findCountry(@RequestBody FindCountry findCountry,
                                 @RequestParam String authToken) throws Exception {
        return cgSetupService.findCountry(findCountry, authToken);
    }

    /* --------------------------------State-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All States") // label for swagger
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ResponseEntity<?> getStates(@RequestParam String authToken) {
        State[] state = cgSetupService.getStates(authToken);
        log.info("getStates::: " + state);
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get State") // label for swagger
    @RequestMapping(value = "/state/{stateId}", method = RequestMethod.GET)
    public ResponseEntity<?> getState(@PathVariable String stateId, @RequestParam String companyId,
                                      @RequestParam String languageId, @RequestParam String authToken) {
        State state = cgSetupService.getState(stateId, companyId, languageId, authToken);
        log.info("getState::: " + state);
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new State") // label for swagger
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public ResponseEntity<?> createState(@RequestBody State newState, @RequestParam String loginUserID,
                                         @RequestParam String authToken) {
        State state = cgSetupService.addState(newState, loginUserID, authToken);
        log.info("createState::: " + state);
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update State") // label for swagger
    @RequestMapping(value = "/state/{stateId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateState(@PathVariable String stateId, @RequestParam String languageId,
                                         @RequestParam String loginUserID, @RequestBody State updatedState, @RequestParam String companyId,
                                         @RequestParam String authToken) {
        State state = cgSetupService.updateState(stateId, languageId, loginUserID, companyId, updatedState, authToken);
        log.info("updateState::: " + state);
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete State") // label for swagger
    @RequestMapping(value = "/state/{stateId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteState(@PathVariable String stateId, @RequestParam String companyId, @RequestParam String loginUserID,
                                         @RequestParam String languageId, @RequestParam String authToken) {
        boolean isStateDeleted = cgSetupService.deleteState(stateId, companyId, languageId, loginUserID, authToken);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find State")//label for swagger
    @PostMapping("/state/findState")
    public State[] findState(@RequestBody FindState findState,
                             @RequestParam String authToken) throws Exception {
        return cgSetupService.findState(findState, authToken);
    }


    /* --------------------------------StoreId-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All StoreId") // label for swagger
    @RequestMapping(value = "/store", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStore(@RequestParam String authToken) {
        StoreId[] storeIds = cgSetupService.getAllStoreId(authToken);
        log.info("getStoreId::: " + storeIds);
        return new ResponseEntity<>(storeIds, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get StoreId") // label for swagger
    @RequestMapping(value = "/store/{storeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getStoreId(@PathVariable Long storeId, @RequestParam String companyId,
                                        @RequestParam String languageId, @RequestParam String authToken) {
        StoreId dbStoreId = cgSetupService.getStoreId(storeId, companyId, languageId, authToken);
        log.info("getStoreId::: " + dbStoreId);
        return new ResponseEntity<>(dbStoreId, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new StoreId") // label for swagger
    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public ResponseEntity<?> createStoreId(@RequestBody StoreId newStoreId, @RequestParam String loginUserID,
                                           @RequestParam String authToken) {
        StoreId dbStore = cgSetupService.addStoreId(newStoreId, loginUserID, authToken);
        log.info("createStore::: " + dbStore);
        return new ResponseEntity<>(dbStore, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update StoreId") // label for swagger
    @RequestMapping(value = "/store/{storeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStoreId(@PathVariable Long storeId, @RequestParam String languageId,
                                           @RequestParam String loginUserID, @RequestBody StoreId updatedStoreId,
                                           @RequestParam String companyId, @RequestParam String authToken) {

        StoreId dbStoreId = cgSetupService.updateStoreId(storeId, languageId, loginUserID, companyId, updatedStoreId, authToken);
        log.info("updateStore::: " + storeId);
        return new ResponseEntity<>(dbStoreId, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete StoreId") // label for swagger
    @RequestMapping(value = "/store/{storeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId, @RequestParam String companyId,
                                         @RequestParam String loginUserID, @RequestParam String languageId,
                                         @RequestParam String authToken) {

        boolean isStateDeleted = cgSetupService.deleteStoreId(storeId, companyId, languageId, loginUserID, authToken);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find StoreId")//label for swagger
    @PostMapping("/store/findStore")
    public StoreId[] findStoreId(@RequestBody FindStoreId findStoreId,
                                 @RequestParam String authToken) throws Exception {
        return cgSetupService.findStoreId(findStoreId, authToken);
    }

    @ApiOperation(response = StoreDropDown[].class, value = "Get All StoreDropDown ")
    @GetMapping("/getAllStoreGroupDown")
    public ResponseEntity<?> getAllStoreDropDown(@RequestParam String authToken){
        StoreDropDown[] dbStoreDropDown = cgSetupService.getStoreDropDown(authToken);
        return new ResponseEntity<>(dbStoreDropDown, HttpStatus.OK);
    }

    /* --------------------------------ControlGroupType-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All ControlGroupType") // label for swagger
    @RequestMapping(value = "/controlgrouptype", method = RequestMethod.GET)
    public ResponseEntity<?> getAllControlGroupType(@RequestParam String authToken) {
        ControlGroupType[] controlGroupTypes = cgSetupService.getAllControlGroupType(authToken);
        log.info("getControlGroupType::: " + controlGroupTypes);
        return new ResponseEntity<>(controlGroupTypes, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get ControlGroupType") // label for swagger
    @RequestMapping(value = "/controlgrouptype/{groupTypeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getControlGroupType(@PathVariable Long groupTypeId, @RequestParam String companyId,
                                                 @RequestParam String languageId, @RequestParam Long versionNumber,
                                                 @RequestParam String authToken) {

        ControlGroupType dbControlGroupType =
                cgSetupService.getControlGroupType(groupTypeId, companyId, languageId, versionNumber, authToken);
        log.info("getControlGroupTypeType::: " + dbControlGroupType);
        return new ResponseEntity<>(dbControlGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new ControlGroupType") // label for swagger
    @RequestMapping(value = "/controlgrouptype", method = RequestMethod.POST)
    public ResponseEntity<?> createControlGroupType(@RequestBody ControlGroupType newControlGroupType,
                                                    @RequestParam String loginUserID, @RequestParam String authToken) {

        ControlGroupType dbControlGroupType = cgSetupService.addControlGroupType(newControlGroupType, loginUserID, authToken);
        log.info("createControlGroupType::: " + dbControlGroupType);
        return new ResponseEntity<>(dbControlGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update ControlGroupType") // label for swagger
    @RequestMapping(value = "/controlgrouptype/{groupTypeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateControlGroupType(@PathVariable Long groupTypeId, @RequestParam String languageId,
                                                    @RequestParam String loginUserID, @RequestBody ControlGroupType updateControlGroupType,
                                                    @RequestParam String companyId, @RequestParam Long versionNumber, @RequestParam String authToken) {

        ControlGroupType dbControlGroup =
                cgSetupService.updateControlGroupType(groupTypeId, languageId, loginUserID, companyId,
                        updateControlGroupType, versionNumber, authToken);
        log.info("updateControlGroupType::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete ControlGroupType") // label for swagger
    @RequestMapping(value = "/controlgrouptype/{groupTypeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteControlGroupType(@PathVariable Long groupTypeId, @RequestParam String companyId,
                                                    @RequestParam String languageId, @RequestParam String authToken,
                                                    @RequestParam String loginUserID, @RequestParam Long versionNumber) {

        boolean isStateDeleted = cgSetupService.deleteControlGroupType(groupTypeId, companyId, languageId,
                versionNumber, loginUserID, authToken);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find ControlGroupType")//label for swagger
    @PostMapping("/controlgrouptype/findControlGroupType")
    public ControlGroupType[] findControlGroupType(@RequestBody FindControlGroupType findControlGroupType,
                                                   @RequestParam String authToken) throws Exception {
        return cgSetupService.findControlGroup(findControlGroupType, authToken);
    }

    /* --------------------------------SubGroup-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All SubGroupType") // label for swagger
    @RequestMapping(value = "/subgrouptype", method = RequestMethod.GET)
    public ResponseEntity<?> getAllSubGroup(@RequestParam String authToken) {
        SubGroupType[] subGroupTypes = cgSetupService.getAllSubGroupType(authToken);
        log.info("getSubGroupType::: " + subGroupTypes);
        return new ResponseEntity<>(subGroupTypes, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get SubGroupType") // label for swagger
    @RequestMapping(value = "/subgrouptype/{subGroupTypeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSubGroupType(@PathVariable Long subGroupTypeId, @RequestParam Long groupTypeId,
                                             @RequestParam String companyId, @RequestParam String languageId,
                                             @RequestParam Long versionNumber, @RequestParam String authToken) {

        SubGroupType dbSubGroupType =
                cgSetupService.getSubGroupType(subGroupTypeId, groupTypeId, companyId, versionNumber, languageId, authToken);
        log.info("getSubGroupType::: " + dbSubGroupType);
        return new ResponseEntity<>(dbSubGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new SubGroupType") // label for swagger
    @RequestMapping(value = "/subgrouptype", method = RequestMethod.POST)
    public ResponseEntity<?> createSubGroupType(@RequestBody SubGroupType newSubGroupType, @RequestParam String loginUserID,
                                                @RequestParam String authToken) {
        SubGroupType dbSubGroupType = cgSetupService.addSubGroupType(newSubGroupType, loginUserID, authToken);
        log.info("createSubGroupType::: " + dbSubGroupType);
        return new ResponseEntity<>(dbSubGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update SubGroupType") // label for swagger
    @RequestMapping(value = "/subGrouptype/{subGroupTypeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateSubGroupType(@PathVariable Long subGroupTypeId, @RequestParam String languageId,
                                                @RequestParam Long groupTypeId, @RequestParam String loginUserID,
                                                @RequestBody SubGroupType updateSubGroupType, @RequestParam String companyId,
                                                @RequestParam Long versionNumber, @RequestParam String authToken) {

        SubGroupType dbSubGroupType =
                cgSetupService.updateSubGroup(groupTypeId, subGroupTypeId, languageId, loginUserID,
                        companyId, versionNumber, updateSubGroupType, authToken);
        log.info("updateSubGroup::: " + dbSubGroupType);
        return new ResponseEntity<>(dbSubGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete SubGroupType") // label for swagger
    @RequestMapping(value = "/subgrouptype/{subGroupTypeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubGroupType(@PathVariable Long subGroupTypeId, @RequestParam Long groupTypeId,
                                                @RequestParam String companyId, @RequestParam String languageId,
                                                @RequestParam Long versionNumber, @RequestParam String authToken,
                                                @RequestParam String loginUserID) {

        boolean isStateDeleted =
                cgSetupService.deleteSubGroup(subGroupTypeId, groupTypeId, loginUserID,
                        versionNumber, companyId, languageId, authToken);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find SubGroupType")//label for swagger
    @PostMapping("/subgrouptype/findSubGroupType")
    public SubGroupType[] findSubGroup(@RequestBody FindSubGroupType findSubGroupType,
                                       @RequestParam String authToken) throws Exception {
        return cgSetupService.findSubGroupType(findSubGroupType, authToken);
    }

    /* --------------------------------Client-------------------------------------------------------------------------*/

    @ApiOperation(response = Optional.class, value = "Get All Client") // label for swagger
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClient(@RequestParam String authToken) {
        Client[] clients = cgSetupService.getAllClientId(authToken);
        log.info("getClient::: " + clients);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Get Client") // label for swagger
    @RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<?> getClient(@PathVariable Long clientId, @RequestParam String companyId,
                                       @RequestParam String languageId, @RequestParam String authToken) {

        Client dbClient = cgSetupService.getClientId(clientId, companyId, languageId, authToken);
        log.info("getClient::: " + dbClient);
        return new ResponseEntity<>(dbClient, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Create new Client") // label for swagger
    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<?> createClient(@RequestBody Client newClient, @RequestParam String loginUserID,
                                          @RequestParam String authToken) {
        Client dbClient = cgSetupService.addClient(newClient, loginUserID, authToken);
        log.info("createClient::: " + dbClient);
        return new ResponseEntity<>(dbClient, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Update Client") // label for swagger
    @RequestMapping(value = "/client/{clientId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateClient(@PathVariable Long clientId, @RequestParam String languageId,
                                          @RequestParam String loginUserID, @RequestBody Client updateClient,
                                          @RequestParam String companyId, @RequestParam String authToken) {
        Client dbClient =
                cgSetupService.updateClient(clientId, languageId, loginUserID, companyId, updateClient, authToken);
        log.info("updateSubGroup::: " + dbClient);
        return new ResponseEntity<>(dbClient, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Delete Client") // label for swagger
    @RequestMapping(value = "/client/{clientId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteClient(@PathVariable Long clientId, @RequestParam String companyId,
                                          @RequestParam String languageId,
                                          @RequestParam String authToken, @RequestParam String loginUserID) {
        boolean isClientDeleted = cgSetupService.deleteClient(clientId, loginUserID, companyId, languageId, authToken);
        log.info("isClientDeleted::: " + isClientDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find Client")//label for swagger
    @PostMapping("/client/findClient")
    public Client[] findSubGroup(@RequestBody FindClient findClient,
                                 @RequestParam String authToken) throws Exception {
        return cgSetupService.findClient(findClient, authToken);
    }


    /* --------------------------------Control Group-------------------------------------------------------------------------*/

    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All ControlGroup") // label for swagger
    @RequestMapping(value = "/controlgroup", method = RequestMethod.GET)
    public ResponseEntity<?> getAllControlGroup(@RequestParam String authToken) {
        ControlGroup[] controlGroups = cgSetupService.getAllControlGroup(authToken);
        log.info("getControlGroup::: " + controlGroups);
        return new ResponseEntity<>(controlGroups, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get ControlGroup") // label for swagger
    @RequestMapping(value = "/controlgroup/{groupId}", method = RequestMethod.GET)
    public ResponseEntity<?> getControlGroup(@PathVariable Long groupId, @RequestParam Long groupTypeId,
                                             @RequestParam String companyId, @RequestParam String languageId,
                                             @RequestParam Long versionNumber, @RequestParam String authToken) {

        ControlGroup dbControlGroup =
                cgSetupService.getControlGroup(groupId, groupTypeId, companyId, languageId, versionNumber, authToken);

        log.info("getControlGroup::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new ControlGroup") // label for swagger
    @RequestMapping(value = "/controlgroup", method = RequestMethod.POST)
    public ResponseEntity<?> createControlGroup(@RequestBody ControlGroup newControlGroup, @RequestParam String loginUserID,
                                                @RequestParam String authToken) {
        ControlGroup dbControlGroup =
                cgSetupService.addControlGroup(newControlGroup, loginUserID, authToken);

        log.info("createControlGroup::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update ControlGroup") // label for swagger
    @RequestMapping(value = "/controlgroup/{groupId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateControlGroup(@PathVariable Long groupId, @RequestParam String languageId,
                                                @RequestParam Long groupTypeId, @RequestParam String loginUserID,
                                                @RequestBody ControlGroup updateControlGroup, @RequestParam Long versionNumber,
                                                @RequestParam String companyId, @RequestParam String authToken) {
        ControlGroup dbControlGroup =
                cgSetupService.updateControlgroup(groupId, groupTypeId, languageId, loginUserID, companyId,
                        versionNumber, updateControlGroup, authToken);
        log.info("updateControlGroup::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete ControlGroup") // label for swagger
    @RequestMapping(value = "/controlgroup/{groupId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteControlGroup(@PathVariable Long groupId, @RequestParam String companyId,
                                                @RequestParam String languageId, @RequestParam Long groupTypeId,
                                                @RequestParam Long versionNumber, @RequestParam String authToken,
                                                @RequestParam String loginUserID) {

        boolean isControlGroupDeleted =
                cgSetupService.deleteControlGroup(groupId, groupTypeId, loginUserID, versionNumber, companyId, languageId, authToken);

        log.info("isControlGroupDeleted::: " + isControlGroupDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find ControlGroup")//label for swagger
    @PostMapping("/controlgroup/findControlGroup")
    public ControlGroup[] findControlGroup(@RequestBody FindControlGroup findControlGroup,
                                           @RequestParam String authToken) throws Exception {
        return cgSetupService.findControlGroup(findControlGroup, authToken);
    }

    /* --------------------------------Client Control Group-------------------------------------------------------------------------*/

    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All ClientControlGroup") // label for swagger
    @RequestMapping(value = "/clientcontrolgroup", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClientControlGroup(@RequestParam String authToken) {
        ClientControlGroup[] clientControlGroups = cgSetupService.getAllClientControlGroup(authToken);
        log.info("getAllClientControl::: " + clientControlGroups);
        return new ResponseEntity<>(clientControlGroups, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get ClientControlGroup") // label for swagger
    @RequestMapping(value = "/clientcontrolgroup/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<?> getClientControlGroup(@PathVariable Long clientId, @RequestParam Long groupTypeId,
                                                   @RequestParam String companyId, @RequestParam String languageId,
                                                   @RequestParam Long versionNumber, @RequestParam String authToken) {

        ClientControlGroup dbControlGroup =
                cgSetupService.getClientControlGroup(groupTypeId, clientId, companyId, languageId, versionNumber, authToken);
        log.info("getClientControlGroup::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new ClientControlGroup") // label for swagger
    @RequestMapping(value = "/clientcontrolgroup", method = RequestMethod.POST)
    public ResponseEntity<?> createClientControlGroup(@RequestBody ClientControlGroup newControlGroup,
                                                      @RequestParam String loginUserID, @RequestParam String authToken) {

        ClientControlGroup dbControlGroup =
                cgSetupService.addClientControlGroup(newControlGroup, loginUserID, authToken);
        log.info("createClientControlGroup::: " + dbControlGroup);
        return new ResponseEntity<>(dbControlGroup, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update ClientControlGroup") // label for swagger
    @RequestMapping(value = "/clientcontrolgroup/{clientId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateClientControlGroup(@PathVariable Long clientId, @RequestParam String languageId,
                                                      @RequestParam Long groupTypeId, @RequestParam String loginUserID,
                                                      @RequestBody ClientControlGroup updateControlGroup,
                                                      @RequestParam String companyId, @RequestParam Long versionNumber,
                                                      @RequestParam String authToken) {
        ClientControlGroup dbClientControl =
                cgSetupService.updateClientControlGroup(groupTypeId, clientId, languageId,
                        loginUserID, companyId, updateControlGroup, versionNumber, authToken);

        log.info("updateClientControlGroup::: " + dbClientControl);
        return new ResponseEntity<>(dbClientControl, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete ClientControlGroup") // label for swagger
    @RequestMapping(value = "/clientcontrolgroup/{clientId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteClientControlGroup(@PathVariable Long clientId, @RequestParam String companyId,
                                                      @RequestParam String languageId, @RequestParam Long groupTypeId,
                                                      @RequestParam String authToken, @RequestParam Long versionNumber,
                                                      @RequestParam String loginUserID) {
        boolean isClientControlGroupDeleted =
                cgSetupService.deleteClientControlGroup(groupTypeId, clientId, loginUserID,
                        companyId, languageId, versionNumber, authToken);

        log.info("isClientControlGroupDeleted::: " + isClientControlGroupDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find ClientControlGroup")//label for swagger
    @PostMapping("/clientcontrolgroup/findClientControlGroup")
    public ClientControlGroup[] findClientControlGroup(@RequestBody FindClientControlGroup findClientControlGroup,
                                                       @RequestParam String authToken) throws Exception {
        return cgSetupService.findClientControlGroup(findClientControlGroup, authToken);
    }

    /* --------------------------------RelationShipId-------------------------------------------------------------------------*/

    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All RelationShipId") // label for swagger
    @RequestMapping(value = "/relationshipid", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRelationShipId(@RequestParam String authToken) {
        RelationShipId[] relationShipIds = cgSetupService.getAllRelationShipId(authToken);
        log.info("getAllRelationShipId::: " + relationShipIds);
        return new ResponseEntity<>(relationShipIds, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get RelationShipId") // label for swagger
    @RequestMapping(value = "/relationshipid/{relationShipId}", method = RequestMethod.GET)
    public ResponseEntity<?> getRelationShipId(@PathVariable Long relationShipId, @RequestParam String companyId,
                                               @RequestParam String languageId, @RequestParam String authToken) {

        RelationShipId dbRelationShipId =
                cgSetupService.getRelationShipId(companyId, languageId, relationShipId, authToken);
        log.info("getRelationShipId::: " + dbRelationShipId);
        return new ResponseEntity<>(dbRelationShipId, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new RelationShipId") // label for swagger
    @RequestMapping(value = "/relationshipid", method = RequestMethod.POST)
    public ResponseEntity<?> createRelationShipId(@RequestBody RelationShipId newRelationShipId,
                                                  @RequestParam String loginUserID, @RequestParam String authToken) {

        RelationShipId dbRelationShipId =
                cgSetupService.addRelationShipId(newRelationShipId, loginUserID, authToken);
        log.info("createRelationShipId::: " + dbRelationShipId);
        return new ResponseEntity<>(dbRelationShipId, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update RelationShipId") // label for swagger
    @RequestMapping(value = "/relationshipid/{relationShipId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateClientControlGroup(@PathVariable Long relationShipId, @RequestParam String languageId,
                                                      @RequestParam String loginUserID, @RequestBody RelationShipId updateRelationShipId,
                                                      @RequestParam String companyId, @RequestParam String authToken) {
        RelationShipId dbRelationShipId =
                cgSetupService.updateRelationShipId(relationShipId, languageId, loginUserID, companyId, updateRelationShipId, authToken);

        log.info("updateRelationShipId::: " + dbRelationShipId);
        return new ResponseEntity<>(dbRelationShipId, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete RelationShipId") // label for swagger
    @RequestMapping(value = "/relationshipid/{relationShipId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRelationShipId(@PathVariable Long relationShipId, @RequestParam String companyId,
                                                  @RequestParam String languageId, @RequestParam String authToken,
                                                  @RequestParam String loginUserID) {
        boolean isRelationShipIdDeleted =
                cgSetupService.deleteRelationShipId(relationShipId, companyId, languageId, loginUserID, authToken);
        log.info("isRelationShipId::: " + isRelationShipIdDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find RelationShipId")//label for swagger
    @PostMapping("/relationshipid/findRelationShipId")
    public RelationShipId[] findRelationShipId(@RequestBody FindRelationShipId findRelationShipId,
                                               @RequestParam String authToken) throws Exception {
        return cgSetupService.findRelationShipId(findRelationShipId, authToken);
    }

    /*----------------------------------------------CLIENT_STORE-------------------------------------------------------------------*/
    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All ClientStores") //label for swagger
    @RequestMapping(value = "/clientstore", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClientStore(@RequestParam String authToken) {
        ClientStore[] clientStores = cgSetupService.getAllClientStore(authToken);
        log.info("getClientStore::: " + clientStores);
        return new ResponseEntity<>(clientStores, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get ClientStore") //label for swagger
    @RequestMapping(value = "/ClientStore/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStore(@PathVariable Long clientId, @RequestParam Long storeId,
                                            @RequestParam String companyId, @RequestParam String languageId,
                                            @RequestParam Long versionNumber, @RequestParam String authToken) {
        ClientStore dbClientStore =
                cgSetupService.getClientStore(clientId, storeId, companyId, languageId, versionNumber, authToken);
        log.info("getClientStore::: " + dbClientStore);
        return new ResponseEntity<>(dbClientStore, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new ClientStore") //label for swagger
    @RequestMapping(value = "/clientstore", method = RequestMethod.POST)
    public ResponseEntity<?> createClientStore(@RequestBody ClientStore newClientStore, @RequestParam String loginUserID,
                                               @RequestParam String authToken) {
        ClientStore dbClientStore = cgSetupService.addClientStore(newClientStore, loginUserID, authToken);
        log.info("createClientStore::: " + dbClientStore);
        return new ResponseEntity<>(dbClientStore, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update ClientStore") //label for swagger
    @RequestMapping(value = "/clientstore/{clientId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateClientStore(@PathVariable Long clientId, @RequestParam Long storeId,
                                               @RequestParam String companyId, @RequestParam String languageId,
                                               @RequestParam String loginUserID, @RequestParam Long versionNumber,
                                               @RequestParam String authToken, @RequestBody ClientStore modifiedClientStore) {
        ClientStore dbClientStore =
                cgSetupService.updateClientStore(clientId, storeId, companyId, languageId, versionNumber,
                        loginUserID, modifiedClientStore, authToken);
        log.info("updateClientStore::: " + dbClientStore);
        return new ResponseEntity<>(dbClientStore, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete ClientStore") //label for swagger
    @RequestMapping(value = "/clientstore/{clientId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteClientStore(@PathVariable Long clientId, @RequestParam Long storeId, @RequestParam String companyId,
                                               @RequestParam String languageId, @RequestParam Long versionNumber,
                                               @RequestParam String authToken, @RequestParam String loginUserID) {
        boolean isStateDeleted =
                cgSetupService.deleteClientStore(clientId, storeId, companyId, languageId, versionNumber, authToken, loginUserID);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find ClientStore") //label for swagger
    @PostMapping("/clientstore/findClientStore")
    public ClientStore[] findClientStore(@RequestBody FindClientStore findClientStore,
                                         @RequestParam String authToken) throws Exception {
        return cgSetupService.findClientStore(findClientStore, authToken);
    }

    @ApiOperation(response = Optional.class, value = "Get Number") //label for swagger
    @RequestMapping(value = "/numberRange/numberRangeCode", method = RequestMethod.GET)
    public ResponseEntity<?> getNumberRange(@RequestParam Long numberRangeCode,@RequestParam String numberRangeObject,
                                            @RequestParam String languageId,@RequestParam String companyId,
                                            @RequestParam String authToken) {
        String dbNumberRange = cgSetupService.getNextNumberRange(numberRangeCode,numberRangeObject,languageId,companyId,authToken);
        return new ResponseEntity<>(dbNumberRange, HttpStatus.OK);
    }


    /*----------------------------------------------STATUS -------------------------------------------------------------------*/
    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All StatusId") //label for swagger
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStatusId(@RequestParam String authToken) {
        StatusId[] statusIds = cgSetupService.getAllStatusId(authToken);
        log.info("getStatus::: " + statusIds);
        return new ResponseEntity<>(statusIds, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get StatusId") //label for swagger
    @RequestMapping(value = "/StatusId/{statusId}", method = RequestMethod.GET)
    public ResponseEntity<?> getStatusId(@PathVariable Long statusId, @RequestParam String languageId,
                                         @RequestParam String authToken) {
        StatusId dbStatusId =
                cgSetupService.getStatusId(statusId, languageId, authToken);
        log.info("getStatusId::: " + dbStatusId);
        return new ResponseEntity<>(dbStatusId, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new StatusId") //label for swagger
    @RequestMapping(value = "/statusId", method = RequestMethod.POST)
    public ResponseEntity<?> createStatusId(@RequestBody StatusId newStatusId, @RequestParam String loginUserID,
                                            @RequestParam String authToken) {
        StatusId dbStatusId = cgSetupService.addStatusId(newStatusId, loginUserID, authToken);
        log.info("createStatusId::: " + dbStatusId);
        return new ResponseEntity<>(dbStatusId, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update StatusId") //label for swagger
    @RequestMapping(value = "/StatusId/{statusId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStatusId(@PathVariable Long statusId,@RequestParam String languageId,
                                            @RequestParam String loginUserID, @RequestParam String authToken,
                                            @RequestBody StatusId modifiedStatusId) {
        StatusId dbStatusId =
                cgSetupService.updateStatusId(languageId,statusId,loginUserID,modifiedStatusId,authToken);
        log.info("updateStatusId::: " + dbStatusId);
        return new ResponseEntity<>(dbStatusId, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete StatusId") //label for swagger
    @RequestMapping(value = "/StatusId/{statusId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteClientStore(@PathVariable Long statusId,@RequestParam String languageId,
                                               @RequestParam String authToken, @RequestParam String loginUserID) {
        boolean isStatusIdDeleted =
                cgSetupService.deleteStatusId(languageId,statusId,authToken,loginUserID);
        log.info("isStatusIdDeleted::: " + isStatusIdDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find StatusId") //label for swagger
    @PostMapping("/statusId/findStatusId")
    public StatusId[] findStatus(@RequestBody FindStatus findStatus,
                                 @RequestParam String authToken) throws Exception {
        return cgSetupService.findStatusId(findStatus, authToken);
    }

    /*----------------------------------------------NUMBERRANGE -------------------------------------------------------------------*/
    //GET ALL
    @ApiOperation(response = Optional.class, value = "Get All NumberRange") //label for swagger
    @RequestMapping(value = "/numberRange", method = RequestMethod.GET)
    public ResponseEntity<?> getAllNumberRange(@RequestParam String authToken) {
        NumberRangeId[] numberRangeIds = cgSetupService.getAllNumberRange(authToken);
        log.info("getStatusId::: " + numberRangeIds);
        return new ResponseEntity<>(numberRangeIds, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Optional.class, value = "Get NumberRange") //label for swagger
    @RequestMapping(value = "/NumberRange/{numberRangeCode}", method = RequestMethod.GET)
    public ResponseEntity<?> getNumberRangeId(@PathVariable Long numberRangeCode,@RequestParam String companyId,
                                            @RequestParam String numberRangeObject, @RequestParam String languageId,
                                            @RequestParam String authToken) {
        NumberRangeId dbNumberRangeId =
                cgSetupService.getNumberRange(companyId, languageId, numberRangeCode,numberRangeObject,authToken);
        log.info("getNumberRange::: " + dbNumberRangeId);
        return new ResponseEntity<>(dbNumberRangeId, HttpStatus.OK);
    }

    //CREATE
    @ApiOperation(response = Optional.class, value = "Create new NumberRange") //label for swagger
    @RequestMapping(value = "/NumberRange", method = RequestMethod.POST)
    public ResponseEntity<?> createNumberRange(@RequestBody NumberRangeId newNumberRangeId, @RequestParam String loginUserID,
                                               @RequestParam String authToken) {
        NumberRangeId dbNumberRangeId = cgSetupService.addNumberRange(newNumberRangeId, loginUserID, authToken);
        log.info("createNumberRange::: " + dbNumberRangeId);
        return new ResponseEntity<>(dbNumberRangeId, HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Optional.class, value = "Update NumberRange") //label for swagger
    @RequestMapping(value = "/NumberRange/{numberRangeCode}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateNumberRange(@PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,
                                               @RequestParam String languageId,@RequestParam String loginUserID,
                                               @RequestParam String authToken, @RequestParam String companyId,
                                               @RequestBody NumberRangeId modifiedNumberRangeId) {
        NumberRangeId dbNumberRangeId =
                cgSetupService.updateNumberRange(languageId,companyId,numberRangeCode,numberRangeObject,loginUserID, modifiedNumberRangeId,authToken);
        log.info("updateNumberRange::: " + dbNumberRangeId);
        return new ResponseEntity<>(dbNumberRangeId, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Optional.class, value = "Delete NumberRange") //label for swagger
    @RequestMapping(value = "/NumberRange/{numberRangeCode}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteClientStore(@PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,
                                               @RequestParam String languageId,@RequestParam String companyId,
                                               @RequestParam String authToken, @RequestParam String loginUserID) {
        boolean isNumberRangeDeleted =
                cgSetupService.deleteNumberRange(languageId,companyId,numberRangeCode,numberRangeObject,authToken,loginUserID);
        log.info("isNumberRangeDeleted::: " + isNumberRangeDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = Optional[].class, value = "Find NumberRange") //label for swagger
    @PostMapping("/numberRange/findNumberRange")
    public NumberRangeId[] findNumberRange(@RequestBody FindNumberRange findNumberRange,
                                           @RequestParam String authToken) throws Exception {
        return cgSetupService.findNumberRange(findNumberRange, authToken);
    }

    //------------------------------------------------ENTITY-------------------------------------------------------------------
    //GET ALL ENTITIES
    @ApiOperation(response = CgEntity.class, value = "Get All Entities")
    @GetMapping("/cgentities")
    public ResponseEntity<?> getAllCgEntities(@RequestParam String authToken) {
        CgEntity[] cgEntities = cgSetupService.getAllCgEntities(authToken);
        log.info("getAllEntities::: " + cgEntities);
        return new ResponseEntity<>(cgEntities, HttpStatus.OK);
    }

    //GET ENTITY
    @ApiOperation(response = CgEntity.class, value = "Get a Entity")
    @GetMapping("/cgentity/{entityId}")
    public ResponseEntity<?> getCgEntity(@PathVariable Long entityId, @RequestParam Long clientId, @RequestParam String companyId,
                                         @RequestParam String languageId, String authToken) {
        CgEntity dbCgEntity = cgSetupService.getCgEntity(entityId, clientId, companyId, languageId, authToken);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //CREATE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Create a Entity")
    @PostMapping("/cgentity")
    public ResponseEntity<?> createCgEntity(@RequestBody CgEntity addCgEntity, @RequestParam String loginUserID, @RequestParam String authToken) {
        CgEntity dbCgEntity = cgSetupService.createCgEntity(addCgEntity, loginUserID, authToken);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //UPDATE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Update a Entity")
    @PatchMapping("/cgentity/{entityId}")
    public ResponseEntity<?> updateCgEntity(@PathVariable Long entityId, @RequestParam Long clientId, @RequestParam String companyId,
                                            @RequestParam String languageId, @RequestParam String loginUserID,
                                            @RequestBody CgEntity updateCgEntity, @RequestParam String authToken) {
        CgEntity dbCgEntity = cgSetupService.patchCgEntity(entityId, clientId, companyId, languageId, loginUserID, updateCgEntity, authToken);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //DELETE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Delete a Entity") //label for swagger
    @DeleteMapping("/cgentity/{entityId}")
    public ResponseEntity<?> deleteCgEntity(@PathVariable Long entityId, @RequestParam Long clientId,
                                            @RequestParam String companyId, @RequestParam String languageId,
                                            @RequestParam String authToken, @RequestParam String loginUserID) {
        boolean isStateDeleted = cgSetupService.deleteCgEntity(entityId, clientId, companyId, languageId, authToken, loginUserID);
        log.info("isStateDeleted::: " + isStateDeleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND ENTITY
    @ApiOperation(response = CgEntity[].class, value = "Find a Entity") //label for swagger
    @PostMapping("/cgentity/find")
    public CgEntity[] findCgEntity(@RequestBody FindCgEntity findCgEntity, @RequestParam String authToken)
            throws Exception {
        return cgSetupService.findCgEntity(findCgEntity, authToken);
    }

    @ApiOperation(response = EMail.class, value = "Send SummeryReport") // label for swagger
    @PostMapping("/sendFormThroEmail")
    public ResponseEntity<?> sendFormThroEmail(@Valid @RequestBody EMail email)
            throws IllegalAccessException, InvocationTargetException {
        cgSetupService.sendFormThroEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(response = EMail.class, value = "Send SummeryReport with attachment") // label for swagger
    @PostMapping("/sendEmailWithAttachment")
    public ResponseEntity<?> sendFormThroEmail(@Valid @RequestBody EMail email,@RequestParam String file)
            throws IllegalAccessException, InvocationTargetException {
        cgSetupService.sendEmailWithAttachment(email,file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
