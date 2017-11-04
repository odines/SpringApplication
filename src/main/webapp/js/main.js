function getAuthToken() {
	$("#error").hide();
	$.ajax({
		type: "GET",
		dataType: "json",
		url: "/get_token",
		success: function (result) {
			processAuthTokenResult(result);
		}
	});
}

function reGetAuthToken() {
	$("#error").hide();
	$.ajax({
		type: "GET",
		dataType: "json",
		url: "/auth_token_reload",
		success: function (result) {
			processAuthTokenResult(result);
		}
	});
}

function getCollections() {
	$.ajax({
		type: "GET",
		dataType: "json",
		url: "/get_collections",
		success: function (result) {
			alert("Success!")
		}

	});
}

function processAuthTokenResult(result) {
	if (true == result) {
		$("#auth_panel").removeClass("panel-default").addClass("panel-success")
	} else {
		$("#auth_panel").removeClass("panel-default").addClass("panel-error")
	}
}

function submitUploadCatalog() {
	$("#uploadCatalogError").hide();
	$("#uploadCatalogSuccess").hide();
	var formData = new FormData($('uploadCatalogForm')[0]);
	$.ajax({
		type: "POST",
		data: formData,
		dataType: 'json',
		processData: false,
		contentType: false,
		url: "/uploadFile",
		success: function (result) {
			$("#uploadCatalogSuccess").val(result.fileUploadResult);
			$("#uploadCatalogSuccess").show();
		},
		error: function (result) {
			$("#uploadCatalogError").val(result.fileUploadResult);
			$("#uploadCatalogError").show();
		}
	});
}


