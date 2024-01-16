package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.client.AddClient;
import com.mnrclara.api.cg.setup.model.client.Client;
import com.mnrclara.api.cg.setup.model.client.FindClient;
import com.mnrclara.api.cg.setup.model.client.UpdateClient;
import com.mnrclara.api.cg.setup.service.ClientService;
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
@Api(tags = {" Client "}, value = " Client Operations related to ClientController ") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = " Client ",description = " Operations related to Client ")})
@RequestMapping("/client")
@RestController
public class ClientController {

    @Autowired
    ClientService clientService;

    // GET ALL
    @ApiOperation(response = Client.class, value = "Get all Client details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Client> clientList = clientService.getAllClientId();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = Client.class, value = "Get a Client") // label for swagger
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClient(@PathVariable Long clientId, @RequestParam String languageId,
                                       @RequestParam String companyId) {
        Client client = clientService.getClientId(companyId, languageId, clientId);
        log.info("Client : " + client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = Client.class, value = "Create client") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addClient(@Valid @RequestBody AddClient newClient, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Client createClient = clientService.createClient(newClient, loginUserID);
        return new ResponseEntity<>(createClient , HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = Client.class, value = "Update client") // label for swagger
    @PatchMapping("/{clientId}")
    public ResponseEntity<?> patchClient(@PathVariable Long clientId, @RequestParam String languageId,
                                       @RequestParam String loginUserID, @RequestParam String companyId,
                                       @RequestBody UpdateClient updateClient)
            throws IllegalAccessException, InvocationTargetException {

        Client updateClientId = clientService.updateClient(companyId, languageId, clientId, loginUserID, updateClient);
        return new ResponseEntity<>(updateClientId, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = Client.class, value = "Delete Client") // label for swagger
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Long clientId, @RequestParam String companyId,
                                        @RequestParam String languageId, @RequestParam String loginUserID) {
        clientService.deleteClient(companyId, languageId, clientId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = Client.class, value = "Find Client") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findClient(@Valid @RequestBody FindClient findClient) throws Exception {
        List<Client> createClient = clientService.findClientId(findClient);
        return new ResponseEntity<>(createClient, HttpStatus.OK);
    }

}
