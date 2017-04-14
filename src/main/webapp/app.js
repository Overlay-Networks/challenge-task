var KEY_AUTH = 'overlay_networks_group1__auth';

new Vue({
	el: '#app',
	data: {
		isLoading: true,
		auth: {
			authenticated: false,
			username: '',
			contacts: []
		},
		login: {
			username: ''
		},
		contact: {
			contactInput: '',
			contactInputDisabled: false
		},
		chat: {
			messageInput: '',
			messageInputDisabled: false,
			selectedContact: '',
			messages: { }
		},
	},
	computed: {
		currentChatMessages: function() {
			return this.chat.messages[this.chat.selectedContact];
		}
	},
	methods: {
		submitLogin: function() {
			var App = this;
			
			return App.$http.post('/rest/login', { name: App.login.username }).then(function() {
				App.auth = {
					authenticated: true,
					username: App.login.username,
					contacts: [],
				};
				setAuthToStorage(App.auth);
				App.login.username = '';
			}, function() {
				alert("wrong password");
				App.login.username = '';
			});
		},
		autoLogin: function(storageObject) {
			var App = this;
			
			return App.$http.post('/rest/login', { name: storageObject.username }).then(function() {
				App.auth = {
					authenticated: true,
					username: storageObject.username,
					contacts: []
				};
				setAuthToStorage(App.auth);
				
				var contactSet = [];
				for(var i=0; i < storageObject.contacts.length; i++) {
					contactSet.push({
						name: storageObject.contacts[i]
					});
				}
				
				return App.$http.post('/rest/new-contact-list', contactSet).then(function() {
					App.auth.contacts = storageObject.contacts;
					for(var j=0; j < App.auth.contacts.length; j++) {
						App.chat.messages[App.auth.contacts[j]] = [];
					}
					
					setAuthToStorage(App.auth);
				}, function() {
					App.auth.contacts = [];
					setAuthToStorage(App.auth);
				});
				
			}, function() {
				removeAuthFromStorage();
			});
		},
		addContact: function() {
			var App = this;
			
			// user is already in list
			if(Object.keys(App.chat.messages).indexOf(App.contact.contactInput) !== -1) {
				return;
			}
			
			App.contact.contactInputDisabled = true;
			App.$http.post('/rest/new-contact', { name: App.contact.contactInput }).then(function() {
				
				App.auth.contacts.push(App.contact.contactInput);
				setAuthToStorage(App.auth);
				
				App.chat.messages[App.contact.contactInput] = [];
				App.contact.contactInput = '';
				App.contact.contactInputDisabled = false;
				
				// set focus back to input field
				window.setTimeout(function() {
					App.$refs.addContactInput.focus();
				});
			}, function() {
				App.contact.contactInputDisabled = false;
			});
			
		},
		submitMessage: function() {
			var App = this;
			var selectedContact = App.chat.selectedContact;
			var messageInput =  App.chat.messageInput
			
			App.chat.messageInputDisabled = true;
			App.$http.post('/rest/send-message', { message: messageInput, receiver: { name: selectedContact }}).then(function(response) {
				var messageId = response.body.messageId;
				
				App.chat.messages[selectedContact].push({
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
			window.location.reload();
		}
	},
	mounted: function() {
		var App = this;
		
		var storageObject = getAuthFromStorage();
		if(storageObject && storageObject.authenticated && storageObject.username && storageObject.contacts) {
			App.autoLogin(storageObject).then(function() {
				App.isLoading = false;
			}, function () {
				removeAuthFromStorage();
				App.isLoading = false;
			});
		} else {
			removeAuthFromStorage();
			App.isLoading = false;
		}
	}
});

function getAuthFromStorage() {
	var storageObject = window.localStorage.getItem(KEY_AUTH);
	if (storageObject) {
		var result = null;
		try { result = JSON.parse(storageObject); } catch (error) { result = null; }
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