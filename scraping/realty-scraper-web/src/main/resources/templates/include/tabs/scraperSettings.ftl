<div class="tab-pane active" id="panel-scraper-config">
	<p />
	<p>Current scraper settings</p>
	<p />


<form class="form-horizontal">
	<div class="form-group">
		<label for="scrapTarget" class="col-sm-6 control-label">scrapTarget : ${scraperConfiguration.scrapTarget}</label>
	</div>
	<div class="form-group">
		<label for="maxAdsToProcess" class="col-sm-6 control-label">maxAdsToProcess :
			${scraperConfiguration.maxAdsToProcess}</label>
	</div>
	<div class="form-group">
		<label for="newestAdsCount" class="col-sm-6 control-label">newestAdsCount :
			${scraperConfiguration.newestAdsCount}</label>
	</div>
	<div class="form-group">
		<label for="useProxy" class="col-sm-3 control-label">useProxy : ${scraperConfiguration.useProxy?c}</label>
		</br>
		<label for="proxy1" class="col-sm-3 control-label">proxy1 : ${scraperConfiguration.proxy1?c}</label>
		<label for="proxy2" class="col-sm-3 control-label">proxy2 : ${scraperConfiguration.proxy2?c}</label>
	</div>
	<div class="form-group">
		<label for="newestCount" class="col-sm-6 control-label">Max proxy response time:
			${scraperConfiguration.maxProxyResponseTime}</label>
	</div>
	<div class="form-group">
		<label for="appendXlsTimestamp" class="col-sm-6 control-label">Append XLS timestamp:
			${scraperConfiguration.appendXlsTimestamp?c}</label>
	</div>
	<div class="form-group">
		<label for="xlsFileName" class="col-sm-6 control-label">XLS file name: ${scraperConfiguration.xlsFileName}</label>
	</div>
	<div class="form-group">
		<label for="privateAdsFiltering" class="col-sm-6 control-label">Private ads filtering:
			${scraperConfiguration.privateAdsFiltering?c}</label>
	</div>
	<div class="form-group">
		<label for="maxThreads" class="col-sm-6 control-label">Threads count: ${scraperConfiguration.maxThreads}</label>
	</div>

</form>