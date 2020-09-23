document.addEventListener("contextmenu", (event) => event.preventDefault());

function startInstall() {
  $("#already-installed").fadeOut(0);
  $("#reinstall").fadeOut(0);
  $("#launcher-open").fadeOut(0);
  $("#restart").fadeOut(0);
  $("#reinstalling").fadeOut(0);
  $("#intro").fadeOut(500, function () {
    $("#load").fadeIn(500);
    $("#percentage-bar").fadeIn(500);
  });
}

function finished() {
  $("#percentage-bar").fadeOut(500);
  $("#load").fadeOut(500, function () {
    $("#done").fadeIn(500);
    $("#close").delay(1000).fadeIn(500);
  });
}

function reinstall() {
  $("#already-installed").fadeOut(0);
  $("#reinstall").fadeOut(0);
  $("#reinstalling").fadeIn(500);
}

function alreadyInstalled() {
  $("#launcher-open").fadeOut(0);
  $("#restart").fadeOut(0);
  $("#reinstalling").fadeOut(0);
  $("#intro").fadeOut(500, function () {
    $("#already-installed").fadeIn(500);
    $("#reinstall").delay(2000).fadeIn(500);
  });
}

function launcherOpen() {
  $("#reinstalling").fadeOut(0);
  $("#intro").fadeOut(500, function () {
    $("#launcher-open").fadeIn(500);
    $("#restart").delay(2000).fadeIn(500);
  });
}

function errored(message) {
  $("#errored-info").text(message);
  $("#percentage-bar").fadeOut(0);
  $("#load").fadeOut(500, function () {
    $("#errored").fadeIn(500);
    $("#errored-info").fadeIn(500);
    $("#close").delay(2000).fadeIn(500);
  });
}

function setBarPercentage(amount, stage) {
  var bar = document.getElementById("bar");
  if (amount > 100) amount = 100;
  bar.style.width = amount + "%";

  $("#bar-text").text(stage);
}
