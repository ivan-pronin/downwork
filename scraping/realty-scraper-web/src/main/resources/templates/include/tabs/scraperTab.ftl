<div class="tab-pane active" id="panel-scraper-config">
	<p />
	<p>Here goes scraper config settings</p>
	<p />

	<form class="form-horizontal" action="/resultScraperConfiguration" method="POST">
		<fieldset disabled>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<label for="scrapTarget">Choose a source to scrap:</label>
					<select class="form-control" id="scrapTarget" name="scrapTarget">
						<option name="scrapTarget">idealista.com</option>
					</select>
				</div>
			</div>
		</fieldset>
		<div class="form-group">
			<label for="maxAdsToProcess" class="col-sm-6 control-label">Max ads to process</label>
			<div class="col-sm-6">
				<input type="text" name="maxAdsToProcess" class="form-control" id="maxAdsToProcess" placeholder="maxAdsToProcess">
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="newestAdsCount" class="col-sm-6 control-label">Newest ads count</label>
			<div class="col-sm-6">
				<input type="text" name="newestAdsCount" class="form-control" id="newestAdsCount" placeholder="newestAdsCount" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label>
						<input type="checkbox" name="useProxy" id="useProxy" />
						Use proxy
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<label class="checkbox-inline" for="proxy1">
					<input type="checkbox" name="proxy1" id="proxy1" />
					proxy 1
				</label>
				<label class="checkbox-inline" for="proxy2">
					<input type="checkbox" name="proxy2" id="proxy2" />
					proxy 2
				</label>
			</div>
		</div>
		<div class="form-group">
			<label for="maxProxyResponseTime" class="col-sm-6 control-label">Proxy response time, ms</label>
			<div class="col-sm-6">
				<input type="text" name="maxProxyResponseTime" class="form-control" id="maxProxyResponseTime"
					placeholder="maxProxyResponseTime" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label>
						<input type="checkbox" name="appendXlsTimestamp" id="appendXlsTimestamp" />
						Append timestamp to XLS
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="xlsFileName" class="col-sm-6 control-label">XLS file name</label>
			<div class="col-sm-6">
				<input type="text" name="xlsFileName" class="form-control" id="xlsFileName" placeholder="xlsFileName" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label>
						<input type="checkbox" name="privateAdsFiltering" id="privateAdsFiltering" />
						Private ads filtering
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<label for="maxThreads">
					Threads count
					<select class="form-control" name="maxThreads" id="maxThreads">
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
					</select>
				</label>
			</div>
		</div>

		<div class="form-group">
			<button type="submit" class="btn btn-primary">Save settings</button>
		</div>
	</form>
</div>