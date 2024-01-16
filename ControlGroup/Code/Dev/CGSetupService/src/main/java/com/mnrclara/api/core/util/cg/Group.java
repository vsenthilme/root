package com.mnrclara.api.core.util.cg;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class Group {

    @Valid
    private List<LikeMatchGroup> likeMatchGroup;

    @Valid
    private List<ExactMatchGroup> exactMatchGroups;
    
    /**
     * 
     * @return
     */
    private static List<ExactMatchGroup> exactMatchGroups () {
    	List<ExactMatchGroup> exactMatchGroups = new ArrayList<>();
    	ExactMatchGroup exactMatchGroup1 = new ExactMatchGroup();
    	ExactMatchGroup exactMatchGroup2 = new ExactMatchGroup();
    	ExactMatchGroup exactMatchGroup3 = new ExactMatchGroup();
    	List<Stores> stores = new ArrayList<>();
    	List<Stores> stores2 = new ArrayList<>();
    	List<Stores> stores3 = new ArrayList<>();
    	
    	/*
    	 *  "storeId": "10",
          "storeName": "10-1",
          "coOwnerName1": "10012-CO1",
          "coOwnerName2": "10017-CO2",
          "coOwnerName3": "10014-CO3",
          "coOwnerName4": "null-null",
          "coOwnerName5": "null-null",
    	 */
    	Stores store = new Stores();
    	store.setStoreId("10");
    	store.setStoreName("XXXX");
    	store.setCoOwnerName1("10012-CO1");
    	store.setCoOwnerName2("10017-CO2");
    	store.setCoOwnerName3("10014-CO3");
    	store.setCoOwnerName4("null-null");
    	store.setCoOwnerName5("null-null");
    	store.setCoOwnerPercentage1(45D);
    	store.setCoOwnerPercentage2(56D);
    	store.setCoOwnerPercentage3(66D);
    	stores.add(store);
    	exactMatchGroup1.setStores(stores);
    	
    	Stores store1 = new Stores();
    	store1.setStoreId("101");
    	store1.setStoreName("YYYY");
    	store1.setCoOwnerName1("10012-CO4");
    	store1.setCoOwnerName2("10017-CO5");
    	store1.setCoOwnerName3("10014-CO6");
    	store1.setCoOwnerName4("null-null");
    	store1.setCoOwnerName5("null-null");
    	store1.setCoOwnerPercentage1(22D);
    	store1.setCoOwnerPercentage2(61D);
    	store1.setCoOwnerPercentage3(87D);
    	stores2.add(store1);
    	exactMatchGroup2.setStores(stores2);
    	
    	Stores store2 = new Stores();
    	store2.setStoreId("1011");
    	store2.setStoreName("ZZZZ");
    	store2.setCoOwnerName1("10012-10012-Salvador Ortega");
    	store2.setCoOwnerName2("10014-10014-ROGA LLC");
    	store2.setCoOwnerName3("null-null");
    	store2.setCoOwnerName4("null-null");
    	store2.setCoOwnerName5("null-null");
    	store2.setCoOwnerPercentage1(82D);
    	store2.setCoOwnerPercentage2(13D);
    	store2.setCoOwnerPercentage3(54D);
    	stores3.add(store2);
    	exactMatchGroup3.setStores(stores3);
    	
    	exactMatchGroups.add(exactMatchGroup1);
    	exactMatchGroups.add(exactMatchGroup2);
    	exactMatchGroups.add(exactMatchGroup3);
    	return exactMatchGroups;
    }
    
    /**
     * 
     * @param result
     * @return
     */
    private static String extractData (String result) {
    	if (result != null) {
	    	result = result.substring(0, result.indexOf("-"));
	    	return result;
    	}
    	return "";
    }
    
    @Data
    class InputParams {
    	private String coOwnerId1 = "10012";
    	private String coOwnerId2 = "10014";
    	private String coOwnerId3 = "10017";
    	private String coOwnerId4 = null;
    	private String coOwnerId5 = null;
    	
    	/**
    	 * 
    	 * @return
    	 */
    	public List<String> inputs() {
    		List<String> ipList = new ArrayList<>();
			ipList.add(coOwnerId1);
			ipList.add(coOwnerId2);
			ipList.add(coOwnerId3);
			ipList.add(coOwnerId4);
			ipList.add(coOwnerId5);
			return ipList;
    	}
    	
    	/**
    	 * 
    	 * @return
    	 */
    	public List<String> inputsGiven() {
    		List<String> ipList = new ArrayList<>();
    		if (coOwnerId1 != null) {
    			ipList.add(coOwnerId1);
    		} 
    		
    		if (coOwnerId2 != null) {
    			ipList.add(coOwnerId2);
    		} 
			
    		if (coOwnerId3 != null) {
    			ipList.add(coOwnerId3);
    		} 
			
    		if (coOwnerId4 != null) {
    			ipList.add(coOwnerId4);
    		} 
			
    		if (coOwnerId5 != null) {
    			ipList.add(coOwnerId5);
    		} 
			
			return ipList;
    	}
    }
    
    /**
     * 
     * @return
     */
    private static List<CoOwnerStoresPercentageCollection> extractResults () {
    	Group.InputParams ip = new Group().new InputParams();
    	List<ExactMatchGroup> exactMatchGroupList = exactMatchGroups();
    	
    	List<CoOwnerStoresPercentageCollection> finalCollections = new ArrayList<>();
    	List<CoOwnerStoresPercentage> coStorePercList = new ArrayList<>();
    	exactMatchGroupList.stream().forEach(e -> {
			e.getStores().stream().forEach(s -> {
				CoOwnerStoresPercentage s1 = findOut(s, "10012");
				CoOwnerStoresPercentage s2 = findOut(s, "10014");
				CoOwnerStoresPercentage s3 = findOut(s, "10017");
				System.out.println ("ijk ---> : " + s1 +"," + s2 + "," + s3);
				
				if (s1 != null && s2 != null && s3 != null) {
					coStorePercList.add(s1);
					coStorePercList.add(s2);
					coStorePercList.add(s3);
					CoOwnerStoresPercentageCollection c = new CoOwnerStoresPercentageCollection();
					c.setCoOwnerStoresPercCollections(coStorePercList);
					finalCollections.add(c);
				}
			});
    	});
    	System.out.println ("finalCollections ---> : " + finalCollections);
    	return finalCollections;
    }
    
    /**
     * 
     * @param s
     * @param ip
     * @return
     */
    private static CoOwnerStoresPercentage findOut(Stores s, String ip) {
    	if (extractData(s.getCoOwnerName1()).equalsIgnoreCase(ip)) {
    		return populateCoOwnerStoresPercentage (s);
    	} else if (extractData(s.getCoOwnerName2()).equalsIgnoreCase(ip)) {
    		return populateCoOwnerStoresPercentage (s);
    	} else if (extractData(s.getCoOwnerName3()).equalsIgnoreCase(ip)) {
    		return populateCoOwnerStoresPercentage (s);
    	}
		return null;
	}
    
    /**
     * 
     * @param s
     * @return
     */
    private static CoOwnerStoresPercentage populateCoOwnerStoresPercentage (Stores s) {
    	List<StorePerc> storesPerc5 = new ArrayList<>();
    	CoOwnerStoresPercentage coStorePerc = new CoOwnerStoresPercentage();
		coStorePerc.setCoOwnerName(extractData (s.getCoOwnerName5()));
		StorePerc storePerc = new StorePerc();
		storePerc.setStoreId(s.getStoreId());
		storePerc.setStoreName(s.getStoreName());
		storePerc.setStorePercentage(s.getCoOwnerPercentage5());
		storesPerc5.add(storePerc);
		coStorePerc.setStoresPerc(storesPerc5);
		return coStorePerc;
    }

	public static void main(String[] args) {
    	extractResults ();
	}
}
