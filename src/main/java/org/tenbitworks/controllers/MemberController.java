package org.tenbitworks.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.dto.AssetDTO;
import org.tenbitworks.dto.UpdateMemberDTO;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.UserRepository;

@Controller
public class MemberController {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AssetRepository assetRepository;

	@RequestMapping(value="/members/{id}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'read')")
	public Member getMemberJson(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
		return memberRepository.findOne(id);
	}
	
	@RequestMapping(value="/members/{id}/assets", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'read')")
	public List<AssetDTO> getMemberAssetsJson(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
		List<Asset> assets = assetRepository.findAllByMembers(memberRepository.findOne(id));
		List<AssetDTO> assetList = new ArrayList<>();
		for (Asset asset : assets) {
			AssetDTO dto = new AssetDTO();
			dto.setId(asset.getId());
			dto.setTitle(asset.getTitle());
			
			assetList.add(dto);
		}
		return assetList;
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'read')")
	public String getMember(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
		model.addAttribute("membercount", memberRepository.count());

		if (security.isUserInRole("ADMIN")) {
			model.addAttribute("members", memberRepository.findAll(new Sort(Direction.ASC, "memberName")));
		} else {
			model.addAttribute("members", Arrays.asList(new Member[] { memberRepository.findOne(id) }));
		}
		
		Member member = memberRepository.findOne(id);
		model.addAttribute("member", member);
		model.addAttribute("asset", assetRepository.findAllByMembers(member));
		
		return "members";
	}

	@RequestMapping(value = "/members", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public String membersList(Model model, SecurityContextHolderAwareRequestWrapper security) {
		if (security.isUserInRole("ADMIN")) {
			model.addAttribute("members", memberRepository.findAll(new Sort(Direction.ASC, "memberName")));
		} else {
			org.tenbitworks.model.User user = userRepository.findOne(security.getUserPrincipal().getName());
			Member m = memberRepository.findOneByUser(user);
			if (m != null) {
				model.addAttribute("members", Arrays.asList(new Member[] { m }));
				model.addAttribute("member", m);
			}
		}

		model.addAttribute("membercount", memberRepository.count());
		return "members";
	}

	@RequestMapping(value = "/members", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('ADMIN')")
	public UUID saveMember(@RequestBody Member member) {
		if (member.getId() != null) {
			Member oldMember = memberRepository.findOne(member.getId());
			member.setUser(oldMember.getUser());
		}

		memberRepository.save(member);
		return member.getId();
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'write')")
	public ResponseEntity<String> updateMember(@PathVariable UUID id, @RequestBody @Valid UpdateMemberDTO updatedMember) {
		Member member = memberRepository.findOne(id);

		if (member == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		//Copy selected fields to existing member for update. We don't want a user to just update everything
		member.setAddress(updatedMember.getAddress());
		member.setDescription(updatedMember.getDescription());
		member.setEmail(updatedMember.getEmail());
		member.setPhoneNumber(updatedMember.getPhoneNumber());
		member.setZipCode(updatedMember.getZipCode());

		memberRepository.save(member);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@PreAuthorize("hasRole('ADMIN')")
	public String removeMember(@PathVariable UUID id){
		memberRepository.delete(id);
		return id.toString();
	}
}
