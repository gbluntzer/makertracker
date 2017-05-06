package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.Asset;
import org.tenbitworks.repositories.AssetRepository;

@Controller
public class AssetController {

    @Autowired
    AssetRepository assetRepository;


    @RequestMapping("/asset/{id}")
    public String product(@PathVariable Long id, Model model){
        model.addAttribute("asset", assetRepository.findOne(id));
        return "asset";
    }
    @RequestMapping("/getasset")
    @ResponseBody
    public Asset getAsset(@RequestParam Long Id, Model model){
        Asset asset = assetRepository.findOne(Id);
        return asset;
    }
    @RequestMapping(value = "/assets",method = RequestMethod.GET)
    public String assetsList(Model model){
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("assetcount", assetRepository.count());
        return "assets";
    }

    @RequestMapping(value = "/saveasset", method = RequestMethod.POST)
    @ResponseBody
    public Long saveAsset(@RequestBody Asset asset) {
        assetRepository.save(asset);
        return asset.getId();
    }

    @RequestMapping(value = "/removeasset", method = RequestMethod.POST)
    @ResponseBody
    public String removeMemeber(@RequestParam Long Id){
        assetRepository.delete(Id);
        return Id.toString();
    }



}
