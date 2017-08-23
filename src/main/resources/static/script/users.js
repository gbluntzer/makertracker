$(document).ready(function () {
	var newUser = false;
	var username = null;

	$('#reset-password').on("click", function(e) {
		e.preventDefault();
		
		if (username === null) {
			username = $('#username').text()
		}
		if(confirm("Reset Password for " + username + "?")){
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/users/" + username + "/password",
				dataType: "text",
				success:function (data, status) {
					alert("Password for " + username + " has been reset. A new password has been emailed.");
				},
				error: function(data, status) {
					console.log("Error changing password: " + status + " " + data);
					alert("Error changing password");
				}
			});
		}
	});
	
	$('#change-password').on("click", function(e) {
		e.preventDefault();
		
		if (username === null) {
			username = $('#username').text()
		}
		if(confirm("Change Password for " + username + "?")){
			var csrf = $("[name='_csrf']").val();
			
			var data = {};
			
			if ($('#oldPassword').length) {
				data.oldPassword = $('#oldPassword').val();
				
				if($.trim(data.oldPassword) === ""){
					alert("Current password cannot be empty");
					return;
				}
			}
			
			data.newPassword = $('#newPassword').val();
			if($.trim(data.newPassword) === ""){
				alert("New password cannot be empty");
				return;
			}
			
			if ($.trim(data.newPassword).length < 8) {
				alert("New password must be more than 8 characters");
				return;
			}
			
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"POST",
				contentType: "application/json",
				url:"/users/" + username + "/password",
				data: JSON.stringify(data),
				dataType: "text",
				success:function (data, status) {
					alert("Password for " + username + " has been changed.");
				},
				error: function(data, status) {
					console.log("Error changing password: " + status + " " + data);
					alert("Error changing password");
				}
			});
		}
	});
	
	$('.delete-user').on("click", function(e){
		e.preventDefault();
		if(confirm("Delete?")){
			username = $(this).closest("td").attr("id");
			var csrf = $("[name='_csrf']").val();
			$.ajax({
				headers: { 'X-CSRF-TOKEN': csrf},
				type:"DELETE",
				url:"/users/" + username,
				success:function (data) {
					window.location.replace("/users");
				}
			});
		}
	});

	$('.edit-user').on("click", function(e){
		e.preventDefault();
		newUser = false;

		resetForm($('#form'));
		$('#newUserForm').hide();
		$('#editUserForm').show();

		username = $(this).closest("td").attr("id");
		$.ajax({
			type:"GET",
			headers: { 'accept': 'application/json'},
			url:"/users/" + username,
			success:function (data) {
				$.each(data, function(key, value) {
					$('#' + key).val(data[key]);
				});
				
				$('#username').text(data.username);
				$('#enabled').prop('checked', data.enabled);
				$('#isAdmin').prop('checked', data.roles.includes("ROLE_ADMIN"));

				if (data.memberId != null && data.memberId != "") {
					$('#memberLink').html("<a href='/members/" + data.memberId + "'>" + data.memberName + "</a>");
				} else {
					$('#memberLink').text('');
				}

				window.history.pushState('Edit User ' + data.username, 'MakerTracker', '/users/' + data.username);
			}
		});
	});

	$('.new-user').on("click", function(e){
		e.preventDefault();
		newUser = true;

		resetForm($('#form'));
		$('#editUserForm').hide();
		$('#newUserForm').show();

		window.history.pushState('Edit Users', 'MakerTracker', '/users');
		username = null;
	});

	$('#btn_submit').on("click",function (e) {
		if (newUser) {
			e.preventDefault();
			var username, password, enabled, member, roles, csrf;
			username = $('#newUsername').val();
			password = $('#newUserPassword').val();
			enabled = $('#newEnabled').prop("checked");
			member = $('#memberId').val();
			roles = new Array();
			roles.push("ROLE_USER");

			if ($('#newIsAdmin').prop("checked")) {
				roles.push("ROLE_ADMIN");
			}
			csrf = $("[name='_csrf']").val();

			if($.trim(username) === ""){
				alert("Username cannot be empty");
			} else if ($.trim(password) === "") { 
				alert("Password cannot be empty");
			} else {
				var data = {};
				data["username"] = username;
				data["plainPassword"] = password;
				data["enabled"] = enabled;
				data["memberId"] = member;
				data["roles"] = roles;

				$.ajax({
					headers: { 'X-CSRF-TOKEN': csrf},
					type: "POST",
					contentType: "application/json",
					url: "/users",
					data: JSON.stringify(data),
					dataType: 'json',
					timeout: 6000,
					success:function (data) {
						location.reload();
					},
					error:function (xhr, textStatus, errorThrown) {
						alert("Error adding new user");
					}
				});
			}
			username = null;
		} else {
			e.preventDefault();
			alert("update not available yet");
		}
	});
});