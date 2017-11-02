<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="../css/bootstrap.min.css">
    </head>
    <body>
        <div class="container" style="padding: 40px">
            <div class="alert alert-success" role="alert" id="auth_got" style="display: none">
                Auth token is retrieved. You can call another actions.
            </div>
            <div class="alert alert-danger" role="alert" id="error" style="display: none">
                An error occured. Unable to process request.
            </div>

            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Get auth_token</h4>
                    <p class="card-text">Before any actions you must to obtain auth_token.</p>
                    <a href="#" onclick="getAuthToken()" class="card-link">Get auth_token</a>
                </div>
            </div>

<%--            <div class="card" style="margin-top: 10px">
                <div class="card-block">
                    <h4 class="card-title">Export assets</h4>
                    <a href="/export/Product" class="card-link">Export products</a>
                </div>
            </div>

            <div class="alert alert-success" role="alert" id="uploadCatalogSuccess" style="display: none"></div>
            <div class="alert alert-danger" role="alert" id="uploadCatalogError" style="display: none"></div>
            <div class="card" style="margin-top: 10px">
                <div class="card-block">
                    <h4 class="card-title">Upload Catalog</h4>
                    <form action="/uploadFile" method="post" enctype="multipart/form-data" id="uploadCatalogForm" name="uploadCatalogForm">
                        <input type="file" name="file" required/>
                        <input type="submit" value="Upload"/>
                    </form>
                </div>
            </div>--%>
        </div>
        <script src="../js/jquery-3.2.1.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <script src="../js/main.js"></script>
    </body>
</html>