package vsg.rest.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class ExportAssetsController {

	private final ExportAssetsService exportAssetsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportAssetsController.class);

	@Autowired
	public ExportAssetsController(ExportAssetsService pExportAssetsService) {
		exportAssetsService = pExportAssetsService;
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


}
