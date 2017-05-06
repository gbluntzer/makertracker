    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var memberId, memberName, memberEmail, phoneNumber, paymentMethod, description, zipCode, csrf;
           memberId = $('#member_id').val();
           memberName = $('#memberName').val();
           memberEmail = $('#member_email').val();
           status = $('#status').val();
           phoneNumber = $('#phoneNumber').val();
           description = $('#description').val();
           paymentMethod = $('#paymentMethod').val();
           zipCode = $('#zipCode').val();
           csrf = $("[name='_csrf']").val();
           if($.trim(memberName) === ""){
               alert("Membername cannot be empty");
           } else if($.trim(memberEmail) === ""){
               alert("Member email cannot be empty");
           } else if ($.trim(status) === "Select One") {
        	   alert("Select a status");
           } else {
               var data = {};
               if(memberId){
                data["id"] = memberId;
               }
               data["memberName"] = memberName;
               data["email"] = memberEmail;
               data["status"] = status;
               data["phoneNumber"] = phoneNumber;
               data["paymentMethod"] = paymentMethod;
               data["description"] = description;
               data["zipCode"] = zipCode;

               $.ajax({
                   headers: { 'X-CSRF-TOKEN': csrf},
                   type: "POST",
                   contentType: "application/json",
                   url: "/members",
                   data: JSON.stringify(data),
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
                       
                       window.history.pushState('Edit Member ' + data.memberName, 'MakerTracker', '/members/' + data.id);
                   }
               });

       });
    });