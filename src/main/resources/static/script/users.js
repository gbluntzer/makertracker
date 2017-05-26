$(document).ready(function () {

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

//    //Complete order button handler
//   $('.delete-user').on("click", function(e){
//       e.preventDefault();
//       if(confirm("Delete?")){
//           var username = $(this).closest("td").attr("id");
//           var csrf = $("[name='_csrf']").val();
//           $.ajax({
//                headers: { 'X-CSRF-TOKEN': csrf},
//               type:"DELETE",
//               url:"/users/" + username,
//               success:function (data) {
//                   $(".delete-order").closest("td#"+data).parent("tr").fadeOut("slow",function(){
//                       $(".delete-order").closest("td#"+data).parent("tr").remove();
//                   });
//                   window.location.reload();
//               }
//           });
//       }
//   });

   $('.edit-user').on("click", function(e){
       e.preventDefault();

       var username = $(this).closest("td").attr("id");
       $.ajax({
           type:"GET",
           headers: { 'accept': 'application/json'},
           url:"/users/" + username,
           success:function (data) {
               $('#username').val(data.username);
               $('#enabled').prop('checked', data.enabled);
               
               window.history.pushState('Edit User ' + data.username, 'MakerTracker', '/users/' + data.username);
           }
       });
   });
   
   $('.new-user').on("click", function(e){
       e.preventDefault();

       $('#username').val('');
       $('#password').val('');
       $('#enabled').prop('checked', false);
       
       window.history.pushState('Edit Users', 'MakerTracker', '/users');
   });
});