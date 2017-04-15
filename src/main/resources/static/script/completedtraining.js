    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var trainingId, memberId, trainingDate;
           trainingId = $('#training_id').val();
           memberId = $('#member_id').val();
           trainingDate = $('#training_date').val();

           if($.trim(trainingId) === ""){
               alert("Training Id cannot be empty");
           }
            if($.trim(memberId) === ""){
                          alert("Member id cannot be empty");
            }

           else {
               var data = {};
               data["trainingId"] = trainingId;
               data["memberId"] = memberId;
               data["trainingDate"] = trainingDate;
               $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: "/savecompletedtraining",
                   data: JSON.stringify(data),
                   dataType: 'json',
                   timeout: 6000,
                   success: function (data) {
                       if (confirm("Completed Training with Id " +data + " Saved")){
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
               $.ajax({
                   type:"POST",
                   url:"/removecompletedtraining",
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

//       $('#training_date').datepicker(
//           {
//               format: 'yyyy-MM-dd',
//               todayHighlight: true,
//               autoclose: true,
//             });
    });