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

function getProducts() {
	$("#exportProducts").removeClass("panel-success").addClass("panel-default");
	$.ajax({
		type: "GET",
		dataType: "json",
		url: "/get_products",
		success: function (result) {
			if (result) {
				$("#exportProducts").addClass("panel-success");
			}
			console.log(result);
		}

	});
}

function exportToZip() {
	$("#exportZip").removeClass("panel-success").addClass("panel-default");
	$.ajax({
		type: "GET",
		dataType: "json",
		url: "/export_zip",
		success: function (result) {
			if (result) {
				$("#exportZip").addClass("panel-success");
			}
			console.log(result);
		}

	});
}


function processAuthTokenResult(result) {
	if (result) {
		$("#auth_panel").removeClass("panel-default").addClass("panel-success")
	} else {
		$("#auth_panel").removeClass("panel-default").addClass("panel-error")
	}
}


