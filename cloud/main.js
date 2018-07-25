Parse.Cloud.define('pingReply', function(request, response) {
  var params = request.params;
  var user = request.user;
  var customData = params.customData;
  var receiver = params.receiver;

  if (!customData) {
    response.error("Missing customData!")
  }


  var query = new Parse.Query(Parse.Installation);
  // if (receiver) {query.equalTo("currentUserId", receiver...
  query.equalTo("deviceType", "android");

  var payload = {};
  if (customData) {
    payload.customData = customData;

  }


  Parse.Push.send({
  channels: ["channelName"],
  where: query,
  // Parse.Push requires a dictionary, not a string.
  data: payload,
  }, {
  useMasterKey: true,
  success: function() {
     console.log("#### PUSH OK");
  }, error: function(error) {
     console.log("#### PUSH ERROR" + error.message);
  }, useMasterKey: true});

  response.success('success');
});