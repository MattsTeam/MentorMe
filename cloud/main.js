Parse.Cloud.define('pingReply', function(request, response) {
  var params = request.params;
  var customData = params.customData;
  var deviceToken = request.params.deviceToken;

  if (!customData) {
    response.error("Missing customData!")
  }

  var sender = JSON.parse(customData).sender;
  var query = new Parse.Query(Parse.Installation);
  query.equalTo("installationId", sender);
  query.equalTo("deviceType", "android");


  Parse.Push.send({
  where: query,
  // Parse.Push requires a dictionary, not a string.
  data: {"alert": "heyheyhey"},
  }, {
  useMasterKey: true,
  success: function() {
     console.log("#### PUSH OK");
  }, error: function(error) {
     console.log("#### PUSH ERROR" + error.message);
  }, useMasterKey: true});

  response.success('success');
});