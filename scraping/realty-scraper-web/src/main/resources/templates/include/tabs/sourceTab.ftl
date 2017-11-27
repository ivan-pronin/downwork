<div class="tab-pane" id="panel-source-config">
	<p />
	<p>Here goes source settings.</p>
	<p />
	<form class="form-horizontal" action="/resultSourceConfiguration" method="POST">
		<div class="form-group">
			<label for="operation" class="col-sm-6 control-label">Operation</label>
			<div class="col-sm-6">
				<select class="form-control" name="operation" id="operation">
					<option>Buy</option>
					<option>Rent</option>
					<option>Share</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label for="typology" class="col-sm-6 control-label">Typology</label>
			<div class="col-sm-6">
				<input type="text" name="typology" class="form-control" id="typology" placeholder="typology" />
			</div>
		</div>
		<div class="form-group">
			<label for="location" class="col-sm-6 control-label">Location</label>
			<div class="col-sm-6">
				<input type="text" name="location" class="form-control" id="location" placeholder="location" />
			</div>
		</div>
		<div class="form-group">
			<label for="province" class="col-sm-6 control-label">Province</label>
			<div class="col-sm-6">
				<input type="text" name="province" class="form-control" id="province" placeholder="province" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<label>
					Publication date filter
					<select class="form-control" name="publicationDateFilter" id="publicationDateFilter">
						<option>none</option>
						<option>Last week</option>
						<option>Last month</option>
						<option>48 hours</option>
					</select>
				</label>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<label>
					Language
					<select class="form-control" name="language">
						<option>Spanish</option>
						<option>English</option>
					</select>
				</label>
			</div>
		</div>
		<div class="form-group">
			<button type="submit" class="btn btn-primary">Save settings</button>
		</div>
	</form>
</div>