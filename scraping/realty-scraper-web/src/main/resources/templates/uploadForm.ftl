<html xmlns:th="http://www.thymeleaf.org">
<body>

	<div>
		<form method="POST" enctype="multipart/form-data" action="/">
			<table>
				<tr>
					<td>File to upload:</td>
					<td>
						<input type="file" name="file" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="submit" value="Upload" />
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div>
		<#list files as file> <p><a href="${file}" text="${file}">${file}</a> </#list>
	</div>

</body>
</html>
