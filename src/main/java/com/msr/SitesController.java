package com.msr;

import com.msr.model.Site;
import com.msr.service.SiteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Respond to site requests
 */
@RestController
@RequestMapping("/sites")
public class SitesController {

    @Autowired
    private SiteService siteService;

    /* Sample Output messages. */
    private final static String SAMPLE_RESPONSE_BASE = "This is a sample response to test if SitesController is responding appropriately. ";
    static final String SAMPLE_PARAM_PROVIDED = SAMPLE_RESPONSE_BASE + "The request param you passed was: ";
    static final String NO_SAMPLE_PARAM_PROVIDED = SAMPLE_RESPONSE_BASE + "No request param was provided.";
    static final String SAMPLE_EXCEPTION_MESSAGE = SAMPLE_RESPONSE_BASE + "An expected error was thrown.";

    /**
     * Used simply to check if this controller is responding to requests.
     * Has no function other than echoing.
     *
     * @return A sample message based on the input parameters.
     * @throws RuntimeException Only when 'throwError' is true.
     */
    @ApiOperation("Returns a sample message for baseline controller testing.")
    @GetMapping("/sample")
    public String getSampleResponse(@ApiParam("The message that will be echoed back to the user.")
                                    @RequestParam(required = false) final String message,
                                    @ApiParam("Forces this endpoint to throw a generic error.")
                                    @RequestParam(required = false) final boolean throwError) {
        String response;
        if (throwError) {
            throw new RuntimeException(SAMPLE_EXCEPTION_MESSAGE);
        } else if (!StringUtils.hasLength(message)) {
            response = NO_SAMPLE_PARAM_PROVIDED;
        } else {
            response = SAMPLE_PARAM_PROVIDED + message;
        }
        return response;
    }

    @ApiOperation("Returns a site by its identifier.")
    @GetMapping("/site/{id}")
    public Site getSiteById(@PathVariable Integer id) {

        return Site.builder()
                .id(1)
                .name("Measurabl HQ")
                .address("707 Broadway Suite 1000")
                .city("San Diego")
                .state("CA")
                .zipcode("92101")
                .totalSize(Long.parseLong("13000"))
                .build()
            ;
        //return siteService.getSiteById(id);
    }
}