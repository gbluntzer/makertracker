$(document).ready(function () {
	
	$('#trainingRequired').change(function() {
		if ($('#trainedMembersAdminForm').length) {
			if ($('#trainingRequired').prop("checked")) {
				$('#trainedMembersAdminForm').show();
			} else {
				$('#trainedMembersAdminForm').hide();
			}
		}
	});
	
	$('#accessControlled').change(function() {
		if ($('#accessControlTimeMSForm').length) {
			if ($('#accessControlled').prop("checked")) {
				$('#accessControlTimeMSForm').show();
			} else {
				$('#accessControlTimeMSForm').hide();
			}
		}
	});
	
	$('#btn_addmember').on("click", function(e) {
		e.preventDefault();

		var memberId = $('#memberId').val();
		
		if (memberId != '' && !$('#member-row-' + memberId).length) {
			var memberName = $("#memberId>option:selected").html()
			addMemberRow(memberId, memberName);
		}
	});
	
	function addMemberRow(memberId, memberName) {
		var newRow = '<tr id="member-row-' + memberId + '" class="member-row">';
		newRow += '<td id="member-id-' + memberId + '" style="display:none">' + memberId + '</td>';
		newRow += '<td id="member-' + memberName + '">' + memberName + '</td>';
		newRow += '<td id="' + memberId + '"><button class="btn btn-danger remove-member">X</button></td>';
		newRow += '</tr>';
		
		$('#memberTable').find('tbody').append(newRow);
	}
	
	$('#trainedMembersAdminForm').on("click", ".remove-member", function(e){
		e.preventDefault();
		
		var id = $(this).closest("td").attr("id");
		$('#member-row-' + id).remove();
	});
	
	$('#btn_submit').on("click",function (e) {
		e.preventDefault();
		var csrf, tenbitId, assetId, status, title, description, dateAcquired, 
				dateRemoved, brand, modelNumber, serialNumber, retailValue, 
				webLink, operator, donor, trainingRequired, trainedMembers, accessControlled,
				accessControlTimeMS;
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
		trainingRequired = $('#trainingRequired').prop('checked');
		
		trainedMembers = new Array();
		$('#memberTable tbody tr').each(function(){
			var id = $(this)[0].id.substring("member-row-".length);
			trainedMembers.push(id);
		});
		
		accessControlled = $('#accessControlled').prop('checked');
		accessControlTimeMS = $('#accessControlTimeMS').val();
		
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
			data["trainingRequired"] = trainingRequired;
			data["members"] = trainedMembers;
			data["accessControlled"] = accessControlled;
			data["accessControlTimeMS"] = accessControlTimeMS;
			
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
				$('#trainingRequired').prop('checked', data.trainingRequired);
				
				if ($('#trainedMembersAdminForm').length) {
					$('#memberTableBody').empty();
					
					if (data.trainingRequired) {
						$('#trainedMembersAdminForm').show();
					} else {
						$('#trainedMembersAdminForm').hide();
					}
					
					var members = data.members;
					if (members != null) {
						for (var i = 0; i < members.length; i++) {
							addMemberRow(members[i].id, members[i].memberName)
						}
					}
				}
				
				$('#accessControlled').prop('checked', data.accessControlled);
				$('#accessControlTimeMS').val(data.accessControlTimeMS);
				
				if (data.accessControlled) {
					$('#accessControlTimeMSForm').show();
				} else {
					$('#accessControlTimeMSForm').hide();
				}
				
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
		$('#trainingRequired').prop('checked', false);
		
		if ($('#trainedMembersAdminForm').length) {
			$('#memberTableBody').empty();
			$('#trainedMembersAdminForm').hide();
		}
		
		$('#accessControlled').prop('checked', false);
		$('#accessControlTimeMS').val('');
		$('#accessControlTimeMSForm').hide();

		window.history.pushState('Edit Assets', 'MakerTracker', '/assets');
	});
});