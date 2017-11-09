package vsg.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vsg.rest.service.ExportAssetsService;

import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

/**
 * Created by Denis Orlov.
 */
@RestController
public class ExportAssetsController {

	private final ExportAssetsService exportAssetsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportAssetsController.class);

	@Autowired
	public ExportAssetsController(ExportAssetsService pExportAssetsService) {
		exportAssetsService = pExportAssetsService;
	}

	@GET
	@RequestMapping(value = "/get_products", produces = MediaType.APPLICATION_JSON)
	public String getListCollections() {
		return exportAssetsService.getProducts().toString();
	}


}
