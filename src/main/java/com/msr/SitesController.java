package com.msr;

import com.msr.model.Site;
import com.msr.service.SiteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
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
    @GetMapping(value = "/site/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Site getSiteById(
                            @ApiParam(
                                    name = "id", example = "1",
                                    value = "The unique site id.  This is the primary key in the database.")
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

    @ApiOperation("Returns a list of all sites with the given state code")
    @GetMapping(value = "/search", params = "state")
    public List<Site> getAllSitesByState(
                                        @ApiParam(name = "state", value = "The 2 digit state code to search for", example = "CA")
                                         @RequestParam String state) {

        // TODO validate state code
        return siteService.findAllByState(state);
    }

    @ApiOperation("Returns a list of all sites that have site use with square footage greater than the specified velu")
    @GetMapping(value = "/search", params = "sqft")
    public List<Site> getAllSitesBySiteUseGreaterThan(@ApiParam(name = "sqft", value = "Check if any site's sqft is greater than this value", example = "15000")
                                                      @RequestParam Long sqft) {
        return siteService.findAllBySiteUseGreaterThan(sqft);
    }

    /**
     * Controller method to insert or update Site object.
     * Using Post method for both but could split out updates into PUT.
     *
     * NOTE: Using ResponseEntity here for more control of HttpStatus
     *
     * @param Site object as JSON
     * @return
     */
    @ApiOperation("Saves a site object given a site request body.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Site> saveSite(
                                            @ApiParam(name="site", value="The site request body in JSON format",
                                                example = "  {\n" +
                                                        "    \"site\": {\n" +
                                                        "        \"name\": \"New Site\",\n" +
                                                        "\t \"address\": \"123 fake st\",\n" +
                                                        "\t \"city\": \"Boston\",\n" +
                                                        "\t \"state\": \"MA\",\n" +
                                                        "\t \"zipcode\": \"02149\"\n" +
                                                        "    }\n" +
                                                        "  }")
                                            @RequestBody Site newSite) throws ServerException {

        Site site = siteService.saveSite(newSite);
        if (site == null) {
            throw new ServerException("Could not save site");
        } else {
            return new ResponseEntity<>(site, HttpStatus.OK);  // TODO Could use HttpStatus.CREATED but would need to check if object was updated or created
        }
    }
}