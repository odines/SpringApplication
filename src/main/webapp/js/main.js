function getAuthToken() {
    $("#error").hide();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "/get_token",
        success: function(result){
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
        success: function(result){
            processAuthTokenResult(result);
        }
    });
}

function processAuthTokenResult(result) {
    if(true == result) {
        $("#auth_missed").hide();
        $("#auth_got").show();
    } else {
        $("#error").show();
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
        success: function(result){
            $("#uploadCatalogSuccess").val(result.fileUploadResult);
            $("#uploadCatalogSuccess").show();
        },
        error: function (result) {
            $("#uploadCatalogError").val(result.fileUploadResult);
            $("#uploadCatalogError").show();
        }
    });
}


