package vsg.rest.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vsg.rest.service.ExportAssetsService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Denis Orlov.
 */
@Controller
public class ApplicationController {

	private final ExportAssetsService exportAssetsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportAssetsController.class);

	@Autowired
	public ApplicationController(ExportAssetsService pExportAssetsService) {
		exportAssetsService = pExportAssetsService;
	}

	@RequestMapping("/")
	public ModelAndView showWelcomePage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		return view;
	}

	@RequestMapping(value = "/export_asset")
	public void exportAssets(HttpServletResponse response) {
		String productExported = exportAssetsService.getExportedFile();
		if (!StringUtils.isEmpty(productExported)) {
			InputStream is = new ByteArrayInputStream(productExported.getBytes(StandardCharsets.UTF_8));
			try {
				response.setContentType("application/csv");
				response.setHeader("Content-Disposition", "attachment; filename=\"ExportProducts.csv\"");
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
			} catch (IOException pE) {
				LOGGER.error("Cannot write file to outputStream. Exception: ", pE);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	@RequestMapping(value = "/export_zip", produces = "application/zip")
	public void getExportToZop(HttpServletResponse response) {
		InputStream is = exportAssetsService.getAlternativeExport();
		try {
			//response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"testExport.zip\"");
			response.setStatus(HttpServletResponse.SC_OK);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException pE) {
			LOGGER.error("Cannot write file to outputStream. Exception: ", pE);
		}
	}
}

