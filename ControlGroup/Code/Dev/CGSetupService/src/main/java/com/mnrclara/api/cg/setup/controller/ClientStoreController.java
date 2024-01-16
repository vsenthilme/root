package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.clientstore.AddClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.ClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.FindClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.UpdateClientStore;
import com.mnrclara.api.cg.setup.service.ClientStoreService;
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
@Api(tags = {"ClientStore"}, value = "ClientStore Operations related to ClientStoreController") //label for Swagger
@SwaggerDefinition(tags = {@Tag(name = "ClientStore", description = "Operations related to ClientStore")})
@RequestMapping("/clientstore")
@RestController
public class ClientStoreController {
    @Autowired
    ClientStoreService clientStoreService;

    //GET ALL ClientStores

    /**
     * @return
     */
    @ApiOperation(response = ClientStore.class, value = "Get all ClientStore details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ClientStore> clientStores = clientStoreService.getAllClientStore();
        return new ResponseEntity<>(clientStores, HttpStatus.OK);
    }

    //GET ClientStore

    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @return
     */
    @ApiOperation(response = ClientStore.class, value = "Get ClientStore") // label for swagger
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClientStore(@PathVariable Long clientId, @RequestParam Long storeId,
                                            @RequestParam String companyId, @RequestParam String languageId,
                                            @RequestParam Long versionNumber) {
        ClientStore clientStore = clientStoreService.getClientStore(clientId, storeId, companyId, languageId, versionNumber);
        log.info("ClientStore : " + clientStore);
        return new ResponseEntity<>(clientStore, HttpStatus.OK);
    }

    //CREATE ClientStore

    /**
     * @param newClientStore
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @ApiOperation(response = ClientStore.class, value = "Create a new ClientStore") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addClientStore(@Valid @RequestBody AddClientStore newClientStore, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ClientStore createClientStore = clientStoreService.createClientStore(newClientStore, loginUserID);
        return new ResponseEntity<>(createClientStore, HttpStatus.OK);
    }

    //UPDATE ClientStore

    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     * @param updateClientStore
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @ApiOperation(response = ClientStore.class, value = "Update ClientStore") // label for swagger
    @PatchMapping("/{clientId}")
    public ResponseEntity<?> patchClientStore(@PathVariable Long clientId, @RequestParam Long storeId,
                                              @RequestParam String companyId, @RequestParam String languageId,
                                              @RequestParam Long versionNumber, @RequestParam String loginUserID,
                                              @Valid @RequestBody UpdateClientStore updateClientStore)
            throws IllegalAccessException, InvocationTargetException {
        ClientStore updatedClientStore = clientStoreService.updateClientStore(
                clientId, storeId, companyId, languageId, versionNumber, loginUserID, updateClientStore);
        return new ResponseEntity<>(updatedClientStore, HttpStatus.OK);
    }

    //DELETE ClientStore

    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     * @return
     */
    @ApiOperation(response = ClientStore.class, value = "Delete ClientStore") // label for swagger
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClientStore(@PathVariable Long clientId, @RequestParam Long storeId,
                                               @RequestParam String companyId, @RequestParam String languageId,
                                               @RequestParam Long versionNumber, @RequestParam String loginUserID) {
        clientStoreService.deleteClientStore(clientId, storeId, companyId, languageId, versionNumber, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND ClientStore

    /**
     * @param findClientStore
     * @return
     * @throws Exception
     */
    @ApiOperation(response = ClientStore.class, value = "Find ClientStore") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findClientStore(@Valid @RequestBody FindClientStore findClientStore) throws Exception {
        List<ClientStore> createdClientStore = clientStoreService.findClientStore(findClientStore);
        return new ResponseEntity<>(createdClientStore, HttpStatus.OK);
    }
}