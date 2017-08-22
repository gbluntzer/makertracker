package org.tenbitworks.controllers;

import java.util.Arrays;
import java.util.List;

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
	//TODO Create a client for a thing
	
	@Autowired
	AssetRepository assetRepository;

	@Autowired
	MemberRepository memberRepository;
	
	@RequestMapping(
			value="/api/interlock/{assetId}/rfids/{rfid}", 
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
	
	@RequestMapping(
			value="/api/interlock/{assetId}/rfids", 
			method=RequestMethod.GET, 
			produces={"application/json"})
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_API')")
	public List<InterlockAccessDTO> getValidRfidsForAsset(
			@PathVariable String assetId){
		
		//TODO lookup this from trained member info for asset
		InterlockAccessDTO iad = new InterlockAccessDTO();
		iad.setAccessGranted(true);
		iad.setAccessTimeMS(10000);
		
		return Arrays.asList(iad);
	}
}
