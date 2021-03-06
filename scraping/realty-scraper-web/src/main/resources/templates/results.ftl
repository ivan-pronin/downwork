<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="en">
<#include "/include/common/head.ftl"/>
<body>
	<div class="container-fluid">
		<#include "/include/common/header.ftl"/>
		<div class="row">
			<#include "/include/common/logo.ftl"/>
			<div class="col-md-4">
				<#include "/include/common/welcome.ftl"/>
			</div>
			<div class="col-md-4">
				<#include "/include/common/login.ftl"/>
			</div>
		</div>
		<div class="row" style="padding: 10px">
			<div class="col-md-12">
				<div class="row">
					<#include "/include/common/menu.ftl"/>
				</div>
			</div>
		</div>
		<div class="row" style="padding: 10px">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-12"></div>
				</div>
				<div class="row">
					<div class="col-md-8">
						<#include "/include/results/filesTable.ftl"/>
					</div>
					<div class="col-md-4"></div>
				</div>
			</div>
		</div>
		<!-- CONFIG TABS row -->
	</div>
	<#include "/include/common/scripts.ftl"/>
</body>
</html>
