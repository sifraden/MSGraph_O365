package com.demo.api.myseat;


public interface MySeatAPIService {

	public String getChairs(String keyApi) throws Exception;
	
	public String getChairsInGroup(String keyApi, String groupId) throws Exception;
}
