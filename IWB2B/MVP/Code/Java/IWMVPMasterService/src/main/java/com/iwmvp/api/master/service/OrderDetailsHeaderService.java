package com.iwmvp.api.master.service;

import com.iwmvp.api.master.controller.exception.BadRequestException;
import com.iwmvp.api.master.model.asyad.*;
import com.iwmvp.api.master.model.auth.AuthToken;
import com.iwmvp.api.master.model.customer.Customer;
import com.iwmvp.api.master.model.loyaltysetup.LoyaltySetup;
import com.iwmvp.api.master.model.orderdetails.*;

import com.iwmvp.api.master.repository.CustomerRepository;
import com.iwmvp.api.master.repository.*;
import com.iwmvp.api.master.repository.Specification.OrderDetailsHeaderSpecification;
import com.iwmvp.api.master.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderDetailsHeaderService extends BaseService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderDetailsHeaderRepository orderDetailsHeaderRepository;
    @Autowired
    private OrderDetailsLineRepository orderDetailsLineRepository;
    @Autowired
    private OrderDetailsLineService orderDetailsLineService;
    @Autowired
    AuthTokenService authTokenService;
    @Autowired
    private SetupService setupService;
    @Autowired
    private LoyaltySetupRepository loyaltySetupRepository;
    @Autowired
    private LoyaltyCategoryRepository loyaltyCategoryRepository;

    /**
     * getAllOrderDetailsHeader
     * @return
     */
    public List<OrderDetailsHeaderOutput> getAllOrderDetailsHeader(){
       List<OrderDetailsHeader>OrderDetailsHeaderList= orderDetailsHeaderRepository.findAll();
       OrderDetailsHeaderList=OrderDetailsHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<OrderDetailsHeaderOutput> orderDetailsHeaderOutput = createBeanList(OrderDetailsHeaderList);
        return orderDetailsHeaderOutput;
    }
    /**
     * getOrderDetailsHeader
     * @param orderId
     * @return
     */
    public OrderDetailsHeaderOutput getOrderDetailsHeader(Long orderId){
        OrderDetailsHeader dbOrderDetailsHeader=
                orderDetailsHeaderRepository.findByOrderIdAndDeletionIndicator(
                orderId,
                0l );
        if(dbOrderDetailsHeader==null){
            throw new BadRequestException("The given values:"+
                    "orderId - "+orderId+
                    "doesn't exist.");
        }
        OrderDetailsHeaderOutput orderDetailsHeaderOutput = createBean(dbOrderDetailsHeader);
        return orderDetailsHeaderOutput;
    }

    public OrderDetailsHeader getOrderDetailHeader(Long orderId) {
        OrderDetailsHeader dbOrderDetailsHeader =
                orderDetailsHeaderRepository.findByOrderIdAndDeletionIndicator(
                        orderId,
                        0l);
        if (dbOrderDetailsHeader == null) {
            throw new BadRequestException("The given values:" +
                    "orderId - " + orderId +
                    "doesn't exist.");
        }
        return dbOrderDetailsHeader;
    }
    /**
     * createOrderDetailsHeader
     * @param newOrderDetailsHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OrderDetailsHeaderOutput createOrderDetailsHeader(AddOrderDetailsHeader newOrderDetailsHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        OrderDetailsHeader dbOrderDetailsHeader = new OrderDetailsHeader();
        OrderDetailsHeaderOutput newOrderDetailsHeaderOutput = new OrderDetailsHeaderOutput();
        BeanUtils.copyProperties(newOrderDetailsHeader, dbOrderDetailsHeader, CommonUtils.getNullPropertyNames(newOrderDetailsHeader));
        dbOrderDetailsHeader.setCompanyId(newOrderDetailsHeaderOutput.getCompanyId());
        dbOrderDetailsHeader.setDeletionIndicator(0L);
        dbOrderDetailsHeader.setCreatedBy(loginUserID);
        dbOrderDetailsHeader.setUpdatedBy(loginUserID);
        dbOrderDetailsHeader.setCreatedOn(new Date());
        dbOrderDetailsHeader.setUpdatedOn(new Date());
        OrderDetailsHeader savedOrderDetailsHeader=orderDetailsHeaderRepository.save(dbOrderDetailsHeader);
        List<AddOrderDetailsLine> createdOrderLine = orderDetailsLineService.createOrderDetailsLine(newOrderDetailsHeader, savedOrderDetailsHeader.getOrderId(), loginUserID);
        //Update Customer type from LEAD to customer once order is created and calculate Loyalty Point and Amount
        Double[] loyaltyPointAmount = calculateLoyaltyPointAmount(newOrderDetailsHeader, loginUserID);

        if(loyaltyPointAmount[1]!=null){
            savedOrderDetailsHeader.setLoyaltyAmount(loyaltyPointAmount[1]);
        }else{
            savedOrderDetailsHeader.setLoyaltyAmount(0d);
        }
        if(loyaltyPointAmount[0]!=null){
            savedOrderDetailsHeader.setLoyaltyPoint(loyaltyPointAmount[0]);
        }else{
            savedOrderDetailsHeader.setLoyaltyPoint(0d);
        }

        savedOrderDetailsHeader=orderDetailsHeaderRepository.save(savedOrderDetailsHeader);
        BeanUtils.copyProperties(savedOrderDetailsHeader, newOrderDetailsHeaderOutput, CommonUtils.getNullPropertyNames(savedOrderDetailsHeader));
        newOrderDetailsHeaderOutput.setOrderDetailsLines(createdOrderLine);
        newOrderDetailsHeaderOutput.setCustomerName(customerService.getCustomerById(newOrderDetailsHeaderOutput.getCustomerId()).getCustomerName());
     return newOrderDetailsHeaderOutput;
    }
    /**
     * updateOrderDetailsHeader
     * @param loginUserID
     * @param orderId
     * @param updateOrderDetailsHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OrderDetailsHeaderOutput updateOrderDetailsHeader(Long orderId, String loginUserID,
                                           AddOrderDetailsHeader updateOrderDetailsHeader)throws IllegalAccessException,InvocationTargetException{
        OrderDetailsHeader dbOrderDetailsHeader=getOrderDetailHeader(orderId);
        OrderDetailsHeaderOutput updateOrderDetailsHeaderOutput = new OrderDetailsHeaderOutput();
        BeanUtils.copyProperties(updateOrderDetailsHeader,dbOrderDetailsHeader,CommonUtils.getNullPropertyNames(updateOrderDetailsHeader));
        dbOrderDetailsHeader.setUpdatedBy(loginUserID);
        dbOrderDetailsHeader.setUpdatedOn(new Date());
        OrderDetailsHeader savedOrderDetailsHeader =  orderDetailsHeaderRepository.save(dbOrderDetailsHeader);
        List<AddOrderDetailsLine> createdOrderLine = new ArrayList<>();
        if(updateOrderDetailsHeader.getOrderDetailsLines()!=null&&!updateOrderDetailsHeader.getOrderDetailsLines().isEmpty()){
            if(orderDetailsLineService.getOrderDetailsLine(orderId)!=null){
                orderDetailsLineService.deleteOrderDetailsLine(orderId,loginUserID);
            }
            createdOrderLine = orderDetailsLineService.createOrderDetailsLine(updateOrderDetailsHeader, savedOrderDetailsHeader.getOrderId(), loginUserID);
            }
            BeanUtils.copyProperties(savedOrderDetailsHeader, updateOrderDetailsHeaderOutput, CommonUtils.getNullPropertyNames(savedOrderDetailsHeader));
            if(updateOrderDetailsHeader.getOrderDetailsLines()==null||updateOrderDetailsHeader.getOrderDetailsLines().isEmpty()){
                List<OrderDetailsLine> orderDetailsLines = orderDetailsLineService.getOrderDetailsLine(orderId);
                if(orderDetailsLines!=null){
                    for(OrderDetailsLine orderDetailsLine : orderDetailsLines){
                        AddOrderDetailsLine dbUpdateOrderDetailsLine = new AddOrderDetailsLine();
                        BeanUtils.copyProperties(orderDetailsLine, dbUpdateOrderDetailsLine, CommonUtils.getNullPropertyNames(orderDetailsLine));
                        createdOrderLine.add(dbUpdateOrderDetailsLine);
                    }
                }
            }
        updateOrderDetailsHeaderOutput.setOrderDetailsLines(createdOrderLine);
        updateOrderDetailsHeaderOutput.setCustomerName(customerService.getCustomerById(updateOrderDetailsHeaderOutput.getCustomerId()).getCustomerName());
        return updateOrderDetailsHeaderOutput;
    }
    /**
     * approveOrder-B2b
     * @param loginUserID
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void approveOrderDetailsHeader(Long  orderId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
        OrderDetailsHeaderOutput dbOrderDetailsHeader = getOrderDetailsHeader(orderId);

        OrderDetailsHeader newOrderDetailsHeader=
                orderDetailsHeaderRepository.findByOrderIdAndDeletionIndicator(
                        orderId,
                        0l );
        if(newOrderDetailsHeader!=null) {
            newOrderDetailsHeader.setApprovedBy(loginUserID);
            newOrderDetailsHeader.setApprovedOn(new Date());
            orderDetailsHeaderRepository.save(newOrderDetailsHeader);
        }

        AddMvpConsignment newConsignment = new AddMvpConsignment();
        AddOrigin_Details newOrginDetails = new AddOrigin_Details();
        AddDestination_Details newDestinationDetails = new AddDestination_Details();
        List<AddPieces_Details> newPiecesDetails = new ArrayList<>();

        //Origin Details
        newOrginDetails.setName(dbOrderDetailsHeader.getOriginDetailsName());
        newOrginDetails.setPhone(dbOrderDetailsHeader.getOriginDetailsPhone());
        newOrginDetails.setAlternate_phone("");
        newOrginDetails.setAddress_line_1(dbOrderDetailsHeader.getOriginDetailsAddressLine1());
        newOrginDetails.setAddress_line_2(dbOrderDetailsHeader.getOriginDetailsAddressLine2());
        newOrginDetails.setPincode(dbOrderDetailsHeader.getOriginDetailsPincode());
        newOrginDetails.setCity(dbOrderDetailsHeader.getOriginCity());
        newOrginDetails.setState(dbOrderDetailsHeader.getOriginState());
        newOrginDetails.setCountry(dbOrderDetailsHeader.getOriginCountry());

        newConsignment.setOrigin_details(newOrginDetails);

        //Destination Details
        newDestinationDetails.setName(dbOrderDetailsHeader.getDestinationDetailsName());
        newDestinationDetails.setPhone(dbOrderDetailsHeader.getDestinationDetailsPhone());
        newDestinationDetails.setAlternate_phone("");
        newDestinationDetails.setAddress_line_1(dbOrderDetailsHeader.getDestinationDetailsAddressLine1());
        newDestinationDetails.setAddress_line_2(dbOrderDetailsHeader.getDestinationDetailsAddressLine2());
        newDestinationDetails.setPincode(dbOrderDetailsHeader.getDestinationDetailsPincode());
        newDestinationDetails.setCity(dbOrderDetailsHeader.getDestinationCity());
        newDestinationDetails.setState(dbOrderDetailsHeader.getDestinationState());
        newDestinationDetails.setCountry(dbOrderDetailsHeader.getDestinationCountry());

        newConsignment.setDestination_details(newDestinationDetails);

        for(AddOrderDetailsLine dbOrderDetailsLine : dbOrderDetailsHeader.getOrderDetailsLines()){
            AddPieces_Details dbPiecesDetails = new AddPieces_Details();
            dbPiecesDetails.setDescription(dbOrderDetailsLine.getCommodityName());
            dbPiecesDetails.setDeclared_value(Double.parseDouble(dbOrderDetailsLine.getDeclaredValue()));
            dbPiecesDetails.setHeight(Double.parseDouble(dbOrderDetailsLine.getHeight()));
            dbPiecesDetails.setWidth(Long.parseLong(dbOrderDetailsLine.getWidth()));
            dbPiecesDetails.setLength(Long.parseLong(dbOrderDetailsLine.getLength()));
            dbPiecesDetails.setWeight(Double.parseDouble(dbOrderDetailsLine.getWeight()));

            newConsignment.setDimension_unit(dbOrderDetailsLine.getDimensionUnit());
            newConsignment.setHeight(dbOrderDetailsLine.getHeight());
            newConsignment.setLength(dbOrderDetailsLine.getLength());
            newConsignment.setWeight(Double.valueOf(dbOrderDetailsLine.getWeight()));
            newConsignment.setWidth(dbOrderDetailsLine.getWidth());
            newConsignment.setWeight_unit(dbOrderDetailsLine.getWeightUnit());
            newConsignment.setDeclared_value(Double.valueOf(dbOrderDetailsLine.getDeclaredValue()));
            newConsignment.setNum_pieces(Long.valueOf(dbOrderDetailsLine.getNoOfPieces()));
            newConsignment.setNotes(dbOrderDetailsLine.getCommodityName());

            newPiecesDetails.add(dbPiecesDetails);
        }

        newConsignment.setPieces_detail(newPiecesDetails);

        newConsignment.setCustomer_code(String.valueOf(dbOrderDetailsHeader.getCustomerId()));
        newConsignment.setCustomer_reference_number(dbOrderDetailsHeader.getReferenceNo());
        newConsignment.setService_type_id(dbOrderDetailsHeader.getServiceTypeId());
        newConsignment.setConsignment_type(dbOrderDetailsHeader.getTypeOfDelivery());
        newConsignment.setLoad_type(dbOrderDetailsHeader.getLoadType());
        newConsignment.setDescription("Order Id: "+String.valueOf(dbOrderDetailsHeader.getOrderId()));
        newConsignment.setCod_favor_of("");
        newConsignment.setCod_collection_mode("");
        newConsignment.setCod_amount(String.valueOf(dbOrderDetailsHeader.getDeliveryCharge()));
        newConsignment.setIs_risk_surcharge_applicable(false);


        ConsignmentResponse pushOrderDetailsHeaderOutput = setupService.createConsignment(newConsignment,loginUserID,authTokenForSetupService.getAccess_token());


    }

    /**
     * deleteOrderDetailsHeader
     * @param loginUserID
     * @param orderId
     */
    public void deleteOrderDetailsHeader(Long orderId,String loginUserID) {
        OrderDetailsHeader dbOrderDetailsHeader = getOrderDetailHeader(orderId);
        if (dbOrderDetailsHeader != null) {
            dbOrderDetailsHeader.setDeletionIndicator(1L);
            dbOrderDetailsHeader.setUpdatedBy(loginUserID);
            orderDetailsHeaderRepository.save(dbOrderDetailsHeader);
            List<OrderDetailsLine> dbOrderDetailsLine = orderDetailsLineService.getOrderDetailsLine(orderId);
            if(dbOrderDetailsLine!=null){
                for(OrderDetailsLine newOrderDetailsLine : dbOrderDetailsLine){
                newOrderDetailsLine.setDeletionIndicator(1L);
                newOrderDetailsLine.setUpdatedBy(loginUserID);
                orderDetailsLineRepository.save(newOrderDetailsLine);
                }
            }
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + orderId);
        }
    }
    //Find OrderDetailsHeader
    public List<OrderDetailsHeaderOutput> findOrderDetailsHeader(FindOrderDetailsHeader findOrderDetailsHeader) throws ParseException {
        if (findOrderDetailsHeader.getStartDate() != null &&
                findOrderDetailsHeader.getEndDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findOrderDetailsHeader.getStartDate(),
                    findOrderDetailsHeader.getEndDate());
            findOrderDetailsHeader.setStartDate(dates[0]);
            findOrderDetailsHeader.setEndDate(dates[1]);
        }
        OrderDetailsHeaderSpecification spec = new OrderDetailsHeaderSpecification(findOrderDetailsHeader);
        List<OrderDetailsHeader> results = orderDetailsHeaderRepository.findAll(spec);
        List<OrderDetailsHeaderOutput> responseOrderDetailsHeaderList = createBeanList(results);
        return responseOrderDetailsHeaderList;
    }
    private List<OrderDetailsHeaderOutput> createBeanList(List<OrderDetailsHeader> orderDetailsHeaderList) {
        List<OrderDetailsHeaderOutput> listOrderDetailsHeader = new ArrayList<>();
        for (OrderDetailsHeader orderDetailsHeader : orderDetailsHeaderList) {
            OrderDetailsHeaderOutput addOrderDetailsHeader = copyHeaderEntityToBean(orderDetailsHeader);
            if(orderDetailsHeader!=null){
                List<OrderDetailsLine> orderDetailsLines = orderDetailsLineRepository.findByOrderIdAndDeletionIndicator(orderDetailsHeader.getOrderId(), 0L);
                List<AddOrderDetailsLine> newAddOrderDetailsLineList = new ArrayList<>();
                for(OrderDetailsLine orderDetailsLine :orderDetailsLines ){
                    AddOrderDetailsLine newAddOrderDetailsLine = new AddOrderDetailsLine();
                    BeanUtils.copyProperties(orderDetailsLine, newAddOrderDetailsLine, CommonUtils.getNullPropertyNames(orderDetailsLine));
                    newAddOrderDetailsLineList.add(newAddOrderDetailsLine);
                }
                addOrderDetailsHeader.setOrderDetailsLines(newAddOrderDetailsLineList);
            }
            listOrderDetailsHeader.add(addOrderDetailsHeader);
        }
        return listOrderDetailsHeader;
    }
    private OrderDetailsHeaderOutput createBean(OrderDetailsHeader orderDetailsHeader) {
            OrderDetailsHeaderOutput addOrderDetailsHeader = copyHeaderEntityToBean(orderDetailsHeader);
            if(orderDetailsHeader!=null){
                List<OrderDetailsLine> orderDetailsLines = orderDetailsLineRepository.findByOrderIdAndDeletionIndicator(orderDetailsHeader.getOrderId(), 0L);
                List<AddOrderDetailsLine> newAddOrderDetailsLineList = new ArrayList<>();
                if(orderDetailsLines!=null){
                    for(OrderDetailsLine orderDetailsLine :orderDetailsLines ){
                        AddOrderDetailsLine newAddOrderDetailsLine = new AddOrderDetailsLine();
                        BeanUtils.copyProperties(orderDetailsLine, newAddOrderDetailsLine, CommonUtils.getNullPropertyNames(orderDetailsLine));
                        newAddOrderDetailsLineList.add(newAddOrderDetailsLine);
                    }
                    addOrderDetailsHeader.setOrderDetailsLines(newAddOrderDetailsLineList);
                }
            }
        return addOrderDetailsHeader;
    }
    private OrderDetailsHeaderOutput copyHeaderEntityToBean(OrderDetailsHeader orderDetailsHeader) {
        OrderDetailsHeaderOutput addOrderDetailsHeader = new OrderDetailsHeaderOutput();
        BeanUtils.copyProperties(orderDetailsHeader, addOrderDetailsHeader, CommonUtils.getNullPropertyNames(orderDetailsHeader));
        addOrderDetailsHeader.setCustomerName(customerService.getCustomerById(addOrderDetailsHeader.getCustomerId()).getCustomerName());
        return addOrderDetailsHeader;
    }

    public Double[] calculateLoyaltyPointAmount(AddOrderDetailsHeader newOrderDetailsHeader, String loginUserID) throws BadRequestException{
        Customer savedCustomer=customerService.getCustomer(newOrderDetailsHeader.getCustomerId(), newOrderDetailsHeader.getCompanyId(), newOrderDetailsHeader.getLanguageId());
        Double loyaltyPoint = 0d;
        Double loyaltyAmount = 0d;
        Double[] loyaltyPointAmount = new Double[2];
        String categoryId = savedCustomer.getCustomerCategory();
        if(savedCustomer!=null){
            List<LoyaltySetup> loyaltyCategoryFilter = loyaltySetupRepository.findByCategoryIdAndDeletionIndicator(categoryId,0L);

            for(LoyaltySetup loyaltySetup:loyaltyCategoryFilter){
                LoyaltySetup newLoyaltySetup = new LoyaltySetup();
                newLoyaltySetup.setTransactionValueFrom(loyaltySetup.getTransactionValueFrom());
                newLoyaltySetup.setTransactionValueTo(loyaltySetup.getTransactionValueTo());
                newLoyaltySetup.setLoyaltyPoint(loyaltySetup.getLoyaltyPoint());
                Boolean check = newLoyaltySetup.getTransactionValueFrom() <= newOrderDetailsHeader.getDeliveryCharge() && newOrderDetailsHeader.getDeliveryCharge() <= newLoyaltySetup.getTransactionValueTo();
                if(check) {
                    loyaltyPoint = newLoyaltySetup.getLoyaltyPoint();
                }
            }
            if(loyaltyPoint!=null){
                loyaltyAmount = loyaltyCategoryRepository.getCreditValue(savedCustomer.getCustomerCategory(),loyaltyPoint);
            }else{
                loyaltyPoint=0d;
            }
            //return loyalty point and amount to create order
            loyaltyPointAmount[0] = loyaltyPoint;
            loyaltyPointAmount[1] = loyaltyAmount;

            savedCustomer.setCustomerType("CUSTOMER");
            savedCustomer.setLoyaltyPoint(savedCustomer.getLoyaltyPoint() + loyaltyPoint);
            savedCustomer.setUpdatedBy(loginUserID);
            savedCustomer.setUpdatedOn(new Date());
            customerRepository.save(savedCustomer);

            return loyaltyPointAmount;

        }else{
            throw new BadRequestException("The given CustomerId:" + newOrderDetailsHeader.getCustomerId() +
                    "doesn't exist.");
        }
    }
}
