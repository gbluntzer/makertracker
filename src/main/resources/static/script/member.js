    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var memberFirstName, memberLastName, memberEmail;
           memberFirstName = $('#member_firstName').val();
           memberLastName = $('#member_lastName').val();
           memberEmail = $('#member_email').val();
           if($.trim(memberFirstName) === ""){
               alert("Member First name cannot be empty");
           }
           if($.trim(memberLastName) === ""){
               alert("Member Last name cannot be empty");
           }
           else if($.trim(memberEmail) === ""){
               alert("Member email cannot be empty");
           }
           else {
               var data = {};
               data["firstName"] = memberFirstName;
               data["lastName"] = memberLastName;
               data["email"] = memberEmail;
               $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: "/savemember",
                   data: JSON.stringify(data),
                   dataType: 'json',
                   timeout: 6000,
                   success: function (data) {
                       if (confirm("Member with Id " +data + " Saved")){
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
               var Id = parseInt($(this).closest("td").attr("id"));
               $.ajax({
                   type:"POST",
                   url:"/removemember",
                   data:{Id:Id},
                   success:function (data) {
                       $(".delete-order").closest("td#"+data).parent("tr").fadeOut("slow",function(){
                           $(".delete-order").closest("td#"+data).parent("tr").remove();
                       });
                       window.location.reload();
                   }
               });
           }
       });
    });