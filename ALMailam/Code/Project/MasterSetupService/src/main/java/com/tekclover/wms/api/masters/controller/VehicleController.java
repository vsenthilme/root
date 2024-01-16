package com.tekclover.wms.api.masters.controller;


import com.tekclover.wms.api.masters.model.imvariant.AddImVariant;
import com.tekclover.wms.api.masters.model.imvariant.ImVariant;
import com.tekclover.wms.api.masters.model.imvariant.SearchImVariant;
import com.tekclover.wms.api.masters.model.imvariant.UpdateImVariant;
import com.tekclover.wms.api.masters.model.vehicle.AddVehicle;
import com.tekclover.wms.api.masters.model.vehicle.SearchVehicle;
import com.tekclover.wms.api.masters.model.vehicle.UpdateVehicle;
import com.tekclover.wms.api.masters.model.vehicle.Vehicle;
import com.tekclover.wms.api.masters.service.ImVariantService;
import com.tekclover.wms.api.masters.service.VehicleService;
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
@Api(tags = {"Vehicle"}, value = "Vehicle  Operations related to VehicleController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Vehicle ",description = "Operations related to Vehicle ")})
@RequestMapping("/vehicle")
@RestController
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    //GET ALL
    @ApiOperation(response = Vehicle.class, value = "Get all Vehicle details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Vehicle> vehicleList = vehicleService.getAllVehicle();
        return new ResponseEntity<>(vehicleList, HttpStatus.OK);
    }

    //GET
    @ApiOperation(response = Vehicle.class, value = "Get a Vehicle") // label for swagger
    @GetMapping("/{vehicleNumber}")
    public ResponseEntity<?> getVehicle(@PathVariable String vehicleNumber, @RequestParam String companyCodeId, @RequestParam String plantId,
                                        @RequestParam String warehouseId,@RequestParam String languageId) {

        Vehicle vehicle = vehicleService.getVehicle(vehicleNumber,companyCodeId,plantId,languageId,warehouseId);
        log.info("Vehicle : " + vehicle);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    //FIND
    @ApiOperation(response = Vehicle.class, value = "Search Vehicle") // label for swagger
    @PostMapping("/findVehicle")
    public List<Vehicle> findVehicle(@RequestBody SearchVehicle searchVehicle)
            throws Exception {
        return vehicleService.findVehicle(searchVehicle);
    }

    //CREATE
    @ApiOperation(response = Vehicle.class, value = "Create Vehicle") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postVehicle(@Valid @RequestBody AddVehicle newVehicle, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Vehicle createVehicle = vehicleService.createVehicle(newVehicle, loginUserID);
        return new ResponseEntity<>(createVehicle , HttpStatus.OK);
    }

    //UPDATE
    @ApiOperation(response = Vehicle.class, value = "Update Vehicle") // label for swagger
    @PatchMapping("/{vehicleNumber}")
    public ResponseEntity<?> patchImVariant(@PathVariable String vehicleNumber, @RequestParam String companyCodeId, @RequestParam String plantId,
                                            @RequestParam String warehouseId, @RequestParam String languageId,
                                            @Valid @RequestBody UpdateVehicle updateVehicle, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Vehicle createVehicle = vehicleService.updateVehicle(companyCodeId,plantId,warehouseId,languageId,vehicleNumber,updateVehicle,loginUserID);
        return new ResponseEntity<>(createVehicle , HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = Vehicle.class, value = "Delete Vehicle") // label for swagger
    @DeleteMapping("/{vehicleNumber}")
    public ResponseEntity<?> deleteVehicleNumber(@PathVariable String vehicleNumber,@RequestParam String companyCodeId,@RequestParam String plantId,
                                                 @RequestParam String warehouseId,@RequestParam String languageId,@RequestParam String loginUserID) {

        vehicleService.deleteVehicle(companyCodeId,languageId,plantId,warehouseId,vehicleNumber,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
