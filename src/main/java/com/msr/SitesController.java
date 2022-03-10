package com.msr;

import com.msr.model.Site;
import com.msr.service.SiteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Respond to site requests
 */
@RestController
@RequestMapping("/sites")
public class SitesController {

    @Autowired
    private SiteService siteService;

    /**
     * Returns a site with total_size and primary_type elements populated
     *
     * @param id  The site id
     * @return A Json response of the requested site object
     */
    @ApiOperation("Returns a site resource by its identifier with both supplemental fields (total_size, primary_type) populated.")
    @GetMapping("/site/{id}")
    public Site getSiteById(
                            @ApiParam("The unique site id.  This is the primary key in the database.")
                            @PathVariable Integer id) {
        return siteService.findSiteById(id);
    }

    /**
     * Returns a list of all sites
     *
     * @return A list of all the sites
     */
    @ApiOperation("Returns a list of all sites with both supplemental fields (total_size, primary_type) populated.")
    @GetMapping("/")
    public List<Site> getAllSites() {
        return siteService.findAll();
    }

    @GetMapping(value = "/", params = "state")
    public List<Site> getAllSitesByState(
                                        @ApiParam("The 2 digit state code to search for")
                                         @RequestParam String state) {

        // TODO validate state code
        return siteService.findAllByState(state);
    }
}