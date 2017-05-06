package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.OffsetOutOfRangeException;
import models.accommodation.Accommodation;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.accommodation.AccommodationService;
import utils.DynamicFilter;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
public class AccommodationController extends Controller{

    private AccommodationService accommodationService;

    @Inject
	public AccommodationController(AccommodationService accommodationService) {
		this.accommodationService = accommodationService;
	}

	public Result get(final Option<Integer> count, final Option<Integer> offset,
					  final Option<Double> rent, final Option<Double> size,
					  final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed){

        try {

            List<Accommodation> accommodation = accommodationService.getSubset(count, offset, rent, size, smokingAllowed, animalsAllowed);
            return ResponseBuilder.buildOKList(accommodation);

        } catch (OffsetOutOfRangeException e) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }
	}
	
	public Result put(){
		JsonNode body;
		try {
			body = request().body().asJson();
		} catch (NullPointerException e) {
			Result result = ResponseBuilder.buildBadRequest("Request body required", ResponseBuilder.MALFORMED_REQUEST_BODY);
			return result;
		}
		
		
		
		
		
		
		return ok("description is :"+body.get("description").asText());
	}
}
