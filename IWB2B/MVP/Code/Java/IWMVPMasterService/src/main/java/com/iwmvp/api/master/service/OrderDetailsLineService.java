package com.iwmvp.api.master.service;

import com.iwmvp.api.master.controller.exception.BadRequestException;
import com.iwmvp.api.master.model.customer.Customer;
import com.iwmvp.api.master.model.orderdetails.*;

import com.iwmvp.api.master.repository.OrderDetailsLineRepository;
import com.iwmvp.api.master.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderDetailsLineService extends BaseService {

    @Autowired
    private OrderDetailsLineRepository orderDetailsLineRepository;

    /**
     * getAllOrderDetailsLine
     * @return
     */
    public List<OrderDetailsLine> getAllOrderDetailsLine(){
       List<OrderDetailsLine>OrderDetailsLineList= orderDetailsLineRepository.findAll();
       OrderDetailsLineList=OrderDetailsLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return OrderDetailsLineList;
    }
    /**
     * getOrderDetailsLine
     * @param orderId
     * @return
     */
    public List<OrderDetailsLine> getOrderDetailsLine(Long orderId){
        List<OrderDetailsLine> dbOrderDetailsLine=
                orderDetailsLineRepository.findByOrderIdAndDeletionIndicator(
                orderId,
                0l );
        if(dbOrderDetailsLine.isEmpty()){
            throw new BadRequestException("The given values:"+
                    "orderId - "+orderId+
                    "doesn't exist.");
        }
        return dbOrderDetailsLine;
    }
    /**
     * createOrderDetailsLine
     * @param newOrderDetailsHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<AddOrderDetailsLine> createOrderDetailsLine(AddOrderDetailsHeader newOrderDetailsHeader,Long orderId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<AddOrderDetailsLine> createdOrderLine = new ArrayList<>();
        for(AddOrderDetailsLine addOrderDetailsLine : newOrderDetailsHeader.getOrderDetailsLines()){
            OrderDetailsLine dbOrderDetailsLine = new OrderDetailsLine();
            BeanUtils.copyProperties(addOrderDetailsLine, dbOrderDetailsLine, CommonUtils.getNullPropertyNames(addOrderDetailsLine));
            dbOrderDetailsLine.setOrderId(orderId);
            dbOrderDetailsLine.setCompanyId(newOrderDetailsHeader.getCompanyId());
            dbOrderDetailsLine.setLanguageId(newOrderDetailsHeader.getLanguageId());
            dbOrderDetailsLine.setReferenceNo(newOrderDetailsHeader.getReferenceNo());
            dbOrderDetailsLine.setDeletionIndicator(0L);
            dbOrderDetailsLine.setCreatedBy(loginUserID);
            dbOrderDetailsLine.setCreatedOn(new Date());
            dbOrderDetailsLine.setUpdatedBy(loginUserID);
            dbOrderDetailsLine.setUpdatedOn(new Date());
            OrderDetailsLine orderDetailsLine = orderDetailsLineRepository.save(dbOrderDetailsLine);
            BeanUtils.copyProperties(orderDetailsLine, addOrderDetailsLine, CommonUtils.getNullPropertyNames(orderDetailsLine));
            createdOrderLine.add(addOrderDetailsLine);
        }
     return createdOrderLine;
    }

    /**
     * deleteOrderDetailsLine
     * @param loginUserID
     * @param orderId
     */
    public void deleteOrderDetailsLine(Long orderId,String loginUserID) {
        List<OrderDetailsLine> dbOrderDetailsLine = getOrderDetailsLine(orderId);
        if (dbOrderDetailsLine != null) {
            for(OrderDetailsLine newOrderDetailsLine : dbOrderDetailsLine){
                orderDetailsLineRepository.delete(newOrderDetailsLine);
            }
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + orderId);
        }
    }
}
