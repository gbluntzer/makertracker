package org.tenbitworks.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.dto.InterlockAccessDTO;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.Member;
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
			method=RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_API')")
	public ResponseEntity<Long> checkAccessToAsset(
			@PathVariable long assetId, 
			@PathVariable String rfid){
		
		Asset asset = assetRepository.findOne(assetId);
		if (asset == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Member member = memberRepository.findOneByRfid(rfid);
		if (member == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (checkAccess(asset, member)) {
			return new ResponseEntity<Long>(asset.getAccessControlTimeMS(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Long>(HttpStatus.FORBIDDEN);
		}
	}
	
	@RequestMapping(
			value="/api/interlock/{assetId}/rfids", 
			method=RequestMethod.GET, 
			produces={"application/json"})
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_API')")
	public ResponseEntity<InterlockAccessDTO> getValidRfidsForAsset(
			@PathVariable long assetId){
		
		Asset asset = assetRepository.findOne(assetId);
		if (asset == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		InterlockAccessDTO iad = new InterlockAccessDTO();
		iad.setAccessTimeMS(asset.getAccessControlTimeMS());
		iad.setTrainingRequired(asset.isTrainingRequired());
		iad.setAssetId(assetId);
		
		List<String> rfidList = new ArrayList<>();
		if (asset.isTrainingRequired()) {
			for (Member m : asset.getMembers()) {
				rfidList.add(m.getRfid());
			}
		} else {
			for (Member m : memberRepository.findAll()) {
				rfidList.add(m.getRfid());
			}
		}
		
		iad.setRfidList(rfidList);
		
		return new ResponseEntity<>(iad, HttpStatus.OK);
	}
	
	private static boolean checkAccess(Asset asset, Member member) {
		if (asset.isTrainingRequired() && asset.getMembers().contains(member)) {
			return true;
		} else if (!asset.isTrainingRequired()) {
			return true;
		} else {
			return false;
		}
	}
}
