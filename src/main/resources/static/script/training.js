$(document).ready(function () {


	$('#btn_submit').on("click",function (e) {
		e.preventDefault();
		var csrf, assetId, trainingId, title, description, assetId;
		title = $('#title').val();
		description = $('#description').val();
		trainingId = $('#trainingId').val();
		assetId = $('#assetId').val();
		csrf = $("[name='_csrf']").val();
		if($.trim(title) === ""){
			alert("Training Title cannot be empty");
		}

		else {
			var data = {};
			if(trainingId){
				data["id"] = trainingId;
			}
			data["title"] = title;
			data["description"] = description;
			data["assetId"] = assetId;

			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type: "POST",
				contentType: "application/json",
				url: "/trainings",
				data: JSON.stringify(data),
				dataType: 'json',
				timeout: 6000,
				success: function (data) {
					if (confirm("Training with Id " +data + " Saved")){
						window.location.reload();
					}
				}
			});
		}
	});

	//Complete order button handler
	$('.delete-training').on("click", function(e){
		e.preventDefault();
		if(confirm("Delete?")){
			var Id = parseInt($(this).closest("td").attr("id"));
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/trainings/" + Id,
				success:function (data) {
					$(".delete-order").closest("td#"+data).parent("tr").fadeOut("slow",function(){
						$(".delete-order").closest("td#"+data).parent("tr").remove();
					});
					window.location.reload();
				}
			});
		}
	});

	$('.edit-training').on("click", function(e){
		e.preventDefault();

		var Id = parseInt($(this).closest("td").attr("id"));
		$.ajax({
			type:"GET",
			headers: { 'accept': 'application/json'},
			url:"/trainings/" + Id,
			success:function (data) {
				$('#trainingId').val(data.id);
				$('#assetId').val(data.assetId);
				$('#title').val(data.title);
				$('#description').val(data.description);

				window.history.pushState('Edit Training ' + data.id, 'MakerTracker', '/trainings/' + data.id);
			}
		});
	});

	$('.new-training').on("click", function(e){
		e.preventDefault();

		$('#trainingId').val('');
		$('#assetId').val('');
		$('#title').val('');
		$('#description').val('');

		window.history.pushState('Edit Training', 'MakerTracker', '/trainings');
	});
});