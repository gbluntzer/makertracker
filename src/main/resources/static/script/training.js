    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var trainingTitle, trainingDescription, assetId;
           trainingTitle = $('#training_title').val();
           trainingDescription = $('#training_description').val();
           assetId = $('#asset_id').val();
           if($.trim(trainingTitle) === ""){
               alert("Training Title cannot be empty");
           }

           else {
               var data = {};
               data["title"] = trainingTitle;
               data["description"] = trainingDescription;
               data["assetId"] = assetId;
               $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: "/savetraining",
                   data: JSON.stringify(data),
                   dataType: 'json',
                   timeout: 6000,
                   success: function (data) {
                       if (confirm("Training with Id " +data + " Saved")){
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
                   url:"/removetraining",
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