<table class="table table-hover">
	<thead>
		<tr>
			<th>#</th>
			<th>File name</th>
		</tr>
	</thead>
	<tbody>
		<#list files as file>
		<tr>
			<td>${file?counter}</td>
			<td>
				<a href="${file}" text="${file}">${file}</a>
			</td>
		</tr>
		</#list>
	</tbody>
</table>
