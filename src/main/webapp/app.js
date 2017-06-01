var KEY_AUTH = 'overlay_networks_group1';

new Vue({
	el: '#app',
	data: {
		isLoading: true,
		websockets: {
			stompClient: null,
			connected: false,
			initialized: false
		},
		auth: {
			authenticated: false,
			username: ''
		},
		chat: {
			contacts: [],
			selectedContact: '',
		},
		input: {
			loginUsername: '',
			loginUsernameDisabled: false,
			addContactInput: '',
			addContactInputDisabled: false,
			messageInput: '',
			messageInputDisabled: false,
			notary: false
		}
	},
	computed: {
		currentChatMessages: function() {
			var App = this;
			var contact = App.chat.contacts.filter(function(item) {
				return item.name === App.chat.selectedContact;
			});
			if(contact.length === 1) {
				return contact[0].messages;
			} else {
				return [];
			}
		},
		currentChatContact: function() {
			var App = this;
			
			var contact = App.chat.contacts.filter(function(item) {
				return item.name === App.chat.selectedContact;
			});
			if(contact.length === 1) {
				return contact[0];
			}
		}
	},
	methods: {
		connectToWebsockets: function () {
			var App = this;
			var socket = new SockJS('/websocket');
			App.websockets.stompClient = Stomp.over(socket);
			App.websockets.stompClient.debug = null;
			App.websockets.stompClient.connect({}, function connect(frame) {
				App.websockets.connected = true;
				App.websockets.initialized = true;

				App.websockets.stompClient.subscribe('/topic/receive-message', function (response) {
					response = JSON.parse(response.body);
					
					var filteredContact = App.chat.contacts.filter(function(item) {
						return item.name === response.sender.name;
					});
					if(filteredContact.length === 1) {
						filteredContact[0].messages.push({
							messageId: response.messageId,
							content: response.message,
							isOwnMessage: false,
							notary: response.notary,
							approved: false
						});
						setToStorage(App);
						
					} else {
						console.log("Could not find a contact to that message.", message);
					}
				});

				App.websockets.stompClient.subscribe('/topic/receive-notary', function (response) {
					response = JSON.parse(response.body);
					var messageFound = false;
					
					for(var i = 0; i < App.chat.contacts.length; i++) {
						var contact = App.chat.contacts[i];
						var messageArray = contact.messages.filter(function (item) {
							return item.messageId === response.messageId;
						});
						
						if(messageArray.length === 1) {
							messageArray[0].approved = true;
							messageFound = true;
							setToStorage(App);
							break;
						}
					}
					
					if(!messageFound) {
						console.log("Could not find a message to approve (notary)", response);
					}
				});
				App.websockets.stompClient.subscribe('/topic/update-contacts', function (response) {
					response = JSON.parse(response.body);
					
					for(var i = 0; i < response.length; i++) {
						var responseItem = response[i];
						
						var contactArray = App.chat.contacts.filter(function (item) {
							return item.name === responseItem.contact.name;
						});
						if(contactArray.length === 1) {
							contactArray[0].online = responseItem.status;
							
						} else {
							console.log("Contact not found in local list", responseItem);
						}
					}
				});
			}, function disconnect() {
				App.websockets.connected = false;
			});
		},
		submitLogin: function() {
			var App = this;
			
			App.input.loginUsernameDisabled = true;
			return App.$http.post('/rest/login', { name: App.input.loginUsername }).then(function() {
				App.auth = {
					authenticated: true,
					username: App.input.loginUsername
				};
				setToStorage(App);
				
				App.input.loginUsername = '';
				App.connectToWebsockets();
				
			}, function() {
				alert("You cannot use this username.");
				App.input.loginUsername = '';
			}).then(function() {
				App.input.loginUsernameDisabled = false;
			});
		},
		autoLogin: function(storageObject) {
			var App = this;
			
			if(storageObject && storageObject.auth && storageObject.chat) {
				return App.$http.post('/rest/login', { name: storageObject.auth.username }).then(function() {
					var contactSet = getSubmittableContactList(storageObject);
					return App.$http.post('/rest/new-contact-list', contactSet).then(function() {
						App.auth = storageObject.auth;
						App.chat = storageObject.chat;
						App.connectToWebsockets();
						
					}, function() {
						App.logout();
					});
				
				}, function() {
					removeFromStorage();
				});
				
			} else {
				removeFromStorage();
				return Promise.resolve();
			}
		},
		addContact: function() {
			var App = this;
			
			var usernameNotEmpty = App.input.addContactInput.trim() !== '';
			var usernameDifferentToOwnUsername = App.input.addContactInput !== App.auth.username;
			var userDoesNotExistAlready = App.chat.contacts.filter(function(item) {
				return item.name === App.input.addContactInput;
			}).length === 0;
			
			if(usernameNotEmpty && usernameDifferentToOwnUsername && userDoesNotExistAlready) {
				App.input.addContactInputDisabled = true;
				App.$http.post('/rest/new-contact', { name: App.input.addContactInput }).then(function() {
					
					App.chat.contacts.push({
						name: App.input.addContactInput,
						messages: [],
						online: false
					});
					setToStorage(App);
					
					App.input.addContactInput = '';
					App.input.addContactInputDisabled = false;
					
					// set focus back to input field
					window.setTimeout(function() {
						App.$refs.addContactInput.focus();
					});
				}, function() {
					App.input.addContactInputDisabled = false;
				});
			}
			
		},
		submitMessage: function() {
			var App = this;
			var selectedContact = App.chat.selectedContact;
			var messageInput =  App.input.messageInput;
			var notary = App.input.notary;
			
			App.input.messageInputDisabled = true;
			App.$http.post('/rest/send-message', { message: messageInput, receiver: { name: selectedContact }, notary: notary }).then(function(response) {
				var messageId = response.body.messageId;
				
				App.chat.contacts.filter(function(item) {
					return item.name === selectedContact;
				})[0].messages.push({
					messageId: messageId,
					content: messageInput,
					isOwnMessage: true,
					notary: notary,
					approved: false
				});
				
				App.input.messageInput = '';
				App.input.messageInputDisabled = false;
				App.input.notary = false;
				
				setToStorage(App);
				
				// set focus back to input field
				window.setTimeout(function() {
					App.$refs.messageInput.focus();
				});
				
			}, function() {
				App.messageInputDisabled = false;
			});
		},
		switchNotaryOfCurrentMessage: function() {
			if (!this.input.messageInputDisabled && this.currentChatContact.online) {
				this.input.notary = !this.input.notary;
			}
		},
		logout: function() {
			var App = this;
			
			App.isLoading = true;
			App.$http.post('/rest/logout').then(function() {
				removeFromStorage();
				window.location.reload();
			}, function() {
				window.location.reload();
			});
		},
		reload: function() {
			window.location.reload();
		}
	},
	mounted: function() {
		var App = this;
		
		App.isLoading = true;
		var storageObject = getFromStorage();
		App.autoLogin(storageObject).then(function() {
			App.isLoading = false;
		}, function() {
			App.isLoading = false;
		});
	}
});

function getFromStorage() {
	var storageObject = window.localStorage.getItem(KEY_AUTH);
	if (storageObject) {
		var result = null;
		try { result = JSON.parse(storageObject); } catch (error) { result = null; }
		
		// adapt online status & selectedFriend
		if(result.chat && result.auth) {
			result.chat.selectedContact = '';
			for(var i=0; i<result.chat.contacts.length; i++) {
				result.chat.contacts[i].online = false;
			}
			return result;
		} else {
			return null;
		}
	} else {
		return null;
	}
}
function setToStorage(App) {
	var storageObject = { auth: App.auth, chat: App.chat };
	window.localStorage.setItem(KEY_AUTH, JSON.stringify(storageObject));
}
function removeFromStorage() {
	window.localStorage.removeItem(KEY_AUTH);
}

function getSubmittableContactList(App) {
	var contactArray = App.chat.contacts;
	var contactSubmitSet = [];
	
	for(var i=0; i < contactArray.length; i++) {
		contactSubmitSet.push({
			name: contactArray[i].name
		});
	}
	return contactSubmitSet;
}
