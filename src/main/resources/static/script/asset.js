$(document).ready(function () {


	$('#btn_submit').on("click",function (e) {
		e.preventDefault();
		var csrf, tenbitId, assetId, status, title, description, dateAcquired, dateRemoved, brand, modelNumber, serialNumber, retailValue, webLink, operator, donor;
		tenbitId = $('#tenbitId').val();
		assetId = $('#assetId').val();
		status = $('#status').val();
		title = $('#title').val();
		description = $('#description').val();
		dateAcquired = $('#dateAcquired').val();
		dateRemoved = $('#dateRemoved').val();
		brand = $('#brand').val();
		modelNumber = $('#modelNumber').val();
		serialNumber = $('#serialNumber').val();
		retailValue = $('#retailValue').val();
		webLink = $('#webLink').val();
		operator = $('#operator').val();
		donor = $('#donor').val();
		csrf = $("[name='_csrf']").val();
		if($.trim(title) === ""){
			alert("Asset Title cannot be empty");
		}
//		if($.trim(tenbitId)!= undefined){
//		//TODO tenbitId  needs formatting validated
//		}

		else {
			var data = {};
			if(assetId){
				data["id"] = assetId;
			}
			data["tenbitId"] = tenbitId;
			data["title"] = title;
			data["description"] = description;
			data["status"] = status;
			var adate = new Date(dateAcquired);
			data["dateAcquired"] = new Date(adate.valueOf() + adate.getTimezoneOffset() * 60000);
			var rdate = new Date(dateRemoved);
			data["dateRemoved"] = new Date(rdate.valueOf() + rdate.getTimezoneOffset() * 60000);
			data["brand"] = brand;
			data["modelNumber"] = modelNumber;
			data["serialNumber"] = serialNumber;
			data["retailValue"] = retailValue;
			data["webLink"] = webLink;
			data["operator"] = operator;
			data["donor"] = donor;
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type: "POST",
				contentType: "application/json",
				url: "/assets",
				data: JSON.stringify(data),
				dataType: 'json',
				timeout: 6000,
				success: function (data) {
					if (confirm("Asset with Id " +data + " Saved")){
						window.location.reload();
					}
				}
			});
		}
	});

	//Complete order button handler
	$('.delete-asset').on("click", function(e){
		e.preventDefault();
		if(confirm("Delete?")){
			var Id = parseInt($(this).closest("td").attr("id"));
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/assets/" + Id,
				success:function (data) {
					$(".delete-order").closest("td#"+data).parent("tr").fadeOut("slow",function(){
						$(".delete-order").closest("td#"+data).parent("tr").remove();
					});
					window.location.reload();
				}
			});
		}
	});

	$('.edit-asset').on("click", function(e){
		e.preventDefault();

		var Id = parseInt($(this).closest("td").attr("id"));
		$.ajax({
			type:"GET",
			headers: { 'accept': 'application/json'},
			url:"/assets/" + Id,
			success:function (data) {
				$('#assetId').val(data.id);
				$('#tenbitId').val(data.tenbitId);
				$('#title').val(data.title);
				$('#status').val(data.status);
				$('#description').val(data.description);
				$('#dateAcquired').val(data.dateAcquired);
				$('#dateRemoved').val(data.dateRemoved);
				$('#brand').val(data.brand);
				$('#modelNumber').val(data.modelNumber);
				$('#serialNumber').val(data.serialNumber);
				$('#retailValue').val(data.retailValue);
				$('#webLink').val(data.webLink);
				$('#operator').val(data.operator);
				$('#donor').val(data.donor);

				window.history.pushState('Edit Asset ' + data.id, 'MakerTracker', '/assets/' + data.id);
			}
		});
	});

	$('.new-asset').on("click", function(e){
		e.preventDefault();

		$('#assetId').val('');
		$('#tenbitId').val('');
		$('#title').val('');
		$('#status').val('');
		$('#description').val('');
		$('#dateAcquired').val('');
		$('#dateRemoved').val('');
		$('#brand').val('');
		$('#modelNumber').val('');
		$('#serialNumber').val('');
		$('#retailValue').val('');
		$('#webLink').val('');
		$('#operator').val('');
		$('#donor').val('');

		window.history.pushState('Edit Assets', 'MakerTracker', '/assets');
	});
});