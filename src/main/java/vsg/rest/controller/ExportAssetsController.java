package vsg.rest.controller;

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
