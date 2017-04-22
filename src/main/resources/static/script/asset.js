    $(document).ready(function () {


       $('#btn_submit').on("click",function (e) {
           e.preventDefault();
           var csrf, tenbitId, assetId, status, title, description, dateAquired, dateRemoved, brand, modelNumber, serialNumber, retailValue, webLink, operator, donator;
           tenbitId = $('#tenbitId').val();
           assetId = $('#assetId').val();
           status = $('#status').val();
           title = $('#title').val();
           description = $('#description').val();
           dateAquired = $('#dateAquired').val();
           dateRemoved = $('#dateRemoved').val();
           brand = $('#brand').val();
           modelNumber = $('#modelNumber').val();
           serialNumber = $('#serialNumber').val();
           retailValue = $('#retailValue').val();
           webLink = $('#webLink').val();
           operator = $('#operator').val();
           donator = $('#donator').val();
           csrf = $("[name='_csrf']").val();
           if($.trim(title) === ""){
               alert("Asset Title cannot be empty");
           }
           else if($.trim(tenbitId){
                          //TODO tenbitId  needs formatting validated
            }

           else {
               var data = {};
                if(assetId){
                               data["id"] = assetId;
                              }
               data["tenbitId"] = tenbitId;
               data["title"] = title;
               data["description"] = description;
               data["status"] = status;
               var adate = new Date(dateAquired);
               data["dateAquired"] = new Date(adate.valueOf() + adate.getTimezoneOffset() * 60000);
               var rdate = new Date(dateRemoved);
               data["dateRemoved"] = new Date(rdate.valueOf() + rdate.getTimezoneOffset() * 60000);
               data["brand"] = brand;
               data["modelNumber"] = modelNumber;
               data["serialNumber"] = serialNumber;
               data["retailValue"] = retailValue;
               data["webLink"] = webLink;
               data["operator"] = operator;
               data["donator"] = donator;
               $.ajax({
                 headers: { 'X-CSRF-TOKEN': csrf},
                   type: "POST",
                   contentType: "application/json",
                   url: "/saveasset",
                   data: JSON.stringify(data),
                   dataType: 'json',
                   timeout: 6000,
                   success: function (data) {
                       if (confirm("Asset with Id " +data + " Saved")){
                           window.location.reload();
                       }
                   }
               });
           }
       });

        //Complete order button handler
       $('.delete-asset').on("click", function(e){
           e.preventDefault();
           if(confirm("Delete?")){
               var Id = parseInt($(this).closest("td").attr("id"));
               $.ajax({
                   type:"POST",
                   url:"/removeasset",
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

              $('.edit-asset').on("click", function(e){
                  e.preventDefault();

                      var Id = parseInt($(this).closest("td").attr("id"));
                      $.ajax({
                          type:"GET",
                          url:"/getasset",
                          data:{Id:Id},
                          success:function (data) {
                            $('#assetId').val(data.id);
                            $('#tenbitId').val(data.tenbitId);
                            $('#title').val(data.title);
                            $('#status').val(data.status);
                            $('#description').val(data.description);
                            $('#dateAquired').val(data.dateAquired);
                            $('#dateRemoved').val(data.dateRemoved);
                            $('#brand').val(data.brand);
                            $('#modelNumber').val(data.modelNumber);
                            $('#serialNumber').val(data.serialNumber);
                            $('#retailValue').val(data.retailValue);
                            $('#webLink').val(data.webLink);
                            $('#operator').val(data.operator);
                            $('#donator').val(data.donator);

                          }
                      });

              });

    });