$(document).ready(function () {
	
	$('#trainingRequired').change(function() {
		if ($('#trainedMembersAdminForm').length) {
			if ($('#trainingRequired').prop("checked")) {
				$('#trainedMembersAdminForm').show();
			} else {
				$('#trainedMembersAdminForm').hide();
			}
		} else if ($('#trainedMembersForm').length) {
			if ($('#trainingRequired').prop("checked")) {
				$('#trainedMembersForm').show();
			} else {
				$('#trainedMembersForm').hide();
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
		
		var formAr = formToObject($('#form'));
		var csrf = $("[name='_csrf']").val();
		
		formAr["id"] = $('#assetId').val();
		formAr["trainingRequired"] = $('#trainingRequired').prop('checked');
		formAr["accessControlled"] = $('#accessControlled').prop('checked');
		
		var trainedMembers = new Array();
		$('#memberTable tbody tr').each(function(){
			trainedMembers.push($(this)[0].id.substring("member-row-".length));
		});
		formAr["members"] = trainedMembers;
		
		var acqDate = new Date($('#dateAcquired').val());
		formAr["dateAcquired"] = new Date(acqDate.valueOf() + acqDate.getTimezoneOffset() * 60000);
		
		var remDate = new Date($('#dateRemoved').val());
		formAr["dateRemoved"] = new Date(remDate.valueOf() + remDate.getTimezoneOffset() * 60000);
		
		if($.trim($('#title')) === ""){
			alert("Asset Title cannot be empty");
		} else {
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type: "POST",
				contentType: "application/json",
				url: "/assets",
				data: JSON.stringify(formAr),
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
			var id = $(this).closest("td").attr("id");
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/assets/" + id,
				success:function (data) {
					window.location.reload();
				}
			});
		}
	});

	$('.edit-asset').on("click", function(e){
		e.preventDefault();

		var id = parseInt($(this).closest("td").attr("id"));
		$.ajax({
			type:"GET",
			headers: { 'accept': 'application/json'},
			url:"/assets/" + id,
			success:function (data) {
				$.each(data, function(key, value) {
					$('#' + key).val(data[key]);
				});
				$('#assetId').val(data.id)
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
				} else if ($('#trainedMembersForm').length) {
					if (data.trainingRequired) {
						$('#trainedMembersForm').show();
						
						var members = data.memberNames;
						if (members != null) {
							for (var i = 0; i < members.length; i++) {
								var newRow = '<tr><td id="member-' + members[i] + '">' + members[i] + '</td></tr>';
								
								$('#memberNameTable').find('tbody').append(newRow);
							}
						}
					} else {
						$('#trainedMembersForm').hide();
					}
				}

				$('#accessControlled').prop('checked', data.accessControlled);
				
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
		resetForm($('#form'));
		
		$('#dateAcquired').val('');
		$('#webLink').val('');
		$('#dateRemoved').val('');
		$('#trainingRequired').prop('checked', false);
		$('#accessControlTimeMS').val('');
		
		if ($('#trainedMembersAdminForm').length) {
			$('#memberTableBody').empty();
			$('#trainedMembersAdminForm').hide();
		}
		$('#accessControlTimeMSForm').hide();
		
		window.history.pushState('Edit Assets', 'MakerTracker', '/assets');
	});
});