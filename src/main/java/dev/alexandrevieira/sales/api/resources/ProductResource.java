package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.ProductRequestDTO;
import dev.alexandrevieira.sales.api.dtos.ProductResponseDTO;
import dev.alexandrevieira.sales.api.exception.ApiError;
import dev.alexandrevieira.sales.domain.entities.Product;
import dev.alexandrevieira.sales.services.ProductService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

//Controller to the '/products' resource
@RestController
@RequestMapping(path = "products", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProductResource {
    @Autowired
    private ProductService service;

    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    @ApiOperation("Search for a product information by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public ProductResponseDTO find(
            @PathVariable @ApiParam(value = "Product id to search for", required = true) Long id) {

        log.info(this.getClass().getSimpleName() + ".find(Long id)");
        return service.find(id).toDTO();
    }

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("Filter products by fields")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public List<ProductResponseDTO> filter(ProductRequestDTO filter) {
        log.info(this.getClass().getSimpleName() + ".filter(ProductRequestDTO filter)");
        Product product = filter.toEntity();
        List<Product> list = service.filter(product);

        return list.stream().map(x -> x.toDTO()).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create a product")
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Created",
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "location",
                                    description = "The URI to the created product",
                                    response = URI.class
                            )
                    }
            ),
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public ResponseEntity<ProductResponseDTO> save(
            @RequestBody @Valid @ApiParam(value = "Product information", name = "product", required = true)
                    ProductRequestDTO dto) {

        log.info(this.getClass().getSimpleName() + ".save(ProductRequestDTO dto)");
        Product entity = dto.toEntity();
        entity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(entity.toDTO());
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Delete a product by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void delete(@PathVariable @ApiParam(value = "Product id to delete", required = true) Long id) {
        log.info(this.getClass().getSimpleName() + ".delete(Long id)");
        service.delete(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Update a product by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void update(@PathVariable @ApiParam(value = "Product id to update", required = true) Long id,
                       @RequestBody @ApiParam(value = "Product new information", required = true)
                               ProductRequestDTO product) {

        log.info(this.getClass().getSimpleName() + ".update(Long id, ProductRequestDTO product)");
        Product entity = product.toEntity();
        service.update(id, entity);
    }
}
