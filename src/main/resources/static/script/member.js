$(document).ready(function () {

	$('#btn_submit').on("click",function (e) {
		e.preventDefault();

		var formAr = formArrayToObject($('form').serializeArray());
		var memberId = $('#member_id').val();
		var csrf = $("[name='_csrf']").val();

		if($.trim(formAr['memberName']) === ""){
			alert("Membername cannot be empty");
		} else if($.trim(formAr.email) === ""){
			alert("Member email cannot be empty");
		} else if ($.trim(formAr.status) === "Select One") {
			alert("Select a status");
		} else {
			if(memberId){
				formAr["id"] = memberId;
			}

			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type: "POST",
				contentType: "application/json",
				url: "/members",
				data: JSON.stringify(formAr),
				dataType: 'json',
				timeout: 6000,
				success: function (data) {
					if (confirm("Member Saved")) {
						window.location.reload();
					}
				}
			});
		}
	});

	//Complete order button handler
	$('.delete-member').on("click", function(e){
		e.preventDefault();
		if(confirm("Delete?")){
			var Id = $(this).closest("td").attr("id");
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/members/" + Id,
				success:function (data) {
					window.location.reload();
				}
			});
		}
	});

   $('.edit-member').on("click", function(e){
       e.preventDefault();

       var Id = $(this).closest("td").attr("id");
       $.ajax({
           type:"GET",
           headers: { 'accept': 'application/json'},
           url:"/members/" + Id,
           success:function (data) {
               $('#member_id').val(data.id);
               $('#memberName').val(data.memberName);
               $('#status').val(data.status);
               $('#member_email').val(data.email);
               $('#phoneNumber').val(data.phoneNumber);
               $('#paymentMethod').val(data.paymentMethod);
               $('#description').val(data.description);
               $('#zipCode').val(data.zipCode);
               $('#username').val(data.user != null ? data.user.username : '');
               
               window.history.pushState('Edit Member ' + data.memberName, 'MakerTracker', '/members/' + data.id);
           }
       });
   });
   
   $('.new-member').on("click", function(e){
       e.preventDefault();
       resetForm($('#form'));
       window.history.pushState('Edit Member', 'MakerTracker', '/members');
   });
});

function formArrayToObject(formArray) {//serialize data function
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++){
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function resetForm($form) {
    $form.find('input:text, input:password, input:file, select, textarea').val('');
    $form.find('input:radio, input:checkbox')
         .removeAttr('checked').removeAttr('selected');
}