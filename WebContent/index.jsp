<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Scan</title>
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css" />
</head>
<body>



	<div class="row" style="margin-top: 50px;">

		<div class="col-md-4"></div>

		<div class="col-md-4">

			<form action="/inspect" method="POST">

				<div class="form-group">
					<label for="topic-name">Msg:</label> <input type="text"
						id="message" class="form-control" name="message">
				</div>

				
				<button type="submit" class="btn btn-success">Inspect</button>

			</form>

		</div>


		<div class="col-md-4"></div>


	</div>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/js/bootstrap.min.js"></script>
	<script src="parts/js/header.js"></script>
</body>
</html>