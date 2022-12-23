/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.36).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.AddMandateTriplet;
import io.swagger.model.MandateToSubDelegate;
import io.swagger.model.NamespaceDefinition;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-23T08:01:40.233Z[GMT]")
@Validated
public interface NsApi {

    @Operation(summary = "Sub-delegate a single mandate", description = "", tags={ "All queries", "Offered to Pääsuke (additional)" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "202", description = "Accepted for processing"),
        
        @ApiResponse(responseCode = "400", description = "Incorrect format of the payload.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))),
        
        @ApiResponse(responseCode = "403", description = "You are not owner of the namespace."),
        
        @ApiResponse(responseCode = "404", description = "Delegate doesn't have anything matching to remove"),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/ns/{ns}/representees/{representee}/delegates/{delegate}/mandates/{mandateIdentifier}/subdelegates",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> addMandateSubDelegate(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Pattern(regexp="^[A-Z]{2}.+") @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the representee. Starts with country code (ISO 3166-1 alpha-2).", required=true, schema=@Schema()) @PathVariable("representee") String representee, @Pattern(regexp="^[A-Z]{2}.+") @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the delegate. Starts with country code (ISO 3166-1 alpha-2).", required=true, schema=@Schema()) @PathVariable("delegate") String delegate, @Parameter(in = ParameterIn.PATH, description = "Mandate to sub-delegate", required=true, schema=@Schema()) @PathVariable("mandateIdentifier") String mandateIdentifier, @Parameter(in = ParameterIn.DEFAULT, description = "Enter details of the subdelegate mandate", schema=@Schema()) @Valid @RequestBody MandateToSubDelegate body);


    @Operation(summary = "Add a single mandate to a delegate under a representee", description = " Kui liidestuv süsteem pakub seda teenust Pääsukesele, siis lubab Pääsuke oma kasutajaliidese kaudu volitusi anda ja selle teenuse abil Pääsuke edastab (ühekaupa) uue volituse liidestunud süsteemile. ", tags={ "All queries", "Offered to Pääsuke (additional)" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "202", description = "Accepted for processing"),
        
        @ApiResponse(responseCode = "400", description = "Incorrect format of the payload.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))),
        
        @ApiResponse(responseCode = "403", description = "You are not owner of the namespace."),
        
        @ApiResponse(responseCode = "404", description = "Delegate doesn't have anything matching to remove"),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/ns/{ns}/mandates",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> addMandateToDelegate(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Parameter(in = ParameterIn.DEFAULT, description = "Enter details of mandate to add", schema=@Schema()) @Valid @RequestBody AddMandateTriplet body);


    @Operation(summary = "List namespaces with translations", description = "", tags={ "All queries", "internal" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List of namespaces with translations", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NamespaceDefinition.class)))) })
    @RequestMapping(value = "/ns",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<NamespaceDefinition>> getNamespaces(@Parameter(in = ParameterIn.QUERY, description = "Filter by owner registry code" ,schema=@Schema()) @Valid @RequestParam(value = "ownerCode", required = false) String ownerCode);


    @Operation(summary = "Remove all mandates from the delegate. THIS IS NOT USED AS WE ALWAYS WANT THE PAYLOADS TO BE SPECIFICY ABOUT WHAT IS GOING TO BE REMOVED.", description = "The mandateIdentifier is returned by the query that returned the mandates.  ", tags={ "Draft not in use" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Removed sucessfully"),
        
        @ApiResponse(responseCode = "202", description = "Request accepted but not yet removed"),
        
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        
        @ApiResponse(responseCode = "403", description = "You are not owner of the namespace."),
        
        @ApiResponse(responseCode = "404", description = "Delegate doesn't have anything matching to remove"),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/ns/{ns}/representees/{representee}/delegates/{delegate}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeAllMandatesFromDelegate(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Pattern(regexp="^[A-Z]{2}.+") @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the representee. Starts with country code (ISO 3166-1 alpha-2).", required=true, schema=@Schema()) @PathVariable("representee") String representee, @Pattern(regexp="^[A-Z]{2}.+") @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the delegate. Starts with country code (ISO 3166-1 alpha-2).", required=true, schema=@Schema()) @PathVariable("delegate") String delegate, @NotNull @Parameter(in = ParameterIn.QUERY, description = "Namespace of the mandate" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "namespace", required = true) String namespace, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "startDate", required = false) String startDate, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "endDate", required = false) String endDate);


    @Operation(summary = "Remove a specific mandate from the delegate", description = "The mandateIdentifier is returned by the query that returned the mandates.  ", tags={ "All queries", "Offered to Pääsuke" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Removed sucessfully"),
        
        @ApiResponse(responseCode = "202", description = "Request accepted but not yet removed"),
        
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        
        @ApiResponse(responseCode = "403", description = "You are not owner of the namespace."),
        
        @ApiResponse(responseCode = "404", description = "Delegate doesn't have anything matching to remove"),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/ns/{ns}/representees/{representeeIdentifier}/delegates/{delegateIdentifier}/mandates/{mandateIdentifier}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeMandateFromDelegate(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the representee. Implementator can use any form of identifier (like internal person id)", required=true, schema=@Schema()) @PathVariable("representeeIdentifier") String representeeIdentifier, @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the delegate. Implementator can use any form of identifier (like internal person id).", required=true, schema=@Schema()) @PathVariable("delegateIdentifier") String delegateIdentifier, @Parameter(in = ParameterIn.PATH, description = "Role to delete. Implementator can use any form of identifier (like internal mandate id).", required=true, schema=@Schema()) @PathVariable("mandateIdentifier") String mandateIdentifier);


    @Operation(summary = "Set representee dedelgates that are pulled from Äriregister or from Rahvastikuregister.", description = "Äriregistrist/Rahvastikuregistrist muudatusi sünkroniseerivad mikroteenused kutsuvad seda Pääsukese siseselt välja (et edastada juhatuse liikmed, prokuristid jne). Kaasa antakse TO BE olukord. ", tags={ "internal" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Request processed successfully"),
        
        @ApiResponse(responseCode = "400", description = "Incorrect payload.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))),
        
        @ApiResponse(responseCode = "403", description = "You are not owner of the namespace."),
        
        @ApiResponse(responseCode = "404", description = "Delegate doesn't have anything matching to remove"),
        
        @ApiResponse(responseCode = "422", description = "Unprocessable request. For example system doesn't know about the namespace.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Problem.class)))) })
    @RequestMapping(value = "/ns/{ns}/representees/{representee}/delegate/mandates",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PATCH)
    ResponseEntity<Void> setRepresenteeDelegatesDefinedExternally(@Parameter(in = ParameterIn.PATH, description = "Namespace", required=true, schema=@Schema()) @PathVariable("ns") String ns, @Pattern(regexp="^[A-Z]{2}.+") @Parameter(in = ParameterIn.PATH, description = "Person code or company code of the representee. Starts with country code (ISO 3166-1 alpha-2).", required=true, schema=@Schema()) @PathVariable("representee") String representee, @Parameter(in = ParameterIn.DEFAULT, description = "Enter details of mandate to add", schema=@Schema()) @Valid @RequestBody List<AddMandateTriplet> body);

}

