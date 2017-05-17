package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.Asset;
import org.tenbitworks.repositories.AssetRepository;

@Controller
public class AssetController {

    @Autowired
    AssetRepository assetRepository;

    @RequestMapping(value="/assets/{id}", method=RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getAsset(@PathVariable Long id, Model model){
        model.addAttribute("asset", assetRepository.findOne(id));
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("assetcount", assetRepository.count());
        return "assets";
    }
    
    @RequestMapping(value="/assets/{id}", method=RequestMethod.GET, produces={"application/json"})
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Asset getAssetJson(@PathVariable Long id, Model model){
        Asset asset = assetRepository.findOne(id);
        return asset;
    }
    
    @RequestMapping(value = "/assets",method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String assetsList(Model model){
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("assetcount", assetRepository.count());
        return "assets";
    }

    @RequestMapping(value = "/assets", method = RequestMethod.POST)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public Long saveAsset(@RequestBody Asset asset) {
        assetRepository.save(asset);
        return asset.getId();
    }

    @RequestMapping(value = "/assets/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public String removeAsset(@PathVariable Long id){
        assetRepository.delete(id);
        return id.toString();
    }
}
