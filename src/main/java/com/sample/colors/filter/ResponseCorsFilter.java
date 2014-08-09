package com.sample.colors.filter;
 
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
 
public class ResponseCorsFilter implements ContainerResponseFilter {
 
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
 
//        ResponseBuilder resp = Response.fromResponse(contResp.getResponse());
//        resp.header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
// 
//        String reqHead = req.getHeaderValue("Access-Control-Request-Headers");
// 
//        if(null != reqHead && !reqHead.equals("")){
//            resp.header("Access-Control-Allow-Headers", reqHead);
//        }
// 
//        contResp.setResponse(resp.build());
//            return contResp;
    	//add CORS headers
    	MultivaluedMap headers = response.getHttpHeaders();
    	//allow all origins
    	headers.add("Access-Control-Allow-Origin", "*");
    	//allow all methods
    	headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
    	//allow all requested headers
    	String requestHeaders = request.getHeaderValue("Access-Control-Request-Headers");
    	if (requestHeaders != null) {
    		headers.add("Access-Control-Allow-Headers", requestHeaders);
    	}
    	return response;
    	
    }
 
}

