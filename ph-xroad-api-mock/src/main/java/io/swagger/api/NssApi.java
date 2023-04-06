/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.41).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.MandateToSubDelegate;
import io.swagger.model.Problem;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-03-28T13:19:29.375049256Z[GMT]")
@Validated
public interface NssApi {

    @Operation(summary = "Sub-delegate a single mandate", description = "To add a sub-delegate for a specific mandate. The values of representeeId, delegateId and mandateId are taken from the output of the Mandate, from the 'addSubDelegate' link. ", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Created"),
        
        @ApiResponse(responseCode = "202", description = "Accepted for processing but yet not added"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Request not authorized."),
        
        @ApiResponse(responseCode = "404", description = "Endpoint was not found."),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. Something went wrong. If mandate was not found then the system should retrun this response together with translation.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))),
        
        @ApiResponse(responseCode = "500", description = "Reserved for errors returned by security server."),
        
        @ApiResponse(responseCode = "501", description = "This operation is not supported.") })
    @RequestMapping(value = "/nss/{ns}/representees/{representeeId}/delegates/{delegateId}/mandates/{mandateId}/subdelegates",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> addSubDelegate(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of representee identifier (like internal person id). Pääsuke takes this value from 'addSubDelegate' link.", required=true, schema=@Schema()) @PathVariable("representeeId") String representeeId, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of delegate identifier (like internal person id). Pääsuke takes this value from 'addSubDelegate' link.", required=true, schema=@Schema()) @PathVariable("delegateId") String delegateId, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of mandate (or role) identifier (like internal mandate id). Pääsuke takes this value from 'addSubDelegate' link.", required=true, schema=@Schema()) @PathVariable("mandateId") String mandateId, @Parameter(in = ParameterIn.HEADER, description = "User identifier whose action initiated the request. NB! This can be employee of RIA." ,schema=@Schema()) @RequestHeader(value="X-Road-User-Id", required=false) String xRoadUserId, @Parameter(in = ParameterIn.HEADER, description = "When service client represents third party while issuing a query then it must be filled with identifier of that third party. When service client represents itself then this header is omitted." ,schema=@Schema()) @RequestHeader(value="X-Road-Represented-Party", required=false) String xRoadRepresentedParty, @Parameter(in = ParameterIn.HEADER, description = "Unique identifier (UUID) for this message." ,schema=@Schema()) @RequestHeader(value="X-Road-Id", required=false) String xRoadId, @Parameter(in = ParameterIn.DEFAULT, description = "Details of the person whom the mandate is being sub-delegated, also the validity period and the details why Pääsuke allowed this operation to take place. <p><b>Identifier of the subDelegate (edasivolitatu)</b> can be one of the follwing&colon;</p> <p>a) 'EE' followed by 11-digit national identity number</p> <p>b) Two-letter country code followed by eIDAS identification (1...254 symbols) - Pääsuke uses the value that was returned by Tara when the person logged into eesti.ee portal.</p><p>The sub-delegate cannot be a legal person.</p><p>The <b>validityPeriod</b> can only be within the original validity period and validity period.</p><p>The <b>authorizations</b> block informs why Pääsuke came to a conclusin that this user has the authorization to sub-delegate this mandate. The userIdentifier always points to the acutal natural person who confirmed the action from the user interface.</p>", schema=@Schema()) @Valid @RequestBody MandateToSubDelegate body);


    @Operation(summary = "Delete a specific mandate from the delegate", description = "To delete a specific mandate. The values of representeeId, delegateId and mandateId are taken from the output of the Mandate, from the delete link. ", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Deleted sucessfully"),
        
        @ApiResponse(responseCode = "202", description = "Request accepted but not yet deleted"),
        
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Request not authorized."),
        
        @ApiResponse(responseCode = "404", description = "Resource to delete was not found."),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. Something went wrong.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))),
        
        @ApiResponse(responseCode = "500", description = "This error code is reserved for security server."),
        
        @ApiResponse(responseCode = "501", description = "This operation is not supported.") })
    @RequestMapping(value = "/nss/{ns}/representees/{representeeId}/delegates/{delegateId}/mandates/{mandateId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteMandate(@Parameter(in = ParameterIn.PATH, description = "Namespace of mandate to be deleted.", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of representee identifier (like internal person id). Pääsuke takes this value from 'delete' link.", required=true, schema=@Schema()) @PathVariable("representeeId") String representeeId, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of delegate identifier (like internal person id). Pääsuke takes this value from 'delete' link.", required=true, schema=@Schema()) @PathVariable("delegateId") String delegateId, @Parameter(in = ParameterIn.PATH, description = "Implementator can use any form of mandate (or role) identifier (like internal mandate id). Pääsuke takes this value from 'delete' link.", required=true, schema=@Schema()) @PathVariable("mandateId") String mandateId, @Parameter(in = ParameterIn.HEADER, description = "User identifier whose action initiated the request. NB! This can be employee of RIA." ,schema=@Schema()) @RequestHeader(value="X-Road-User-Id", required=false) String xRoadUserId, @Parameter(in = ParameterIn.HEADER, description = "When service client represents third party while issuing a query then it must be filled with identifier of that third party. When service client represents itself then this header is omitted." ,schema=@Schema()) @RequestHeader(value="X-Road-Represented-Party", required=false) String xRoadRepresentedParty, @Parameter(in = ParameterIn.HEADER, description = "Unique identifier (UUID) for this message." ,schema=@Schema()) @RequestHeader(value="X-Road-Id", required=false) String xRoadId);

}

