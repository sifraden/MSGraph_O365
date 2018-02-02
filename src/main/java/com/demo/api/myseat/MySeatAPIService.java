package com.demo.api.myseat;

import com.demo.model.myseat.chairs.Content;

public interface MySeatAPIService {

	public Content getChairs(String keyApi) throws Exception;
	
	public Content getChairsInGroup(String keyApi, String groupId) throws Exception;
}
