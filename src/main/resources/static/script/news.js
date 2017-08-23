$(document).ready(function () {

	$('#new-news').on("click", function(e) {
		e.preventDefault();
		resetForm($('#form'));
		window.history.pushState('MakerTracker', 'MakerTracker', '/news');

		$('#editNewsForm').toggle();
	});

	$('#btn_editNews').on("click", function(e){
		e.preventDefault();

		var formAr = formToObject($('#form'));
		var id = $('#id').val();
		var title = $('#title').val();
		var content = $('#content').val();
		var csrf = $("[name='_csrf']").val();

		if($.trim(formAr.content) === "" ){
			alert("News content cannot be empty");
		} else {
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type: "POST",
				contentType: "application/json",
				url: "/news/" + id,
				data: JSON.stringify(formAr),
				dataType: 'json',
				timeout: 6000,
				success: function (data) {
					if (confirm("News Saved")) {
						window.location.reload();
					}
				}
			});
		}
	});
});