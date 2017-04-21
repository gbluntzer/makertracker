    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var csrf, completedTrainingId, trainingId, memberId, trainingDate;
           completedTrainingId = $('#completedTrainingId').val();
           trainingId = $('#trainingId').val();
           memberId = $('#memberId').val();
           trainingDate = $('#trainingDate').val();
           csrf = $("[name='_csrf']").val();
           if($.trim(trainingId) === ""){
               alert("Training Id cannot be empty");
           }
            if($.trim(memberId) === ""){
                alert("Member id cannot be empty");
            }

           else {
               var data = {};
               if(completedTrainingId){
                data["id"] = completedTrainingId;
               }
               data["trainingId"] = trainingId;
               data["memberId"] = memberId;
               data["trainingDate"] = trainingDate;
               $.ajax({
                   headers: { 'X-CSRF-TOKEN': csrf},
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

        $('.edit-training').on("click", function(e){
                         e.preventDefault();

                             var Id = parseInt($(this).closest("td").attr("id"));
                             $.ajax({
                                 type:"GET",
                                 url:"/getcompletedtraining",
                                 data:{Id:Id},
                                 success:function (data) {
                                   $('#completedTrainingId').val(data.id);
                                   $('#trainingId').val(data.trainingId);
                                   $('#memberId').val(data.memberId);
                                   $('#trainingDate').val(data.trainingDate);
                                }
                             });

         });
    });