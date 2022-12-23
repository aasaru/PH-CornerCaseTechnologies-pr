/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.36).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Problem;
import io.swagger.model.RoleDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-23T08:01:40.233Z[GMT]")
@Validated
public interface RolesApi {

    @Operation(summary = "List roles with translations", description = "When 'If-Modified-Since' is included in the request the service responds HTTP Status code 304 if nor roles have been changed since that time. Otherwise all results (that match the filters) are returned (even the ones that have modified time earlier than If-Modified-Since parameter). ", tags={ "All queries", "Offered by Pääsuke", "Offered to Pääsuke" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List roles of some namespace(s) with translations", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoleDefinition.class)))),
        
        @ApiResponse(responseCode = "304", description = "List of roles is not modified. Might be returned only if request sent additional header \"If-Modified-Since\" and no changes have been done since that date."),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/roles",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<RoleDefinition>> getRoles(@Parameter(in = ParameterIn.HEADER, description = "" ,schema=@Schema()) @RequestHeader(value="If-Modified-Since", required=false) OffsetDateTime ifModifiedSince, @Parameter(in = ParameterIn.QUERY, description = "Filter by namespace(s)" ,schema=@Schema()) @Valid @RequestParam(value = "namespace", required = false) List<String> namespace);

}

