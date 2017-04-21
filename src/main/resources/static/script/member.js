    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var memberId, memberFirstName, memberLastName, memberEmail, phoneNumber, description, zipCode, csrf;
           memberId = $('#member_id').val();
           memberFirstName = $('#member_firstName').val();
           memberLastName = $('#member_lastName').val();
           memberEmail = $('#member_email').val();
           phoneNumber = $('#phoneNumber').val();
           description = $('#description').val();
           zipCode = $('#zipCode').val();
           csrf = $("[name='_csrf']").val();
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
               if(memberId){
                data["id"] = memberId;
               }
               data["firstName"] = memberFirstName;
               data["lastName"] = memberLastName;
               data["email"] = memberEmail;
               data["phoneNumber"] = phoneNumber;
               data["description"] = description;
               data["zipCode"] = zipCode;

               $.ajax({
                   headers: { 'X-CSRF-TOKEN': csrf},
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

       $('.edit-member').on("click", function(e){
           e.preventDefault();

               var Id = parseInt($(this).closest("td").attr("id"));
               $.ajax({
                   type:"GET",
                   url:"/getmember",
                   data:{Id:Id},
                   success:function (data) {
                       $('#member_id').val(data.id);
                       $('#member_firstName').val(data.firstName);
                       $('#member_lastName').val(data.lastName);
                       $('#member_email').val(data.email);
                       $('#phoneNumber').val(data.phoneNumber);
                       $('#description').val(data.description);
                       $('#zipCode').val(data.zipCode);

                   }
               });

       });
    });