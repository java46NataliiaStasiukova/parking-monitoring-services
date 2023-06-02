package parking.monitoring.controller;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import parking.monitoring.service.ProxyService;

@RestController
public class GateWayController {

	ProxyService proxyService;

	private GateWayController(ProxyService proxyService) {
		this.proxyService = proxyService;
	}
	
	@GetMapping("/**")
	ResponseEntity<byte[]> getRequests(ProxyExchange<byte[]> proxy,
			HttpServletRequest request) {
		return proxyService.proxyRouting(proxy, request, HttpMethod.GET);
		
	}
	
	@PostMapping("/**")
	ResponseEntity<byte[]> postRequests(ProxyExchange<byte[]> proxy,
			HttpServletRequest request) {
		return proxyService.proxyRouting(proxy, request, HttpMethod.POST);
		
	}
}
