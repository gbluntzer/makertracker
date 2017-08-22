package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.dto.InterlockAccessDTO;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.MemberRepository;

@Controller
public class InterlockController {

	@Autowired
	AssetRepository assetRepository;

	@Autowired
	MemberRepository memberRepository;
	
	@RequestMapping(
			value="/api/interlock/{assetId}/rfid/{rfid}", 
			method=RequestMethod.GET, 
			produces={"application/json"})
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_API')")
	public InterlockAccessDTO checkAccessToAsset(
			@PathVariable String assetId, 
			@PathVariable String rfid){
		
		//TODO lookup this from trained member info for asset
		InterlockAccessDTO iad = new InterlockAccessDTO();
		iad.setAccessGranted(true);
		iad.setAccessTimeMS(10000);
		
		return iad;
	}
}
