var KEY_AUTH = 'overlay_networks_group1__auth';

new Vue({
	el: '#app',
	data: {
		isLoading: true,
		auth: {
			authenticated: false,
			username: '',
			friends: []
		},
		login: {
			username: ''
		},
		chat: {
			messageInput: '',
			messageInputDisabled: false,
			selectedFriend: '',
			messages: {
				'yuri': []
			}
		},
	},
	computed: {
		currentChatMessages: function() {
			return this.chat.messages[this.chat.selectedFriend];
		}
	},
	methods: {
		submitLogin: function() {
			var App = this;
			
			App.$http.post('/rest/login', { name: App.login.username }).then(function() {
				App.auth = {
					authenticated: true,
					username: App.login.username,
					friends: [],
				};
				setAuthToStorage(App.auth);
				App.login.username = '';
			}, function() {
				alert("wrong password");
				App.login.username = '';
			});
		},
		submitMessage: function() {
			var App = this;
			var selectedFriend = App.chat.selectedFriend;
			var messageInput =  App.chat.messageInput
			
			App.chat.messageInputDisabled = true;
			App.$http.post('/rest/send-message', { message: messageInput, receiver: { name: selectedFriend }}).then(function(response) {
				var messageId = response.body.messageId;
				
				App.chat.messages[selectedFriend].push({
					messageId: messageId,
					content: messageInput,
					isOwnMessage: true,
					approved: false
				});
				
				App.chat.messageInput = '';
				App.chat.messageInputDisabled = false;
				
				// set focus back to input field
				window.setTimeout(function() {
					App.$refs.messageInput.focus();
				});
				
			}, function() {
				
				App.messageInputDisabled = false;
			});
		},
		logout: function() {
			removeAuthFromStorage();
			this.auth.authenticated = false;
			this.auth.username = '';
			this.auth.friends = [];
		}
	},
	mounted: function() {
		var App = this;
		var auth = getAuthFromStorage();
		if(auth && auth.authenticated && auth.username) {
			// after login call:
			App.auth.authenticated = true;
			App.auth.username = auth.username;
			App.isLoading = false;
		} else {
			removeAuthFromStorage();
			App.isLoading = false;
		}
	}
});

function getAuthFromStorage() {
	var auth = window.localStorage.getItem(KEY_AUTH);
	if (auth) {
		var result = null;
		try { result = JSON.parse(auth); } catch (error) { result = null; }
		return result;
		
	} else {
		return null;
	}
}
function setAuthToStorage(auth) {
	window.localStorage.setItem(KEY_AUTH, JSON.stringify(auth));
}
function removeAuthFromStorage() {
	window.localStorage.removeItem(KEY_AUTH);
}

/* Sockets: 
 * 
 * 
var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {
	var socket = new SockJS('/websocket-connection');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/greetings', function (greeting) {
			showGreeting(JSON.parse(greeting.body).content);
		});
	});
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
	$("form").on('submit', function (e) {
		e.preventDefault();
	});
	$( "#connect" ).click(function() { connect(); });
	$( "#disconnect" ).click(function() { disconnect(); });
	$( "#send" ).click(function() { sendName(); });
});

*/