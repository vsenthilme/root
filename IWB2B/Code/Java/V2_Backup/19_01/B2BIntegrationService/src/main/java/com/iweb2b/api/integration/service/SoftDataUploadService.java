package com.iweb2b.api.integration.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.integration.config.PropertiesConfig;
import com.iweb2b.api.integration.model.consignment.dto.Consignment;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentResponse;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentWebhook;
import com.iweb2b.api.integration.model.consignment.dto.Destination_Details;
import com.iweb2b.api.integration.model.consignment.dto.Origin_Details;
import com.iweb2b.api.integration.model.consignment.dto.Pieces_Details;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentEntity;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentWebhookEntity;
import com.iweb2b.api.integration.model.consignment.entity.DestinationDetailEntity;
import com.iweb2b.api.integration.model.consignment.entity.OriginDetailsEntity;
import com.iweb2b.api.integration.model.consignment.entity.PiecesDetailsEntity;
import com.iweb2b.api.integration.model.tracking.ConsignmentTracking;
import com.iweb2b.api.integration.model.tracking.DestinationDetail;
import com.iweb2b.api.integration.model.tracking.OriginDetail;
import com.iweb2b.api.integration.model.tracking.PiecesDetail;
import com.iweb2b.api.integration.repository.ConsignmentRepository;
import com.iweb2b.api.integration.repository.ConsignmentWebhookRepository;
import com.iweb2b.api.integration.repository.DestinationDetailRepository;
import com.iweb2b.api.integration.repository.OriginDetailRepository;
import com.iweb2b.api.integration.repository.PiecesDetailRepository;
import com.iweb2b.api.integration.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SoftDataUploadService {
	
	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	private IntegrationService integrationService;
	
	@Autowired
	private ConsignmentRepository consignmentRepository;
	
	@Autowired
	private ConsignmentWebhookRepository consignmentWebhookRepository;

	@Autowired
	private PiecesDetailRepository piecesDetailRepository;

	@Autowired
	private DestinationDetailRepository destinationDetailRepository;

	@Autowired
	private OriginDetailRepository originDetailRepository;
	
	@Autowired
	private ConsignmentTrackingService consignmentTrackingService;

	private long recordId = 1;
	
	// POST - Consignment
	public ConsignmentResponse createConsignment (Consignment newConsignment, String loginUserId)
			throws IllegalAccessException, InvocationTargetException, Exception {
		try {
			ConsignmentEntity dbConsignmentEntity = new ConsignmentEntity();
			BeanUtils.copyProperties(newConsignment, dbConsignmentEntity, CommonUtils.getNullPropertyNames(newConsignment));
			
			OriginDetailsEntity dbOriginDetailsEntity = new OriginDetailsEntity();
			Origin_Details newOriginDetail = newConsignment.getOrigin_details();
			BeanUtils.copyProperties(newOriginDetail, dbOriginDetailsEntity, CommonUtils.getNullPropertyNames(newOriginDetail));
			
			dbOriginDetailsEntity.setOriginId(System.currentTimeMillis());
			
//			if(originDetailRepository.findOriginId() != null) {
//				dbOriginDetailsEntity.setOriginId(originDetailRepository.findOriginId());
//			} else {
//				dbOriginDetailsEntity.setOriginId(recordId);
//			}
			
			// State validation
			log.info("dbOriginDetailsEntity.getState() : " + dbOriginDetailsEntity.getState());
			if (dbOriginDetailsEntity.getState() == null) {
				dbOriginDetailsEntity.setState("KUWAIT");
			} else if (dbOriginDetailsEntity.getState() != null && dbOriginDetailsEntity.getState().trim().length() == 0) {
				dbOriginDetailsEntity.setState("KUWAIT");
			}
			
			DestinationDetailEntity dbDestinationDetailEntity = new DestinationDetailEntity();
			Destination_Details newDestinationDetail = newConsignment.getDestination_details();
			BeanUtils.copyProperties(newDestinationDetail, dbDestinationDetailEntity, CommonUtils.getNullPropertyNames(newDestinationDetail));
//			if(destinationDetailRepository.findDestinationId() != null) {
//				dbDestinationDetailEntity.setDestinationId(destinationDetailRepository.findDestinationId());
//			} else {
//				dbDestinationDetailEntity.setDestinationId(recordId);
//			}
			
			dbDestinationDetailEntity.setDestinationId(System.currentTimeMillis());
			
			// State validation
			if (dbDestinationDetailEntity.getState() == null) {
				dbDestinationDetailEntity.setState("KSA");
			} else if (dbDestinationDetailEntity.getState() != null && dbDestinationDetailEntity.getState().trim().length() == 0) {
				dbDestinationDetailEntity.setState("KSA");
			}
			
			dbConsignmentEntity.setCreated_at(new Date());
			
			// Call Shipsy API
			ConsignmentResponse response = integrationService.postClientSoftdataUpload (newConsignment);
			log.info("Shipsy Response : " + response);
			
			if (response != null) {
				dbConsignmentEntity.setReference_number(response.getReference_number());

				//hard code for success orders
				dbConsignmentEntity.setBoutiqaatPushStatus("PASS");
				dbConsignmentEntity.setOrderType("1");

				ConsignmentEntity createdConsignmentEntity = consignmentRepository.save(dbConsignmentEntity);
				log.info("createdConsignmentEntity ------->: " + createdConsignmentEntity);
				
				dbOriginDetailsEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
				OriginDetailsEntity createdOriginDetailsEntity = originDetailRepository.save(dbOriginDetailsEntity);
				log.info("createdOriginDetailsEntity ------->: " + createdOriginDetailsEntity);
				
				dbDestinationDetailEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
				DestinationDetailEntity createdDestinationDetail = destinationDetailRepository.save(dbDestinationDetailEntity);
				log.info("createdDestinationDetail ------->: " + createdDestinationDetail);
				
				List<PiecesDetailsEntity> listPiecesDetailsEntity = new ArrayList<>();
				if (newConsignment.getPieces_detail() != null) {
					newConsignment.getPieces_detail().forEach(piece -> {
						PiecesDetailsEntity dbPiecesDetailsEntity = new PiecesDetailsEntity();
						BeanUtils.copyProperties(piece, dbPiecesDetailsEntity, CommonUtils.getNullPropertyNames(piece));
						if(piecesDetailRepository.findPiecesId() != null) {
							dbPiecesDetailsEntity.setPiecesId(piecesDetailRepository.findPiecesId());
						} else {
							dbPiecesDetailsEntity.setPiecesId(recordId);
						}
						dbPiecesDetailsEntity.setDeclared_value(newConsignment.getDeclared_value());
						dbPiecesDetailsEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
						piecesDetailRepository.save(dbPiecesDetailsEntity);
						listPiecesDetailsEntity.add(dbPiecesDetailsEntity);
					});
				}
				log.info("createdListPiecesDetailsEntity ------->: " + listPiecesDetailsEntity);
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	// POST - CREATE ORDER LOCALLY
	public ConsignmentResponse createConsignmentInLocal (Consignment newConsignment, String loginUserId)
			throws IllegalAccessException, InvocationTargetException, Exception {
		try {
			ConsignmentEntity dbConsignmentEntity = new ConsignmentEntity();
			BeanUtils.copyProperties(newConsignment, dbConsignmentEntity, CommonUtils.getNullPropertyNames(newConsignment));
			
			OriginDetailsEntity dbOriginDetailsEntity = new OriginDetailsEntity();
			Origin_Details newOriginDetail = newConsignment.getOrigin_details();
			BeanUtils.copyProperties(newOriginDetail, dbOriginDetailsEntity, CommonUtils.getNullPropertyNames(newOriginDetail));
			if(originDetailRepository.findOriginId() != null) {
				dbOriginDetailsEntity.setOriginId(originDetailRepository.findOriginId());
			} else {
				dbOriginDetailsEntity.setOriginId(recordId);
			}
			
			DestinationDetailEntity dbDestinationDetailEntity = new DestinationDetailEntity();
			Destination_Details newDestinationDetail = newConsignment.getDestination_details();
			BeanUtils.copyProperties(newDestinationDetail, dbDestinationDetailEntity, CommonUtils.getNullPropertyNames(newDestinationDetail));
			if(destinationDetailRepository.findDestinationId() != null) {
				dbDestinationDetailEntity.setDestinationId(destinationDetailRepository.findDestinationId());
			} else {
				dbDestinationDetailEntity.setDestinationId(recordId);
			}
			
			dbConsignmentEntity.setCreated_at(new Date());
			
			dbConsignmentEntity.setReference_number(newConsignment.getReference_number());
			ConsignmentEntity createdConsignmentEntity = consignmentRepository.save(dbConsignmentEntity);
			log.info("createdConsignmentEntity ------->: " + createdConsignmentEntity);
			
			dbOriginDetailsEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
			OriginDetailsEntity createdOriginDetailsEntity = originDetailRepository.save(dbOriginDetailsEntity);
			log.info("createdOriginDetailsEntity ------->: " + createdOriginDetailsEntity);
			
			dbDestinationDetailEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
			DestinationDetailEntity createdDestinationDetail = destinationDetailRepository.save(dbDestinationDetailEntity);
			log.info("createdDestinationDetail ------->: " + createdDestinationDetail);
			
			List<PiecesDetailsEntity> listPiecesDetailsEntity = new ArrayList<>();
			if (newConsignment.getPieces_detail() != null) {
				newConsignment.getPieces_detail().forEach(piece -> {
					PiecesDetailsEntity dbPiecesDetailsEntity = new PiecesDetailsEntity();
					BeanUtils.copyProperties(piece, dbPiecesDetailsEntity, CommonUtils.getNullPropertyNames(piece));
					if(piecesDetailRepository.findPiecesId() != null) {
						dbPiecesDetailsEntity.setPiecesId(piecesDetailRepository.findPiecesId());
					} else {
						dbPiecesDetailsEntity.setPiecesId(recordId);
					}
					dbPiecesDetailsEntity.setConsignmentId(createdConsignmentEntity.getConsignmentId());
					piecesDetailRepository.save(dbPiecesDetailsEntity);
					listPiecesDetailsEntity.add(dbPiecesDetailsEntity);
				});
			}
			log.info("createdListPiecesDetailsEntity ------->: " + listPiecesDetailsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Consignment> getConsignments () {
		List<ConsignmentEntity> consignmentEntityList = consignmentRepository.findAll();
		List<Consignment> consignmentList = new ArrayList<>();
		if (!consignmentEntityList.isEmpty()) {
			consignmentEntityList.stream().forEach(c -> {
				Consignment consignment = new Consignment();
				BeanUtils.copyProperties(c, consignment, CommonUtils.getNullPropertyNames(c));
				
				OriginDetailsEntity originDetailsEntity = originDetailRepository.findByConsignmentId(c.getConsignmentId());
				if (originDetailsEntity != null) { 
					Origin_Details odetails = new Origin_Details();
					BeanUtils.copyProperties(originDetailsEntity, odetails, CommonUtils.getNullPropertyNames(originDetailsEntity));
					consignment.setOrigin_details(odetails);
				}
				
				DestinationDetailEntity destinationDetailsEntity = destinationDetailRepository.findByConsignmentId(c.getConsignmentId());
				if (destinationDetailsEntity !=  null) {
					Destination_Details ddetails = new Destination_Details();
					BeanUtils.copyProperties(destinationDetailsEntity, ddetails, CommonUtils.getNullPropertyNames(destinationDetailsEntity));
					consignment.setDestination_details(ddetails);
				}
				
				List<PiecesDetailsEntity> piecesDetailsEntityList = piecesDetailRepository.findByConsignmentId(c.getConsignmentId());
				Set<Pieces_Details> piecesDetailsList = new HashSet<>();
				piecesDetailsEntityList.stream().forEach(p -> {
					Pieces_Details pdetails = new Pieces_Details();
					BeanUtils.copyProperties(p, pdetails, CommonUtils.getNullPropertyNames(p));
					piecesDetailsList.add(pdetails);
				});
				consignment.setPieces_detail(piecesDetailsList);
				consignmentList.add(consignment);
			});
			return consignmentList;
		}
		return consignmentList;
	}
	
	/**
	 * 
	 * @param referenceNumber
	 * @return
	 */
	public Consignment getConsignment (String referenceNumber) {
		ConsignmentEntity consignmentEntity = consignmentRepository.findConsigmentUniqueRecord(referenceNumber);
		if (consignmentEntity != null) {
			Consignment consignment = new Consignment();
			BeanUtils.copyProperties(consignmentEntity, consignment, CommonUtils.getNullPropertyNames(consignmentEntity));
			
			OriginDetailsEntity originDetailsEntity = 
					originDetailRepository.findByConsignmentId(consignmentEntity.getConsignmentId());
			if (originDetailsEntity != null) { 
				Origin_Details odetails = new Origin_Details();
				BeanUtils.copyProperties(originDetailsEntity, odetails, CommonUtils.getNullPropertyNames(originDetailsEntity));
				consignment.setOrigin_details(odetails);
			}
			
			DestinationDetailEntity destinationDetailsEntity =
					destinationDetailRepository.findByConsignmentId(consignmentEntity.getConsignmentId());
			if (destinationDetailsEntity !=  null) {
				Destination_Details ddetails = new Destination_Details();
				BeanUtils.copyProperties(destinationDetailsEntity, ddetails, CommonUtils.getNullPropertyNames(destinationDetailsEntity));
				consignment.setDestination_details(ddetails);
			}
			
			List<PiecesDetailsEntity> piecesDetailsEntityList = 
					piecesDetailRepository.findByConsignmentId(consignmentEntity.getConsignmentId());
			Set<Pieces_Details> piecesDetailsList = new HashSet<>();
			piecesDetailsEntityList.stream().forEach(p -> {
				Pieces_Details pdetails = new Pieces_Details();
				BeanUtils.copyProperties(p, pdetails, CommonUtils.getNullPropertyNames(p));
				piecesDetailsList.add(pdetails);
			});
			consignment.setPieces_detail(piecesDetailsList);
			return consignment;
		} else {
			Consignment consignment = getConsignmentFromShipsy (referenceNumber);
			log.info("consignment----------from Shipsy------> : " + consignment);
			return consignment;
		}
	}
	
	/**
	 * 
	 * @param referenceNumber
	 * @return
	 */
	public Consignment getConsignmentFromShipsy (String referenceNumber) {
		ConsignmentTracking consignmentTracking = consignmentTrackingService.getConsignmentTracking(referenceNumber);
		Consignment consignment = new Consignment();
		BeanUtils.copyProperties(consignmentTracking, consignment, CommonUtils.getNullPropertyNames(consignmentTracking));
		if (consignmentTracking.getWeight() != null) {
			double weight = Double.valueOf(consignmentTracking.getWeight()).doubleValue();
			consignment.setWeight(weight);
		}
		
		if (consignmentTracking.getNum_pieces() != null) {
			long num_pieces = Long.valueOf(consignmentTracking.getNum_pieces()).longValue();
			consignment.setNum_pieces(num_pieces);
		}
		
		OriginDetail originDetailsEntity = consignmentTracking.getOrigin_details();
		if (originDetailsEntity != null) { 
			Origin_Details odetails = new Origin_Details();
			BeanUtils.copyProperties(originDetailsEntity, odetails, CommonUtils.getNullPropertyNames(originDetailsEntity));
			consignment.setOrigin_details(odetails);
		}
		
		DestinationDetail destinationDetailsEntity = consignmentTracking.getDestination_details();
		if (destinationDetailsEntity !=  null) {
			Destination_Details ddetails = new Destination_Details();
			BeanUtils.copyProperties(destinationDetailsEntity, ddetails, CommonUtils.getNullPropertyNames(destinationDetailsEntity));
			consignment.setDestination_details(ddetails);
		}
		
		List<PiecesDetail> piecesDetailsEntityList = consignmentTracking.getPieces_detail();
		Set<Pieces_Details> piecesDetailsList = new HashSet<>();
		if (piecesDetailsEntityList != null) {
			piecesDetailsEntityList.stream().forEach(p -> {
				Pieces_Details pdetails = new Pieces_Details();
				BeanUtils.copyProperties(p, pdetails, CommonUtils.getNullPropertyNames(p));
				piecesDetailsList.add(pdetails);
			});
			consignment.setPieces_detail(piecesDetailsList);
		}
		return consignment;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public List<ConsignmentWebhook> getConsignmentByType(String type) {
		List<ConsignmentWebhookEntity> consignmentWebhookEntityList = consignmentWebhookRepository.findByType(type);
		if (consignmentWebhookEntityList != null && !consignmentWebhookEntityList.isEmpty()) {
			List<ConsignmentWebhook> consignmentWebhookList = new ArrayList<>();
			consignmentWebhookEntityList.stream().forEach(p -> {
				ConsignmentWebhook consignmentWebhook = new ConsignmentWebhook();
				BeanUtils.copyProperties(p, consignmentWebhook, CommonUtils.getNullPropertyNames(p));
				consignmentWebhookList.add(consignmentWebhook);
			});
			return consignmentWebhookList;
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getConsigmentByQP() {
		List<String> qpConsignments = consignmentRepository.findConsigmentByQP();
		return qpConsignments;
	}
	
	/**
	 * 
	 * @param billCode
	 * @return
	 */
	public String getConsigmentByJntBillCode(String billCode) {
		String reference_number = consignmentRepository.findConsigmentByBillCode(billCode);
		return reference_number;
	}
	
	/**
	 * 
	 * @param referenceNumber
	 * @param billCode
	 * @param jNT_HUBCODE 
	 * @return
	 */
	public ConsignmentEntity updateConsignment (String referenceNumber, String billCode, String jNT_HUBCODE) {
		ConsignmentEntity consignmentEntity = consignmentRepository.findConsigmentUniqueRecord(referenceNumber);
		consignmentEntity.setAwb_3rd_Party(billCode);
		consignmentEntity.setHubCode_3rd_Party(jNT_HUBCODE);
		//hard code for success orders
		consignmentEntity.setJntPushStatus("PASS");
		consignmentEntity.setOrderType("1");

		return consignmentRepository.save(consignmentEntity);
	}
}
