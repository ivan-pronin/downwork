<form class="form-horizontal">
	<div class="form-group">
		<label for="operation" class="col-sm-6 control-label">
			Operation:
			<#if sourceConfiguration??>${sourceConfiguration.operation}<#else>value not set</#if>
											</label>
	</div>
</form>