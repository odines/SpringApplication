<!DOCTYPE html>
<html lang="en">
<head>
	<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container" style="padding: 40px">
	<div class="alert alert-success" role="alert" id="auth_got" style="display: none">
		Auth token is retrieved. You can call another actions.
	</div>
	<div class="alert alert-danger" role="alert" id="error" style="display: none">
		An error occured. Unable to process request.
	</div>

	<p><h2 align="center">Export Assets Demo</h2></p>

	<div class="panel-group">
		<div class="panel panel-default" id="auth_panel">
			<div class="panel-heading">Get auth_token</div>
			<div class="panel-body">
				<p class="card-text">Before any actions you must to obtain auth_token.</p>
				<a href="#" onclick="getAuthToken()" class="card-link">Get auth_token</a>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">Export assets</div>
			<div class="panel-body">
				<a href="/export_asset" class="card-link">Export products</a>
			</div>
		</div>
		<div class="panel panel-default" id="exportProducts">
			<div class="panel-heading">Get 20 products from the first 5 categories</div>
			<div class="panel-body">
				<a href="#" onclick="getProducts()" class="nav-link">RUN</a>
			</div>
		</div>
		<div class="panel panel-default" id="exportZip">
			<div class="panel-heading">Export items to ZIP archive</div>
			<div class="panel-body">
				<a href="/export_zip"  class="nav-link">RUN</a>
			</div>
		</div>
	</div>
</div>
<script src="../js/jquery-3.2.1.min.js"></script>
<script src="https://npmcdn.com/tether@1.2.4/dist/js/tether.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/main.js"></script>
</body>
</html>