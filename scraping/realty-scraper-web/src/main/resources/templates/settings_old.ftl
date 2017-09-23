<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<title>LEWT Capital Scraper</title>
<link href="/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<a href="/">
						<h1>LEWT Capital!</h1>
					</a>
				</div>
			</div>
		</div>
		<!-- HEADER ROW -->
		<div class="row">
			<div class="col-md-4">
				<img alt="LOGO Preview" src="http://lorempixel.com/250/250/" class="img-rounded" />
			</div>
			<div class="col-md-4">
				<h2 class="text-primary text-left">Welcome to the new Scraper GUI!</h2>
				<dl>
					<dt>Some usage instructions</dt>
					<dd>First - configure Scraper</dd>
					<dd>Second - configure Source</dd>
					<dd>Finally - launch and get the results!</dd>
				</dl>
			</div>
			<div class="col-md-4">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email </label>
						<div class="col-sm-10">
							<input type="email" class="form-control" id="inputEmail3" />
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-2 control-label">Password </label>
						<div class="col-sm-10">
							<input type="password" class="form-control" id="inputPassword3" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<div class="checkbox">
								<label>
									<input type="checkbox" />
									Remember me
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-default">Sign in</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- LOGO row -->
		<div class="row" style="padding: 10px">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-12">
						<div class="tabbable" id="configuration-tabs">
							<ul class="nav nav-tabs">
								<li class="active">
									<a href="#panel-scraper-config" data-toggle="tab">Configure scraper</a>
								</li>
								<li>
									<a href="#panel-source-config" data-toggle="tab">Configure source</a>
								</li>
								<li>
									<a href="#panel-download-results" data-toggle="tab">Download results</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12"></div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="tab-content">

							<div class="tab-pane active" id="panel-scraper-config">
								<p />
								<p>Here goes scraper config settings</p>
								<p />

								<form class="form-horizontal">
									<div class="form-group">
										<label for="scrapTarget" class="col-sm-6 control-label">scrapTarget :
											${scraperConfiguration.scrapTarget}</label>
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
										<label for="xlsFileName" class="col-sm-6 control-label">XLS file name:
											${scraperConfiguration.xlsFileName}</label>
									</div>
									<div class="form-group">
										<label for="privateAdsFiltering" class="col-sm-6 control-label">Private ads filtering:
											${scraperConfiguration.privateAdsFiltering?c}</label>
									</div>
									<div class="form-group">
										<label for="maxThreads" class="col-sm-6 control-label">Threads count:
											${scraperConfiguration.maxThreads}</label>
									</div>

								</form>
							</div>

							<div class="tab-pane" id="panel-source-config">
								<p />
								<p>Here goes source settings.</p>
								<p />
								<form class="form-horizontal">
									<div class="form-group">
										<label for="operation" class="col-sm-6 control-label">
											Operation:
											<#if sourceConfiguration??>${sourceConfiguration.operation}<#else>value not set</#if>
											</label>
									</div>
								</form>
							</div>
							<div class="tab-pane" id="panel-download-results">
								<p>Download results</p>
							</div>
						</div>
					</div>
					<div class="col-md-4"></div>
					<div class="col-md-4"></div>
				</div>
			</div>
		</div>
		<!-- CONFIG TABS row -->
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<button type="button" class="btn btn-success btn-lg">Scrape me!</button>
				</div>
			</div>
		</div>
		<!-- LAUNCH row -->
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js">
		
	</script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="/js/bootstrap.min.js"></script>
</body>
</html>
