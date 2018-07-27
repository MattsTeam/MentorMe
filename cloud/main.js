Parse.Cloud.define('pingReply', function(request, response) {
  var params = request.params;
  var user = request.user;
  var customData = params.customData;
  var receiver = params.receiver;
  if (!customData) {
    response.error("Missing customData!")
  }


  var query = new Parse.Query(Parse.Installation);
  if (receiver) {
    query.equalTo("currentUserId", receiver);
    console.log(receiver);
  }
  else {
    query.equalTo("deviceType", "android");
  }

  var payload = {};
  if (customData) {
    payload.alert = customData;
  }


  Parse.Push.send({
  where: query,
  // Parse.Push requires a dictionary, not a string.
  data: payload,
  }, {
  success: function() {
     console.log("#### PUSH OK");
  }, error: function(error) {
     console.log("#### PUSH ERROR" + error.message);
  }, useMasterKey: true});

  response.success('success');
});

//Calendar notification
Parse.Cloud.define('eventNotif', function(request, response) {
  var params = request.params;
  var user = request.user;
  var customData = params.customData;
  var receiver = params.receiver;

  if (!customData) {
    response.error("Missing customData!")
  }


  var query = new Parse.Query(Parse.Installation);
  if (receiver) {
    query.equalTo("currentUserId", receiver);
    console.log(receiver);
  }
  else {
    query.equalTo("deviceType", "android");
  }

  var payload = {};
  if (customData) {
    payload.alert = customData;
  }


  Parse.Push.send({
  where: query,
  push_time: "2018-07-26T23:00:00",
  // Parse.Push requires a dictionary, not a string.
  data: payload,
  }, {
  success: function() {
     console.log("#### PUSH OK");
  }, error: function(error) {
     console.log("#### PUSH ERROR" + error.message);
  }, useMasterKey: true});

  response.success('success');
});