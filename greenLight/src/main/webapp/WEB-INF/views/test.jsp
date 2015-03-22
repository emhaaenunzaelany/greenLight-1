<html>
<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>

<head>
	<title>Test Guest</title>
</head>
<body>
<h1>
	Test Guest
</h1>

<br/>
<input id="createGuestIdo" type="submit" value="Create Guest Ido"/>
<br/>
<input id="createGuestGuy" type="submit" value="Create Guest Guy"/>
<br/>
<input id="createGuestLiron" type="submit" value="Create Guest Liron"/>
<br/>
<input id="sendNotification" type="submit" value="Send Notification"/>
<br/>
<input id="searchByPhone" type="submit" value="Search By phone"/>
<br/>


<img id="img" src="/resources/0.jpg" />
<img id="img2" src="/resources/1.jpg" />
<img id="img3" src="/resources/2.jpg" />
<img id="imgLiron" src="/resources/liron.jpg" />
<img id="imgLiron2" src="/resources/liron2.jpg" />
<img id="imgLiron3" src="/resources/liron3.jpg" />
<img id="imgGuy" src="/resources/guy.jpg" />
<img id="imgGuy2" src="/resources/guy2.jpg" />
<img id="imgGuy3" src="/resources/guy3.jpg" />



 <br/>
      <script type="text/javascript">

         $(document).ready(function () {

             $("#createGuestIdo").click(function() {

                  var imgElem = document.getElementById('img');

                  var imgData = (getBase64Image(imgElem));

                 $.ajax({

                                 type:"POST",
                                 url:"/guests",
                                 contentType:"application/json",
                                 data:JSON.stringify({
                                     firstName:"Ido",
                                     lastName:"Shaked",
                                     email:"Ido@gmail.com",
                                     phoneNumber:"20305090",
                                     base64img:imgData
                                 }),
                                 dataType: "html",
                                 success:function (responseText) {
                                     $("body").html(responseText);
                                 }
                             });


             });

                  $("#createGuestGuy").click(function() {

                           var imgElem = document.getElementById('imgGuy');
                           var imgData = (getBase64Image(imgElem));


                              $.ajax({

                                              type:"POST",
                                              url:"/guests",
                                              contentType:"application/json",
                                              data:JSON.stringify({
                                                  firstName:"Guy",
                                                  lastName:"Kahlon",
                                                  email:"guy@gmail.com",
                                                  phoneNumber:"0509944364",
                                                  base64img:imgData
                                              }),
                                              dataType: "html",
                                              success:function (responseText) {
                                                  $("body").html(responseText);
                                              }
                                          });


                          });

                   $("#createGuestLiron").click(function() {
                     var imgElem = document.getElementById('imgLiron');
                     var imgData = (getBase64Image(imgElem));

                                                                        $.ajax({

                                                                                          type:"POST",
                                                                                          url:"/guests",
                                                                                          contentType:"application/json",
                                                                                          data:JSON.stringify({
                                                                                              firstName:"Liron",
                                                                                              lastName:"Netzer",
                                                                                              email:"liron@gmail.com",
                                                                                              phoneNumber:"05457918186",
                                                                                              base64img:imgData
                                                                                          }),
                                                                                          dataType: "html",
                                                                                          success:function (responseText) {
                                                                                              $("body").html(responseText);
                                                                                          }
                                                                                      });


                                                                      });


                 $("#sendNotification").click(function() {

                              $.ajax({

                                              type:"POST",
                                              url:"/notifications?hostId=6&guestId=16",
                                              contentType:"application/json",
                                              dataType: "html",
                                              success:function (responseText) {
                                                  $("body").html(responseText);
                                              }
                                          });


                          });



                      $("#searchByPhone").click(function() {

                                                                                     $.ajax({

                                                                                                    type:"GET",
                                                                                                    url:"/guests/searchByPhone?phoneNumber=98765",
                                                                                                    contentType:"application/json",
                                                                                                    dataType: "html",
                                                                                                    success:function (responseText) {
                                                                                                      $("body").html(responseText);
                                                                                                    }
                                                                                      });


                                                                        });




                  function getBase64Image(imgElem) {

                  // imgElem must be on the same server otherwise a cross-origin error will be thrown "SECURITY_ERR: DOM Exception 18"
                      var canvas = document.createElement("canvas");
                      canvas.width = imgElem.clientWidth;
                      canvas.height = imgElem.clientHeight;
                      var ctx = canvas.getContext("2d");
                      ctx.drawImage(imgElem, 0, 0);
                      var dataURL = canvas.toDataURL("image/png");
                      return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
                  }


         });

     </script>
</body>
</html>