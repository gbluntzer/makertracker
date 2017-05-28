$(document).ready(function () {
	var newUser = false;
//   $('#btn_submit').on("click",function (e) {
//       e.preventDefault();
//       var username, password, enabled, csrf;
//       username = $('#username').val();
//       csrf = $("[name='_csrf']").val();
//       if($.trim(username) === ""){
//           alert("Username cannot be empty");
//       } else {
//           var data = {};
//           data["username"] = username;
//           data["password"] = password;
//           data["enabled"] = enabled;
//           
//           $.ajax({
//               headers: { 'X-CSRF-TOKEN': csrf},
//               type: "POST",
//               contentType: "application/json",
//               url: "/users",
//               data: JSON.stringify(data),
//               dataType: 'json',
//               timeout: 6000,
//               success: function (data) {
//                   if (confirm("User Saved")) {
//                       window.location.reload();
//                   }
//               }
//           });
//       }
//   });

   $('.delete-user').on("click", function(e){
       e.preventDefault();
       if(confirm("Delete?")){
           var username = $(this).closest("td").attr("id");
           var csrf = $("[name='_csrf']").val();
           $.ajax({
                headers: { 'X-CSRF-TOKEN': csrf},
               type:"DELETE",
               url:"/users/" + username,
               success:function (data) {
                   window.location.reload();
               }
           });
       }
   });

   $('.edit-user').on("click", function(e){
       e.preventDefault();
       newUser = false;
       
       $('#username').val('');
       $('#member').val('');
       $('#enabled').prop('checked', false);
       $('#editUserForm').show();
       
       $('#newUsername').val('');
       $('#memberId').val('');
       $('#newPassword').val('');
       $('#newEnabled').prop('checked', true);
       $('#newUserForm').hide();

       var username = $(this).closest("td").attr("id");
       $.ajax({
           type:"GET",
           headers: { 'accept': 'application/json'},
           url:"/users/" + username,
           success:function (data) {
               $('#username').val(data.username);
               $('#member').val(data.memberName);
               $('#enabled').prop('checked', data.enabled);
               
               window.history.pushState('Edit User ' + data.username, 'MakerTracker', '/users/' + data.username);
           }
       });
   });
   
   $('.new-user').on("click", function(e){
       e.preventDefault();
       newUser = true;

       $('#username').val('');
       $('#member').val('');
       $('#enabled').prop('checked', false);
       $('#editUserForm').hide();
       
       $('#newUsername').val('');
       $('#memberId').val('');
       $('#newPassword').val('');
       $('#newEnabled').prop('checked', true);
       $('#newUserForm').show();
       
       window.history.pushState('Edit Users', 'MakerTracker', '/users');
   });
   
   $('#btn_submit').on("click",function (e) {
		if (newUser) {
			e.preventDefault();
			var username, password, enabled, member, roles, csrf;
			username = $('#newUsername').val();
			password = $('#newPassword').val();
			enabled = $('#newEnabled').prop("checked");
			member = $('#memberId').val();
			roles = new Array();
			roles.push("ROLE_USER");

			if ($('#isAdmin').prop("checked")) {
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
					}
				});
			}
		}
	});
});